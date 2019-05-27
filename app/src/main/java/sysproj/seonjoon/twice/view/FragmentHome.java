package sysproj.seonjoon.twice.view;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import sysproj.seonjoon.twice.DataLoadCompleteCallback;
import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.FacebookPageVO;
import sysproj.seonjoon.twice.entity.Post;
import sysproj.seonjoon.twice.loader.DataLoader;
import sysproj.seonjoon.twice.loader.FacebookLoader;
import sysproj.seonjoon.twice.loader.InstagramLoader;
import sysproj.seonjoon.twice.loader.TwitterLoader;
import sysproj.seonjoon.twice.parser.FacebookParser;
import sysproj.seonjoon.twice.parser.InstagramParser;
import sysproj.seonjoon.twice.parser.SNSParser;
import sysproj.seonjoon.twice.parser.TwitterParser;
import sysproj.seonjoon.twice.staticdata.LastUpdate;
import sysproj.seonjoon.twice.staticdata.UserSession;
import sysproj.seonjoon.twice.view.custom.TimelineRecyclerAdapter;

public class FragmentHome extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private Context mContext;
    private static final String TAG = "FRAGMENT_HOME";
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView.LayoutManager recyclerLayoutManager;
    private TimelineRecyclerAdapter timelineAdapter;
    private ArrayList<Post> contents;
    private MakeTimeLineAsync makeTimeLineAsync;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mContext = getContext();
        contents = new ArrayList<>();

        // Recycler Set
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.main_time_line_swipe_layout);
        recyclerView = (RecyclerView) root.findViewById(R.id.main_time_line_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(recyclerLayoutManager);
        timelineAdapter = new TimelineRecyclerAdapter(mContext, contents);
        recyclerView.setAdapter(timelineAdapter);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        setListener();
        refreshTimeline();

        return root;
    }

    private void setListener() {

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.swipe_colors));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerLayoutManager;
            }
        });

    }

    @Override
    public void onRefresh() {
        if (makeTimeLineAsync != null) {
            Toast.makeText(getContext(), "Refreshing, Please Wait ", Toast.LENGTH_SHORT).show();
        }
        else
            refreshTimeline();
    }

    private void refreshTimeline() {
        makeTimeLineAsync = new MakeTimeLineAsync();
        makeTimeLineAsync.execute();
    }

    private class MakeTimeLineAsync extends AsyncTask<Void, Void, Void> {

        private ArrayList<Post> facebookTimeline;
        private ArrayList<Post> twitterTimeline;
        private ArrayList<Post> instagramTimeline;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LastUpdate.updateTime(MainActivity.getShowingFragmentNumber());
        }

        @Override
        protected Void doInBackground(Void... voids) {

            if (UserSession.TwitterToken != null) {
                Log.e(TAG, "Start Twitter Async");

                DataLoader loader = new TwitterLoader(mContext);
                loader.LoadTimeLineData(new DataLoadCompleteCallback() {
                    @Override
                    public void Complete(boolean isSuccess, JSONObject result) {
                        if (isSuccess) {
                            SNSParser snsParser = new TwitterParser();
                            twitterTimeline = snsParser.parseTimeline(result);
                        }
                    }
                });
            } else
                Log.e(TAG, "Twitter Session is Null");

            if (UserSession.FacebookToken != null) {
                Log.e(TAG, "Start Facebook Async");

                // TODO : Make Time Line Loader
                FacebookLoader loader = new FacebookLoader(mContext);
                JSONObject jsonObject = loader.LoadTimeLineData();

                FacebookParser snsParser = new FacebookParser();
                facebookTimeline = snsParser.parseTimeline(jsonObject);

                Log.e(TAG, "Page Size : " + UserSession.FacebookPageProfile.size());

                if (UserSession.FacebookPageProfile.size() > 0){
                    for (int i = 0; i < UserSession.FacebookPageProfile.size(); i++){
                        FacebookPageVO curVO = UserSession.FacebookPageProfile.get(i);
                        JSONObject object = loader.LoadPageTimeLineData(curVO);

                        ArrayList<Post> parsedTimeLine = snsParser.parseTimeline(object, curVO);

                        if (parsedTimeLine != null)
                            facebookTimeline.addAll(parsedTimeLine);
                    }
                }
            } else
                Log.e(TAG, "Facebook Token is Null");

            if (UserSession.InstagramToken != null){
                Log.e(TAG, "Start Instagram Async");

                // TODO : Instagram Timeline Making
                DataLoader loader = new InstagramLoader();
                JSONObject jsonObject = loader.LoadTimeLineData();

                SNSParser snsParser = new InstagramParser();
                instagramTimeline = snsParser.parseTimeline(jsonObject);
            } else
                Log.e(TAG, "Instagram Token is Null");

            Log.e("Main", "Contents Size : " + contents.size());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            makeTimeLineAsync = null;
            if ( swipeRefreshLayout.isRefreshing() )
                swipeRefreshLayout.setRefreshing(false);

            if (!contents.isEmpty()) {
                contents.clear();
                timelineAdapter.notifyDataSetChanged();
            }

            ArrayList<Post> faceInsta = Post.mergePost(facebookTimeline, twitterTimeline);

            contents.addAll(Post.mergePost(faceInsta, instagramTimeline));

            timelineAdapter.notifyDataSetChanged();
        }
    }
}
