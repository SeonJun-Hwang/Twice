package sysproj.seonjoon.twice.loader;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import sysproj.seonjoon.twice.DataLoadCompleteCallback;
import sysproj.seonjoon.twice.staticdata.UserSession;

public class InstagramLoader implements DataLoader {

    private static final String TAG = "InstagramLoader";

    @Override
    public void LoadUserProfileData(DataLoadCompleteCallback callback) {
    }

    @Override
    public JSONObject LoadUserProfileData() {
        JSONObject res = null;

        String urls = "https://api.instagram.com/v1/users/self/?access_token=" + UserSession.InstagramToekn;

        try {
            URL url = new URL(urls);

            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(3000);
            urlConnection.connect();

            String line;

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            line = br.readLine();

            res = new JSONObject(line);
            br.close();
            urlConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return res;
    }

    @Override
    public void LoadTimeLineData(DataLoadCompleteCallback callback) {

    }

    @Override
    public JSONObject LoadTimeLineData() {

        JSONObject res = null;

        String urls = "https://api.instagram.com/v1/users/self/media/recent/?access_token=" + UserSession.InstagramToekn;

        try {
            URL url = new URL(urls);

            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(3000);
            urlConnection.connect();

            String line;

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            line = br.readLine();

            res = new JSONObject(line);
            br.close();
            urlConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return res;
    }

    @Override
    public void LoadTimeLineData(DataLoadCompleteCallback callback, long maxId) {

    }

    @Override
    public void LoadSearchData(String searchTag, DataLoadCompleteCallback callback) {

    }
}
