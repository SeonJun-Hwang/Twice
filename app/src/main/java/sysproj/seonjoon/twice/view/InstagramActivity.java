package sysproj.seonjoon.twice.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;
import android.widget.CalendarView;

import sysproj.seonjoon.twice.R;


public class InstagramActivity extends Activity {

    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instagram_activity);

        webView = (WebView) findViewById(R.id.instargram_link_view);

        CalendarView view = new CalendarView(this);
    }
}
