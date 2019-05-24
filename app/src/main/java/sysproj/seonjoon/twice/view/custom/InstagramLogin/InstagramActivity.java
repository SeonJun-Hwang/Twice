package sysproj.seonjoon.twice.view;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CalendarView;

import sysproj.seonjoon.twice.R;


public class InstagramActivity extends Activity {

    private WebView webView;
    public static final int reqCode = 3837;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instagram_activity);

        webView = (WebView) findViewById(R.id.instargram_link_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setAllowFileAccess(false);
        webView.getSettings().setUseWideViewPort(true);

        webView.loadUrl("https://api.instagram.com/oauth/authorize/?client_id=" +
                "0193edda4dd44b14876ef8440b2fc38a" +
                "&redirect_uri=" + "" +
                "https://girlfriend-yerin.tistory.com&response_type=token");

        webView.setWebViewClient(new WebViewClient());


    }
}
