package collabalist.retroletLib.Callbacks;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by deepak on 13/3/18.
 */

public interface Requests<T> {
    @GET
    Call<Object> get(@Url String url, @HeaderMap Map<String, String> headers, @QueryMap Map<String, String> param);

    @FormUrlEncoded
    @POST
    Call<Object> post(@Url String url, @HeaderMap Map<String, String> headers, @FieldMap Map<String, String> param);

    @Multipart
    @POST
    Call<Object> postUpload(@Url String url, @HeaderMap Map<String, Object> headers, @QueryMap Map<String, Object> paramsField, @Part List<MultipartBody.Part> files);

    @Multipart
    @GET
    Call<Object> getUpload(@Url String url, @HeaderMap Map<String, Object> headers, @FieldMap Map<String, Object> paramsField, @Part List<MultipartBody.Part> files);
}
