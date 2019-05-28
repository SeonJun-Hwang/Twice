package sysproj.seonjoon.twice.parser;

import java.util.Map;

import sysproj.seonjoon.twice.staticdata.SNSTag;

public class InstagramTokenParser implements TokenParser {

    @Override
    public Object map2Token(Map<String, Object> data) {

        if (data == null)
            return null;

        String token = (String) data.get(SNSTag.InstagramTokenTag);

        return token;
    }
}
