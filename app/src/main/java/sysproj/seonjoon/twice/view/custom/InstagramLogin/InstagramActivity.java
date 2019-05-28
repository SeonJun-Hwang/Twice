package sysproj.seonjoon.twice.view.custom.InstagramLogin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CalendarView;

import sysproj.seonjoon.twice.BuildConfig;
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

        // https://api.instagram.com/oauth/authorize/?client_id=0193edda4dd44b14876ef8440b2fc38a&redirect_uri=https://girlfriend-yerin.tistory.com&response_type=token

        webView.loadUrl("https://api.instagram.com/oauth/authorize/?" +
                "client_id=" + BuildConfig.InstagramClient +
                "&redirect_uri=" + BuildConfig.InstagramRedirectionURL +
                "&response_type=token");

        InstagramURLCallBack urlCallback = new InstagramURLCallBack() {

            @Override
            public void onSuccessFoundURL(String found) {
                Intent result = new Intent();
                result.putExtra("result", found);

                setResult(Activity.RESULT_OK, result);
                finish();
            }

            @Override
            public void onFailureFoundURL(String errorMessage) {

                Intent result = new Intent();
                result.putExtra("fail_message", errorMessage);

                setResult(Activity.RESULT_CANCELED, result);
                fileList();
            }
        };

        webView.setWebViewClient(new InstagramClient(urlCallback));
    }

    @Override
    public void onBackPressed() {
        Intent result = new Intent();
        result.putExtra("result", "취소");
        setResult(RESULT_CANCELED, result);
        super.onBackPressed();
    }
}
