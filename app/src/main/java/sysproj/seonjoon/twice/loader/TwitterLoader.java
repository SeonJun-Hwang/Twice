package sysproj.seonjoon.twice.loader;

import android.content.Context;
import android.util.Log;

import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.internal.TwitterApi;
import com.twitter.sdk.android.core.internal.network.UrlUtils;
import com.twitter.sdk.android.core.internal.oauth.OAuthConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import okio.ByteString;
import sysproj.seonjoon.twice.BuildConfig;
import sysproj.seonjoon.twice.DataLoadCompleteCallback;
import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.staticdata.SNSTag;
import sysproj.seonjoon.twice.staticdata.UserSession;

public class TwitterLoader implements DataLoader {

    private final String TAG = "Twitter_Looader";
    private final String USER_AGENT = "Mozilla/5.0";
    private final String VERSION = "1.0";
    private final String SIGNATURE_METHOD = "HMAC-SHA1";
    private Context context;

    private TwitterLoader() {
    }

    private String constructSignatureBase(String restURL, String nonce, String timestamp) {
        // Get query parameters from request.
        final URI uri = URI.create(restURL);
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
        return System.nanoTime() + String.valueOf(Math.abs(RAND.nextLong()));
    }

    private String constructAuthorizationHeader(String nonce, String timestamp, String signature) {
        final StringBuilder sb = new StringBuilder("OAuth");
        appendParameter(sb, OAuthConstants.PARAM_CALLBACK, null);
        appendParameter(sb, OAuthConstants.PARAM_CONSUMER_KEY, BuildConfig.TwitterAPI);
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
    public void LoadTimeLineData(DataLoadCompleteCallback callback) {

        try {
            String restURL = SNSTag.TWITTER_BASE_URL + SNSTag.TWITTER_URL_TIMELINE + "?count=10";
            String nonce = generateNonce();
            String timestamp = Long.toString(System.currentTimeMillis() / 1000);
            String signatureBase = constructSignatureBase(restURL, nonce, timestamp);
            String signature = generateSignature(signatureBase);

            // Make Oauth Token
            String Oauth = constructAuthorizationHeader(nonce, timestamp, signature);

            //Log.e(TAG, "Oauth : " + Oauth);

            // TODO : Make Resizable MaxItemRequest
            URL url = null;

            url = new URL(restURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3000);
            conn.setRequestProperty("User-Agent", USER_AGENT);
            conn.setRequestProperty("Authorization", Oauth);

            //Log.e(TAG, "Timeline ResponseCode " + conn.getResponseCode());

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = br.readLine();

                JSONObject result = new JSONObject();
                result.accumulate("result", new JSONArray(line));

                callback.Complete(true, result);

                br.close();
            } else {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                String line = br.readLine();

                Log.e(TAG,line);
                callback.Complete(false, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void LoadSearchData(String searchTag, DataLoadCompleteCallback callback) {
        try {
            String restURL = SNSTag.TWITTER_BASE_URL + SNSTag.TWITTER_URL_SEARCH + "?count=10&q=" + searchTag;

            String nonce = generateNonce();
            String timestamp = Long.toString(System.currentTimeMillis() / 1000);
            String signatureBase = constructSignatureBase(restURL, nonce, timestamp);
            String signature = generateSignature(signatureBase);

            // Make Oauth Token
            String Oauth = constructAuthorizationHeader(nonce, timestamp, signature);

            Log.e(TAG, "Oauth : " + Oauth);

            // TODO : Make Resizable MaxItemRequest
            URL url = null;

            url = new URL(restURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3000);
            conn.setRequestProperty("User-Agent", USER_AGENT);
            conn.setRequestProperty("Authorization", Oauth);

            Log.e(TAG, "Search ResponseCode " + conn.getResponseCode());

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = br.readLine();

                JSONObject result = new JSONObject(line);
                callback.Complete(true, result);

                br.close();
            } else {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                String line = br.readLine();

                Log.e(TAG,line);
                callback.Complete(false, null);
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }
}
