package sysproj.seonjoon.twice.loader;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONException;
import org.json.JSONObject;

import sysproj.seonjoon.twice.DataLoadCompleteCallback;
import sysproj.seonjoon.twice.staticdata.SNSPermission;
import sysproj.seonjoon.twice.staticdata.UserSession;

public class FacebookLoader implements DataLoader {

    private Context context;

    public FacebookLoader(Context context) {
        this.context = context;
    }

    private final String TAG = "FACEBOOK_LOADER";

    @Override
    public void LoadUserProfileData(DataLoadCompleteCallback callback) {
        Log.e(TAG, "facebook profile load async start");

        GraphRequest request = GraphRequest.newGraphPathRequest(UserSession.FacebookToken,
                "/me", null);

        Bundle params = new Bundle();
        params.putString("fields", "name,email,picture{url}");

        request.setParameters(params);
        GraphResponse response = request.executeAndWait();
        JSONObject object = response.getJSONObject();

        callback.Complete(true, object);

        Log.e(TAG, "facebook profile load async start");
    }

    @Override
    public JSONObject LoadUserProfileData() {
        Log.e(TAG, "facebook profile load sync start");
        GraphRequest request = GraphRequest.newGraphPathRequest(UserSession.FacebookToken,
                "/me", null);

        Bundle params = new Bundle();
        params.putString("fields", "name,email,picture{url}");
        request.setParameters(params);
        GraphResponse response = request.executeAndWait();

        Log.e(TAG, response.toString());

        Log.e(TAG, "facebook profile load sync over");

        return response.getJSONObject();
    }

    @Override
    public void LoadTimeLineData(DataLoadCompleteCallback callback) {
        Log.e(TAG, "facebook timeline load async");

        GraphRequest request = GraphRequest.newGraphPathRequest(
                UserSession.FacebookToken,
                "/me/feed",
                null);

        Bundle parameters = new Bundle();
        parameters.putString("fields", SNSPermission.getFacebookField());
        parameters.putString("limit", PreferenceLoader.loadPreference(context, PreferenceLoader.KEY_FACEBOOK));
        request.setParameters(parameters);
        GraphResponse response = request.executeAndWait();
        JSONObject object = response.getJSONObject();

        callback.Complete(true, object);

        Log.e(TAG, "facebook timeline load async over");
    }

    @Override
    public JSONObject LoadTimeLineData() {
        Log.e(TAG, "facebook timeline load sync start");

        GraphRequest request = GraphRequest.newGraphPathRequest(
                UserSession.FacebookToken,
                "/me/feed",
                null);

        Bundle parameters = new Bundle();
        parameters.putString("fields", SNSPermission.getFacebookField());
        parameters.putString("limit", PreferenceLoader.loadPreference(context, PreferenceLoader.KEY_FACEBOOK));
        request.setParameters(parameters);
        GraphResponse response = request.executeAndWait();

        Log.e(TAG, "facebook timeline load sync end");

        return response.getJSONObject();
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

    public void LoadPagelist(DataLoadCompleteCallback callback) {

        GraphRequest request = GraphRequest.newGraphPathRequest(
                UserSession.FacebookToken,
                "/me",
                null);

        Bundle parameters = new Bundle();
        parameters.putString("fields", "accounts");
        request.setParameters(parameters);
        GraphResponse response = request.executeAndWait();

        Log.e(TAG, request.getParameters().toString());

        if (response != null) {
            callback.Complete(true, response.getJSONObject());
        } else
            callback.Complete(false, null);

        Log.e(TAG, "End Facebook Loading");

    }


}
