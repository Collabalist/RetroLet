package collabalist.retroletLib.Callbacks;

import java.util.Map;

/**
 * Created by deepak on 13/3/18.
 */

public interface RequestListener {

    void beforeExecuting(String url, Map formInfo, int TAG);

    void onResponse(String response, int TAG);

    void onError(String error, String message, int TAG);
}
