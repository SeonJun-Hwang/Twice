package sysproj.seonjoon.twice.parser;

import org.json.JSONObject;

import java.util.ArrayList;

import sysproj.seonjoon.twice.entity.Post;

public interface SNSParser {

    ArrayList<Post> parseTimeline(JSONObject object);
    ArrayList<Post> parseSearch(JSONObject object);
}
