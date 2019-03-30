package sysproj.seonjoon.twice.parser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sysproj.seonjoon.twice.entity.TimeLineItem;
import sysproj.seonjoon.twice.staticdata.SNSTag;

public class TwitterParser implements SNSParser {

    private static final String TAG = "TwitterParser";

    @Override
    public List<TimeLineItem> parseItem(JSONObject object) {

        List<TimeLineItem> result = new ArrayList<>();

        try {
            JSONArray requestData = object.getJSONArray("result");

            for (int i = 0; i < requestData.length(); i++)
            {
                //Log.e(TAG, (i + 1) + " : " + requestData.getJSONObject(i).toString(2));
                JSONObject item = requestData.getJSONObject(i);

                String userName= item.getJSONObject("user").getString("name");
                String text = item.getString("text");
                String profileUrl = item.getJSONObject("user").getString("profile_image_url");


                result.add(new TimeLineItem(SNSTag.Twitter, userName, text, profileUrl ,null));
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        return result;
    }
}
