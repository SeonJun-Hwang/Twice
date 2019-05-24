package sysproj.seonjoon.twice.parser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import sysproj.seonjoon.twice.entity.Post;
import sysproj.seonjoon.twice.entity.UserProfile;

public class InstagramParser extends SNSParser {

    @Override
    public ArrayList<Post> parseTimeline(JSONObject object) {
        return null;
    }

    @Override
    public ArrayList<Post> parseSearch(JSONObject object) {
        return null;
    }

    @Override
    public UserProfile parseUserProfile(JSONObject jsonObject) {

        JSONObject dataObject = null;
        String userName = "Unknown";
        String profileImage = null;
        try {
            dataObject = jsonObject.getJSONObject("data");

            userName = dataObject.getString("username");
            profileImage = dataObject.getString("profile_picture");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new UserProfile.Builder(userName).profileImage(profileImage).build();
    }

    @Override
    protected String parseCreatedDate(JSONObject jsonObject) throws JSONException {
        return null;
    }

    @Override
    protected long parseID(JSONObject jsonObject) throws JSONException {
        return 0;
    }
}
