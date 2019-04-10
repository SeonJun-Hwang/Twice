package sysproj.seonjoon.twice.loader;

import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import sysproj.seonjoon.twice.DataLoadCompleteCallback;
import sysproj.seonjoon.twice.staticdata.UserSession;

public class FacebookLoader implements DataLoader {

    private final String TAG = "FACEBOOK_LOADER";

    /* Load Data */
    @Override
    public void LoadTimeLineData(@Nullable DataLoadCompleteCallback callback) {
        Log.e(TAG, "Start Facebook Loading");

        GraphRequest request = GraphRequest.newGraphPathRequest(
                UserSession.FacebookToken,
                "/me/feed",
                null);

        GraphResponse response = request.executeAndWait();

        if (response != null)
            callback.Complete(true, response.getJSONObject());
        else
            callback.Complete(false, null);

        Log.e(TAG, "End Facebook Loading");
    }

    @Override
    public void LoadSearchData(String searchTag, DataLoadCompleteCallback callback) {
        callback.Complete(true, null);
    }


}
