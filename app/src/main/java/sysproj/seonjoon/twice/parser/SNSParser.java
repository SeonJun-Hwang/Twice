package sysproj.seonjoon.twice.parser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import sysproj.seonjoon.twice.entity.Post;
import sysproj.seonjoon.twice.entity.UserProfile;

public abstract class SNSParser {

    public abstract ArrayList<Post> parseTimeline(JSONObject object);
    public abstract ArrayList<Post> parseSearch(JSONObject object);

    protected abstract UserProfile parseUserProfile(JSONObject jsonObject) throws JSONException;
    protected abstract String parseCreatedDate(JSONObject jsonObject) throws JSONException;
}
