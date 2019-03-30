package sysproj.seonjoon.twice.parser;

import com.twitter.sdk.android.core.AuthToken;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.Map;

import sysproj.seonjoon.twice.staticdata.SNSTag;

public class TwitterTokenParser implements TokenParser {
    @Override
    public Object map2Token(Map<String, Object> data) {
        if (data == null)
            return null;

        String token = (String) data.get(SNSTag.TwitterTokenTag);
        String tokenSecret = (String) data.get(SNSTag.TwitterTokenSecretTag);
        long uid = (long) data.get(SNSTag.TwitterUIDTag);
        String name = (String) data.get(SNSTag.TwitterUNameTag);

        TwitterAuthToken authToken = new TwitterAuthToken(token, tokenSecret);
        TwitterSession session = new TwitterSession(authToken, uid, name);

        return session;
    }
}
