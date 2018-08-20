package collabalist.retroletLib;

import collabalist.retroletLib.RequestHelper.GET;
import collabalist.retroletLib.RequestHelper.POST;

/**
 * Created by deepak on 13/3/18.
 */

public class RetroLet {

    public static GET get(String baseURL, String endPoint) {
        return new GET(baseURL, endPoint);
    }

    public static POST post(String baseURL, String endPoint) {
        return new POST(baseURL, endPoint);
    }
}
