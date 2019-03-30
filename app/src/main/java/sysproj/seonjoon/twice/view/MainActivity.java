package sysproj.seonjoon.twice.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sysproj.seonjoon.twice.DataLoadCompleteCallback;
import sysproj.seonjoon.twice.Loader.DataLoader;
import sysproj.seonjoon.twice.Loader.FacebookLoader;
import sysproj.seonjoon.twice.Loader.TwitterLoader;
import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.TimeLineItem;
import sysproj.seonjoon.twice.manager.LoginManager;
import sysproj.seonjoon.twice.parser.FacebookParser;
import sysproj.seonjoon.twice.parser.SNSParser;
import sysproj.seonjoon.twice.parser.TwitterParser;
import sysproj.seonjoon.twice.staticdata.UserSession;

public class MainActivity extends Activity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MAIN_ACTIVITY";

    private Context mContext;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager recyclerLayoutManager;
    private TimelineRecyclerAdapter timelineAdapater;
    private ArrayList<TimeLineItem> contents;

    private DrawerLayout personDrawer;
    private NavigationView personNavigation;

    private ImageView openMenuButton;
    private ImageView openSettingButton;

    private long parsedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        contents = new ArrayList<>();

        setLayout();
        setListener();

        MakeTimeLineAsync makeTimeLineAsync = new MakeTimeLineAsync();
        makeTimeLineAsync.execute();
    }

    private void setLayout() {
        openMenuButton = (ImageView) findViewById(R.id.main_profile_setting);
        openSettingButton = (ImageView) findViewById(R.id.main_setting);

        // Recycler Set
        recyclerView = (RecyclerView) findViewById(R.id.main_time_line_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerLayoutManager);
        timelineAdapater = new TimelineRecyclerAdapter(mContext, contents);
        recyclerView.setAdapter(timelineAdapater);

        // User Drawer Layout Set
        personDrawer = (DrawerLayout) findViewById(R.id.main_drawer_user);
        personNavigation = (NavigationView) findViewById(R.id.drawer_user_navigation);
        personDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        // Setting Drawer Layout Sets
    }

    private void setListener() {
        openMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "Click Open Menu Button");
                if (!personDrawer.isDrawerOpen(GravityCompat.START))
                    personDrawer.openDrawer(Gravity.LEFT);
            }
        });

        personNavigation.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.drawer_logout:
                askLogout();
                break;
        }
        return false;
    }

    private void askLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.main_dialog_logout_message)
                .setPositiveButton(R.string.main_dialog_logout_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);

                        finish();
                    }
                }).create().show();
    }

    @Override
    public void onBackPressed() {

        if (personDrawer.isDrawerOpen(Gravity.LEFT)) {
            personDrawer.closeDrawer(Gravity.LEFT);
        } else {
            if (parsedTime == 0) {
                Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
                parsedTime = System.currentTimeMillis();
            } else {
                int seconds = (int) (System.currentTimeMillis() - parsedTime);
                if (seconds > 2000) {
                    Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
                    parsedTime = 0;
                } else {
                    super.onBackPressed();
                    finish();
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        LoginManager.getInstance().SignOut();
    }

    private class MakeTimeLineAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            // Data Load
            if (UserSession.FacebookToken != null) {
                Log.e(TAG, "Start Facebook Async");

                // TODO : Make Time Line Loader
                DataLoader loader = new FacebookLoader();
                loader.LoadTimeLineData(new DataLoadCompleteCallback() {
                    @Override
                    public void Complete(boolean isSuccess, JSONObject result) {
                        if (isSuccess) {
                            SNSParser snsParser = new FacebookParser();
                            List<TimeLineItem> facebookTimeline = snsParser.parseItem((JSONObject) result);

                            if (!facebookTimeline.isEmpty())
                                contents.addAll(facebookTimeline);
                        }
                    }
                });
            } else
                Log.e(TAG, "Facebook Token is Null");

            if (UserSession.TwitterToken != null) {
                Log.e(TAG, "Start Twitter Async");

                DataLoader loader = new TwitterLoader(mContext);
                loader.LoadTimeLineData(new DataLoadCompleteCallback() {
                    @Override
                    public void Complete(boolean isSuccess, JSONObject result) {
                        if (isSuccess) {
                            SNSParser snsParser = new TwitterParser();
                            List<TimeLineItem> twitterTimeline = snsParser.parseItem(result);

                            if (!twitterTimeline.isEmpty())
                                contents.addAll(twitterTimeline);
                        }
                    }
                });
            } else
                Log.e(TAG, "Twitter Session is Null");

            Log.e("Main", "Contents Size : " + contents.size());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            timelineAdapater.notifyDataSetChanged();
        }
    }
}
