package collabalist.retroletLib.RequestHelper;

import java.io.File;
import java.util.HashMap;

/**
 * Created by deepak on 13/3/18.
 */

public class GET {
    String baseURL, endPoint;
    HashMap<String, String> queries = new HashMap<>();
    HashMap<String, String> headers = new HashMap<>();
    HashMap<String, File> files = new HashMap<>();
    int TAG = -1;

    public GET(String baseURL, String endPoint) {
        this.baseURL = baseURL;
        this.endPoint = endPoint;
    }

    public GET setTAG(int TAG) {
        this.TAG = TAG;
        return this;
    }


    public GET addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public GET addQuery(String key, String value) {
        queries.put(key, value);
        return this;
    }

    public GET addFile(String key, File file) {
        files.put(key, file);
        return this;
    }

    public RequestBuilder build() {
        RequestBuilder builder = new RequestBuilder(this.baseURL, 1);
        if (!this.queries.isEmpty())
            builder.queries = queries;
        builder.headers = headers;
        builder.files = files;
        builder.TAG = TAG;
        builder.endPoint = endPoint;
        return builder;
    }

}
