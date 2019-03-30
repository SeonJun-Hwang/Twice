package sysproj.seonjoon.twice.parser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import sysproj.seonjoon.twice.entity.TimeLineItem;
import sysproj.seonjoon.twice.staticdata.SNSTag;
import sysproj.seonjoon.twice.staticdata.UserSession;

public class FacebookParser implements SNSParser {

    private final static String TAG = "FacebookParser";
    private ArrayList<Bitmap> imageList;

    public FacebookParser() {
        imageList = new ArrayList<>();
    }

    @Override
    public List<TimeLineItem> parseItem(JSONObject object) {

        if (object == null)
            return null;

        try {
            Log.e(TAG, object.toString(2));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<TimeLineItem> resultList = new ArrayList<>();

        try {
            JSONArray dataArray = object.getJSONArray("data");

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject jsonObject = dataArray.getJSONObject(i);

                String uid = jsonObject.getString("id").split("_")[0];
                String message = null;

                String uName = GraphRequest.newGraphPathRequest(
                        UserSession.FacebookToken,
                        "/" + uid + "/",
                        null).executeAndWait().getJSONObject().getString("name");

                if (!jsonObject.isNull("message"))
                    message = jsonObject.getString("message");

                if (uName == null || uName.isEmpty())
                    uName = "Unknown";

                resultList.add(new TimeLineItem(SNSTag.Facebook, uName, message, null ,null));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        resultList.add(new TimeLineItem(SNSTag.Facebook, "윤기재", "Hello Facebook",null, null));
        resultList.add(new TimeLineItem(SNSTag.Instagram, "홍승표", "Hello Instagram",null, null));
        resultList.add(new TimeLineItem(SNSTag.Twitter, "서동환", "Hello Twitter",null, null));

        return resultList;
    }

    private class readImage extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                imageList.add(BitmapFactory.decodeFile("./yoongijae"));

            } catch (MalformedURLException e) {
                Log.e("readImage", "readImage MalformedURLException");
            } catch (IOException e) {
                Log.e("readImage", "readImage IOException");
            }

            return null;
        }
    }
}
