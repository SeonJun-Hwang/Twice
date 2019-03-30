package sysproj.seonjoon.twice.parser;

import org.json.JSONObject;
import java.util.List;

import sysproj.seonjoon.twice.entity.TimeLineItem;

public interface SNSParser {

    List<TimeLineItem> parseItem(JSONObject object);
}
