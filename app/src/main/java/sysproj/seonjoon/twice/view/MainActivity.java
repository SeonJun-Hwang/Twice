package sysproj.seonjoon.twice.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sysproj.seonjoon.twice.DataLoadCompleteCallback;
import sysproj.seonjoon.twice.loader.DataLoader;
import sysproj.seonjoon.twice.loader.FacebookLoader;
import sysproj.seonjoon.twice.loader.TwitterLoader;
import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.Post;
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
    private ArrayList<Post> contents;

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
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

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

        private ArrayList<Post> facebookTimeline;
        private ArrayList<Post> twitterTimeline;

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
                            facebookTimeline = snsParser.parseItem((JSONObject) result);
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
                            twitterTimeline = snsParser.parseItem(result);
                        }
                    }
                });
            } else
                Log.e(TAG, "Twitter Session is Null");

            contents.addAll(mergePost(facebookTimeline, twitterTimeline));

            Log.e("Main", "Contents Size : " + contents.size());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            timelineAdapater.notifyDataSetChanged();
        }

        private ArrayList<Post> mergePost(ArrayList<Post> src1, ArrayList<Post> src2) {

            if (src1 == null && src2 == null)
                return null;
            else if (src1 == null)
                return src2;
            else if (src2 == null)
                return src1;

            ArrayList<Post> result = new ArrayList<>();
            int leftPivot = 0, rightPivot = 0;
            int leftLength = src1.size(), rightLength = src2.size();

            while (leftPivot < leftLength && rightPivot < rightLength) {
                Post left = src1.get(leftPivot);
                Post right = src2.get(rightPivot);

                if (left.getCreateDate().compareTo(right.getCreateDate()) > 0) {
                    result.add(left);
                    leftPivot++;
                } else {
                    result.add(right);
                    rightPivot++;
                }
            }

            while (leftPivot < leftLength)
                result.add(src1.get(leftPivot++));

            while (rightPivot < rightLength)
                result.add(src2.get(rightPivot++));

            return result;
        }
    }
}
