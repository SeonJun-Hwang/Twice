package sysproj.seonjoon.twice.parser;

import org.json.JSONObject;
import java.util.List;

import sysproj.seonjoon.twice.entity.Post;

public interface SNSParser {

    List<Post> parseItem(JSONObject object);
}
