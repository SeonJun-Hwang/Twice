package sysproj.seonjoon.twice.parser;

import androidx.annotation.Nullable;

import com.facebook.AccessToken;
import com.facebook.AccessTokenSource;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import sysproj.seonjoon.twice.staticdata.SNSTag;

public class FacebookTokenParser implements TokenParser {

    private AccessTokenSource string2TokenSource(@Nullable String str) {
        if (str.equals(AccessTokenSource.FACEBOOK_APPLICATION_SERVICE.toString()))
            return AccessTokenSource.FACEBOOK_APPLICATION_SERVICE;
        else if (str.equals(AccessTokenSource.FACEBOOK_APPLICATION_NATIVE.toString()))
            return AccessTokenSource.FACEBOOK_APPLICATION_NATIVE;
        else if (str.equals(AccessTokenSource.FACEBOOK_APPLICATION_WEB.toString()))
            return AccessTokenSource.FACEBOOK_APPLICATION_WEB;

        return null;
    }

    private Set<String> string2Set(@Nullable String str) {
        Set<String> result = new HashSet<>();

        String[] items = str.split(" ");

        for (String item : items) {
            if (!item.isEmpty())
                result.add(item.replaceAll(" ", ""));
        }

        return result;
    }

    @Override
    public Object map2Token(Map<String, Object> data) {
        if (data == null)
            return null;

        String token = (String) data.get(SNSTag.FacebookTokenTag);
        String uID = (String) data.get(SNSTag.FacebookUIDTag);
        String aID = (String) data.get(SNSTag.FacebookAIDTag);
        AccessTokenSource source = string2TokenSource(SNSTag.FacebookTokenSourceTag);
        Set<String> permission = string2Set(SNSTag.FacebookPermissionTag);
        Set<String> dPermission = string2Set(SNSTag.FacebookDPermissionTag);
        Date expiration = new Date((String) data.get(SNSTag.FacebookExpTimeTag));
        Date lastRefresh = new Date((String) data.get(SNSTag.FacebookLastTimeTag));
        Date dataAcc = new Date((String) data.get(SNSTag.FacebookDataAccExpTimeTag));

        return new AccessToken(token, aID, uID, permission, dPermission, source, expiration, lastRefresh, dataAcc);
    }
}
