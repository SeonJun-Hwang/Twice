package sysproj.seonjoon.twice.view.custom.InstagramLogin;

import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class InstagramClient extends WebViewClient {

    private static final String TAG = "InstagramClient";

    InstagramURLCallBack callback;

    public InstagramClient(InstagramURLCallBack callback) {
        this.callback = callback;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();

        int tagPos = url.indexOf("#") + 1;
        if (tagPos > 0) {
            final String tag = "access_token=";
            final String token = url.substring(url.indexOf(tag) + tag.length());

            callback.onSuccessFoundURL(token);
        }

        return super.shouldOverrideUrlLoading(view, request);
    }
}
