package collabalist.retroletLib.RequestHelper;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

/**
 * Created by deepak on 13/3/18.
 */

public class RetroLet {

    String baseURL, endPoint;
    HashMap<String, String> queries = new HashMap<>();
    HashMap<String, String> headers = new HashMap<>();
    HashMap<String, File> files = new HashMap<>();
    int TAG = -1, TYPE = -1;

    //TYPE=1 -> GET
    //TYPE=2 -> POST

    //Save last api here
    SharedPreferences preferences;


    RetroLet(String baseURL, String endPoint, int TYPE) {
        this.baseURL = baseURL;
        this.endPoint = endPoint;
        this.TYPE = TYPE;
    }

    public static RetroLet get(String baseURL, String endPoint) {
        return new RetroLet(baseURL, endPoint, 1);
    }

    public static RetroLet post(String baseURL, String endPoint) {
        return new RetroLet(baseURL, endPoint, 2);
    }

    public RetroLet setTAG(int TAG) {
        this.TAG = TAG;
        return this;
    }

    public RetroLet addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }


    public RetroLet addQuery(String key, String value) {
        queries.put(key, value);
        return this;
    }

    public RetroLet addFile(String key, File file) {
        files.put(key, file);
        return this;
    }

    public RetroLet addHeaders(HashMap<String, String> headers) {
        this.headers.putAll(headers);
        return this;
    }

    public RetroLet addQueries(HashMap<String, String> queries) {
        this.queries.putAll(queries);
        return this;
    }

    public RetroLet addFiles(HashMap<String, File> files) {
        this.files.putAll(files);
        return this;
    }

    public RequestBuilder build(Context context) {

        Context mContext = context.getApplicationContext();
        preferences = mContext.getSharedPreferences("collabalist_RetroLet", Context.MODE_PRIVATE);
        if (!this.queries.isEmpty()) {
            JSONObject object = new JSONObject(queries);
            preferences.edit().putString("queries", object.toString());
        } else
            preferences.edit().putString("queries", "");
        if (this.headers.isEmpty()) {
            preferences.edit().putString("headers", "");
        } else {
            JSONObject obj = new JSONObject(headers);
            preferences.edit().putString("headers", obj.toString());
        }
        if (this.files.isEmpty()) {
            preferences.edit().putString("files", "");
        } else {
            String[] keys = new String[files.size()];
            files.keySet().toArray(keys);
            JSONObject obj = new JSONObject();
            try {
                for (int i = 0; i < keys.length; i++) {
                    obj.put(keys[i], files.get(keys[i]).getPath() + "");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            preferences.edit().putString("files", obj.toString());
        }
        preferences.edit().commit();
        RequestBuilder builder = new RequestBuilder(preferences,this.baseURL, TYPE);
        if (!this.queries.isEmpty())
            builder.queries = queries;
        builder.headers = headers;
        builder.files = files;
        builder.TAG = TAG;
        builder.endPoint = endPoint;
        return builder;
    }
}
