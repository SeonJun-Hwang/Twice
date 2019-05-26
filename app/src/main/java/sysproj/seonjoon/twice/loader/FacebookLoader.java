package sysproj.seonjoon.twice.loader;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import sysproj.seonjoon.twice.DataLoadCompleteCallback;
import sysproj.seonjoon.twice.staticdata.SNSPermission;
import sysproj.seonjoon.twice.staticdata.UserSession;

public class FacebookLoader implements DataLoader {

    private Context context;

    private LoadAccounts loadAccounts;

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
                "/me/feed?limit=" + PreferenceLoader.loadPreference(context, PreferenceLoader.KEY_FACEBOOK),
                null);

        Bundle parameters = new Bundle();


        parameters.putString("fields", SNSPermission.getFacebookField());
        //parameters.putString("limit", PreferenceLoader.loadPreference(context, PreferenceLoader.KEY_FACEBOOK));
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

        Log.e(TAG, "Deault : " + PreferenceLoader.loadPreferenceFromDefault(context, PreferenceLoader.KEY_FACEBOOK));

        parameters.putString("fields", SNSPermission.getFacebookField());
        parameters.putString("limit", PreferenceLoader.loadPreferenceFromDefault(context, PreferenceLoader.KEY_FACEBOOK));
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

        if (loadAccounts == null)
            loadAccounts = new LoadAccounts(callback);
        loadAccounts.execute();


        Log.e(TAG, "End Facebook Loading");

    }

    private class LoadAccounts extends AsyncTask<Void, Void, Void> {

        private DataLoadCompleteCallback callback;

        public LoadAccounts(DataLoadCompleteCallback callback){
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... Void) {
            String urls = "https://graph.facebook.com/me/accounts?access_token=" + UserSession.FacebookToken.getToken();

            try {
                URL url = new URL(urls);

                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setConnectTimeout(3000);
                conn.setReadTimeout(3000);

                String line = null;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK)
                    callback.Complete(false, null);
                else
                {
                    Log.e(TAG, "Account Result : " + conn.getResponseCode());

                    line = br.readLine();

                    callback.Complete(true, new JSONObject(line));
                }

                br.close();
                conn.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }


}
