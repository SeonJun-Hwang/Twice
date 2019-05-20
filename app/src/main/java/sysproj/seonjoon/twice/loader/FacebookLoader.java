package sysproj.seonjoon.twice.loader;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONException;
import org.json.JSONObject;

import sysproj.seonjoon.twice.DataLoadCompleteCallback;
import sysproj.seonjoon.twice.entity.FacebookPageVO;
import sysproj.seonjoon.twice.staticdata.SNSPermission;
import sysproj.seonjoon.twice.staticdata.UserSession;

public class FacebookLoader implements DataLoader {

    private Context context;

    public FacebookLoader(){
        context = null;
    }

    public FacebookLoader(Context context) {
        this.context = context;
    }

    private final String TAG = "FACEBOOK_LOADER";

    @Override
    public void LoadUserProfileData(DataLoadCompleteCallback callback) {

    }

    /* Load Data */
    @Override
    public void LoadTimeLineData(DataLoadCompleteCallback callback) {
        Log.e(TAG, "Start Facebook Loading");

        GraphRequest request = GraphRequest.newGraphPathRequest(
                UserSession.FacebookToken,
                "/me/feed",
                null);

        Bundle parameters = new Bundle();
        parameters.putStringArrayList("fields", SNSPermission.getFacebookField(context));
        request.setParameters(parameters);
        GraphResponse response = request.executeAndWait();

        Log.e(TAG, request.getParameters().toString());

        if (response != null)
            callback.Complete(true, response.getJSONObject());
        else
            callback.Complete(false, null);

        Log.e(TAG, "End Facebook Loading");
    }

    @Override
    public void LoadTimeLineData(DataLoadCompleteCallback callback, long maxId) {
        Log.e(TAG, "Start Facebook Loading More");

        callback.Complete(false, null);

        Log.e(TAG, "End Facebook Loading");
    }

    @Override
    public void LoadSearchData(String searchTag, DataLoadCompleteCallback callback) {
        callback.Complete(true, null);
    }

    public void LoadPagelist(DataLoadCompleteCallback callback){

        GraphRequest request = GraphRequest.newGraphPathRequest(
                UserSession.FacebookToken,
                "/me/accounts",
                null);

        GraphResponse response = request.executeAndWait();

        Log.e(TAG, request.getParameters().toString());

        if (response != null) {
            try {
                Log.e(TAG, response.getJSONObject().toString(2));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            callback.Complete(true, response.getJSONObject());
        }
        else
            callback.Complete(false, null);

        Log.e(TAG, "End Facebook Loading");

    }


}
