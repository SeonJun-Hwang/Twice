package sysproj.seonjoon.twice.view;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import java.util.ArrayList;

import sysproj.seonjoon.twice.DataLoadCompleteCallback;
import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.Post;
import sysproj.seonjoon.twice.loader.DataLoader;
import sysproj.seonjoon.twice.loader.FacebookLoader;
import sysproj.seonjoon.twice.loader.TwitterLoader;
import sysproj.seonjoon.twice.parser.FacebookParser;
import sysproj.seonjoon.twice.parser.SNSParser;
import sysproj.seonjoon.twice.parser.TwitterParser;
import sysproj.seonjoon.twice.staticdata.UserSession;

public class FragmentHome extends Fragment{

    private Context mContext;
    private static final String TAG = "FRAGMENT_HOME";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager recyclerLayoutManager;
    private TimelineRecyclerAdapter timelineAdapater;
    private ArrayList<Post> contents;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mContext = getContext();
        contents = new ArrayList<>();

        // Recycler Set
        recyclerView = (RecyclerView) root.findViewById(R.id.main_time_line_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(recyclerLayoutManager);
        timelineAdapater = new TimelineRecyclerAdapter(mContext, contents);
        recyclerView.setAdapter(timelineAdapater);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        setListener();

        MakeTimeLineAsync makeTimeLineAsync = new MakeTimeLineAsync();
        makeTimeLineAsync.execute();

        return root;
    }

    private void setListener() {
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
