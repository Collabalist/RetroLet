package collabalist.retroletLib.RequestHelper;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import collabalist.retroletLib.Callbacks.RequestListener;
import collabalist.retroletLib.Callbacks.Requests;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by deepak on 13/3/18.
 */

public class RequestBuilder {
    String baseUrl, endPoint;
    HashMap<String, String> queries, headers;
    HashMap<String, File> files;
    RequestListener listener;
    OkHttpClient.Builder httpClient;
    Call<Object> call = null;
    int ReqType = -1, TAG = -1;

    SharedPreferences preferences;

    RequestBuilder(SharedPreferences preferences, String baseUrl, int reqType) {
        this.preferences = preferences;
        this.baseUrl = baseUrl;
        this.ReqType = reqType;
        httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(50, TimeUnit.SECONDS);
        httpClient.readTimeout(50, TimeUnit.SECONDS);
        httpClient.writeTimeout(50, TimeUnit.SECONDS);
    }

    public RequestBuilder setTimeOut(long connectTimeout, long readTimeout, long writeTimeout) {
        httpClient.connectTimeout(connectTimeout, TimeUnit.SECONDS);
        httpClient.readTimeout(readTimeout, TimeUnit.SECONDS);
        httpClient.writeTimeout(writeTimeout, TimeUnit.SECONDS);
        return this;
    }

    public void execute(RequestListener listener) {
        this.listener = listener;
        if (baseUrl.endsWith("/")) {
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .baseUrl(this.baseUrl).build();
            Requests requests = retrofit.create(Requests.class);
            if (files.isEmpty()) {
                if (ReqType == 1) {
                    call = requests.get(baseUrl + endPoint, headers, queries);
                } else if (ReqType == 2) {
                    call = requests.post(baseUrl + endPoint, headers, queries);
                }
            } else {
                if (ReqType == 1) {
                    call = requests.getUpload(baseUrl + endPoint, headers, queries, getMultipleFile(files));
                } else if (ReqType == 2) {
                    call = requests.postUpload(baseUrl + endPoint, headers, queries, getMultipleFile(files));
                }
            }
            preferences.edit().putString("requestType", ReqType == 1 ? "GET" : "POST").commit();
            if (call != null) {
                preferences.edit().putString("url", call.request().url().toString() + "").commit();
                this.listener.beforeExecuting(call.request().url().toString(), getFormInfo(queries, headers), this.TAG);
                call.enqueue(responseCallback);
            }
        } else {
            this.listener.onError("Base Url format is wrong.",
                    "Base URL should be end with \"/\"", TAG);
        }
    }

    Callback<Object> responseCallback = new Callback<Object>() {
        @Override
        public void onResponse(Call<Object> call, Response<Object> response) {
            if (response.body() == null) {
                preferences.edit().putString("response", "Raw: " + response.raw()
                        + "\nMessage: " + response.message()
                        + "\nStatus: " + response.code()
                        + "\nerrorBody: " + response.toString()).commit();
                listener.onError("FatalError",
                        "Raw: " + response.raw()
                                + "\nMessage: " + response.message()
                                + "\nStatus: " + response.code()
                                + "\nerrorBody: " + response.toString(), TAG);
            } else {
                JSONObject test = null;

                JsonObject obj = new JsonObject();
                if (response.body() instanceof LinkedTreeMap) {
                    LinkedTreeMap linkedTreeMap = (LinkedTreeMap) response.body();
                    obj = new Gson().toJsonTree(linkedTreeMap).getAsJsonObject();
                    preferences.edit().putString("response", "" + obj.toString()).commit();
                } else if (response.body() instanceof JsonObject) {
                    obj = (JsonObject) response.body();
                    preferences.edit().putString("response", "" + obj.toString()).commit();
                } else {
                    try {
                        test = new JSONObject(response.body() + "");
                        preferences.edit().putString("response", "" + test.toString()).commit();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        preferences.edit().putString("response", "" + response.raw()).commit();
                    }
                    try {
                        JsonObject object = new JsonParser().parse(response.body() + "").getAsJsonObject();
                        if (object != null)
                            obj = object;
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                        preferences.edit().putString("response", "" + response.raw()).commit();
                    }
                }
                listener.onResponse(obj.toString(), TAG);
            }
        }

        @Override
        public void onFailure(Call<Object> call, Throwable t) {
            t.printStackTrace();
            if (t instanceof IOException || t instanceof UnknownHostException
                    || t instanceof SocketTimeoutException) {
                listener.onError("Network Error", "Internet is not working", TAG);
                preferences.edit().putString("response", "Internet is not working").commit();
            } else {
                listener.onError("Invalid Request", "" + t.getMessage(), TAG);
                preferences.edit().putString("response", "Invalid Request: \n" + t.getMessage()).commit();
            }
        }
    };

    String queriesToBulk(HashMap<String, String> queries, HashMap<String, File> files) {
        String temp = "";
        if (queries != null && !queries.isEmpty())
            for (String key : queries.keySet()) {
                temp = temp + key + ":" + queries.get(key) + "\n";
            }
        if (files != null && !files.isEmpty())
            for (String key : files.keySet()) {
                temp = temp + key + ":" + files.get(key).getPath() + "\n";
            }
        if (temp.equals(""))
            temp = "No Queries..!";
        return temp;
    }

    Map getFormInfo(HashMap<String, String> queries, HashMap<String, String> headers) {
        Map t = new HashMap();
        t.put("Queries", queries);
        t.put("Headers", headers);
        t.put("BulkEdit", queriesToBulk(queries, files));
        return t;
    }

    List<MultipartBody.Part> getMultipleFile(HashMap<String, File> files) {
        List<MultipartBody.Part> bodyHashMap = new ArrayList<>();
        for (Map.Entry<String, File> stringStringEntry : files.entrySet()) {
            bodyHashMap.add(getRequestBodyImage(stringStringEntry.getValue(), stringStringEntry.getKey()));
        }
        return bodyHashMap;
    }

    MultipartBody.Part getRequestBodyImage(File file, String key) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData(key, file.getName(), requestFile);
        return body;
    }
}
