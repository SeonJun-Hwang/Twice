package sysproj.seonjoon.twice.Loader;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.internal.network.UrlUtils;
import com.twitter.sdk.android.core.internal.oauth.OAuthConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import javax.annotation.Nullable;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import okio.ByteString;
import sysproj.seonjoon.twice.DataLoadCompleteCallback;
import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.staticdata.UserSession;

public class TwitterLoader implements DataLoader {

    private final String TAG = "Twitter_Looader";
    private final String USER_AGENT = "Mozilla/5.0";
    private final String VERSION = "1.0";
    private final String SIGNATURE_METHOD = "HMAC-SHA1";
    private final String apiUrl = "https://api.twitter.com/1.1/statuses/home_timeline.json?count=10";
    private Context context;

    private TwitterLoader() {
    }

    private String constructSignatureBase(String nonce, String timestamp) {
        // Get query parameters from request.
        final URI uri = URI.create(apiUrl);
        final TreeMap<String, String> params = UrlUtils.getQueryParams(uri, true);

        // Add OAuth parameters.
        params.put(OAuthConstants.PARAM_CONSUMER_KEY, context.getString(R.string.CONSUMER_KEY));
        params.put(OAuthConstants.PARAM_NONCE, nonce);
        params.put(OAuthConstants.PARAM_SIGNATURE_METHOD, SIGNATURE_METHOD);
        params.put(OAuthConstants.PARAM_TIMESTAMP, timestamp);
        params.put(OAuthConstants.PARAM_TOKEN, UserSession.TwitterToken.getAuthToken().token);
        params.put(OAuthConstants.PARAM_VERSION, VERSION);

        // Construct the signature base.
        final String baseUrl = uri.getScheme() + "://" + uri.getHost() + uri.getPath();

        Log.e(TAG, baseUrl);
        final StringBuilder sb = new StringBuilder()
                .append("GET")
                .append('&')
                .append(UrlUtils.percentEncode(baseUrl))
                .append('&')
                .append(getEncodedQueryParams(params));
        return sb.toString();
    }

    private String getEncodedQueryParams(TreeMap<String, String> params) {
        final StringBuilder paramsBuf = new StringBuilder();
        final int numParams = params.size();
        int current = 0;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            paramsBuf.append(UrlUtils.percentEncode(UrlUtils.percentEncode(entry.getKey())))
                    .append("%3D")
                    .append(UrlUtils.percentEncode(UrlUtils.percentEncode(entry.getValue())));
            current += 1;
            if (current < numParams) {
                paramsBuf.append("%26");
            }
        }
        return paramsBuf.toString();
    }

    private String generateSignature(String signatureBase) {
        try {
            final String key = new StringBuilder().
                    append(UrlUtils.urlEncode(context.getString(R.string.CONSUMER_SECRET))).
                    append('&').
                    append(UrlUtils.urlEncode(UserSession.TwitterToken.getAuthToken().secret)).toString();

            // Calculate the signature by passing both the signature base and signing key to the
            // HMAC-SHA1 hashing algorithm
            final byte[] signatureBaseBytes = signatureBase.getBytes(UrlUtils.UTF8);
            final byte[] keyBytes = key.getBytes(UrlUtils.UTF8);
            final SecretKey secretKey = new SecretKeySpec(keyBytes, "HmacSHA1");
            final Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(secretKey);
            final byte[] signatureBytes = mac.doFinal(signatureBaseBytes);
            return ByteString.of(signatureBytes, 0, signatureBytes.length).base64();
        } catch (InvalidKeyException e) {
            Twitter.getLogger().e(TwitterCore.TAG, "Failed to calculate signature", e);
            return "";
        } catch (NoSuchAlgorithmException e) {
            Twitter.getLogger().e(TwitterCore.TAG, "Failed to calculate signature", e);
            return "";
        } catch (UnsupportedEncodingException e) {
            Twitter.getLogger().e(TwitterCore.TAG, "Failed to calculate signature", e);
            return "";
        }
    }

    private String generateNonce() {
        SecureRandom RAND = new SecureRandom();
        return String.valueOf(System.nanoTime()) + String.valueOf(Math.abs(RAND.nextLong()));
    }

    String constructAuthorizationHeader(String nonce, String timestamp, String signature) {
        final StringBuilder sb = new StringBuilder("OAuth");
        appendParameter(sb, OAuthConstants.PARAM_CALLBACK, null);
        appendParameter(sb, OAuthConstants.PARAM_CONSUMER_KEY, "H3qNM38a3TzDXpWz6yY1hknFy");
        appendParameter(sb, OAuthConstants.PARAM_NONCE, nonce);
        appendParameter(sb, OAuthConstants.PARAM_SIGNATURE, signature);
        appendParameter(sb, OAuthConstants.PARAM_SIGNATURE_METHOD, SIGNATURE_METHOD);
        appendParameter(sb, OAuthConstants.PARAM_TIMESTAMP, timestamp);
        appendParameter(sb, OAuthConstants.PARAM_TOKEN, UserSession.TwitterToken.getAuthToken().token);
        appendParameter(sb, OAuthConstants.PARAM_VERSION, VERSION);
        // Remove the extra ',' at the end.
        return sb.substring(0, sb.length() - 1);
    }

    private void appendParameter(StringBuilder sb, String name, String value) {
        if (value != null) {
            sb.append(' ')
                    .append(UrlUtils.percentEncode(name)).append("=\"")
                    .append(UrlUtils.percentEncode(value)).append("\",");
        }
    }

    public TwitterLoader(Context context) {
        this.context = context;
    }

    @Override
    public void LoadTimeLineData(DataLoadCompleteCallback callbacks) {

        try {
            String nonce = generateNonce();
            String timestamp = Long.toString(System.currentTimeMillis() / 1000);
            String signatureBase = constructSignatureBase(nonce, timestamp);
            String signature = generateSignature(signatureBase);

            // Make Oauth Token
            String Oauth = constructAuthorizationHeader(nonce, timestamp, signature);

            Log.e(TAG, "Oauth : " + Oauth);

            // TODO : Make Resizable MaxItemRequest
            URL url = null;

            url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3000);
            conn.setRequestProperty("User-Agent", USER_AGENT);
            conn.setRequestProperty("Authorization", Oauth);

            Log.e(TAG, "ResponseCode " + conn.getResponseCode());

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = br.readLine();

                JSONObject result = new JSONObject();
                result.accumulate("result", new JSONArray(line));

                callbacks.Complete(true, result);

                br.close();
            } else {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                String line = br.readLine();

                Log.e(TAG,line);
                callbacks.Complete(false, null);
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }
}