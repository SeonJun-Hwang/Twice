package sysproj.seonjoon.twice.view;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import sysproj.seonjoon.twice.staticdata.LastUpdate;
import sysproj.seonjoon.twice.staticdata.UserSession;

public class FragmentSearch extends Fragment {

    private final static String TAG = "SEARCH_ACTIVITY";

    private Context mContext;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager recyclerLayoutManager;
    private TimelineRecyclerAdapter timelineAdapter;
    private ArrayList<Post> contents;
    private SearchAsync searchAsync;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        contents = new ArrayList<>();
        mContext = getContext();

        setSearchLayout(root);

        return root;
    }

    private void setSearchLayout(View root) {
        recyclerView = (RecyclerView) root.findViewById(R.id.search_search_view);
        recyclerView.setHasFixedSize(true);
        recyclerLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(recyclerLayoutManager);
        timelineAdapter = new TimelineRecyclerAdapter(mContext, contents);
        recyclerView.setAdapter(timelineAdapter);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
    }

    public void startSearch(String search) {
        searchAsync = new SearchAsync();
        searchAsync.execute(search);
    }

    @Override
    public void onStop() {
        searchAsync.cancel(false);
        searchAsync = null;

        super.onStop();
    }

    private class SearchAsync extends AsyncTask<String, Void, Void> {

        private ArrayList<Post> facebookTimeline;
        private ArrayList<Post> twitterTimeline;

        @Override
        protected void onCancelled() {
            contents = null;
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LastUpdate.updateTime(MainActivity.getShowingFragmentNumber());
        }

        @Override
        protected Void doInBackground(String... strings) {
            String searchTag = strings[0];

            // Data Load
            if (UserSession.FacebookToken != null) {
                Log.e(TAG, "Start Facebook Async");

                // TODO : Make Time Line Loader
                DataLoader loader = new FacebookLoader(mContext);
                loader.LoadSearchData(searchTag, new DataLoadCompleteCallback() {
                    @Override
                    public void Complete(boolean isSuccess, JSONObject result) {
                        if (isSuccess) {
                            // TODO : Create Facebook Search Calback
                            SNSParser parser = new FacebookParser();
                            facebookTimeline = parser.parseSearch(result);
                        }
                    }
                });
            } else
                Log.e(TAG, "Facebook Token is Null");

            if (UserSession.TwitterToken != null) {
                Log.e(TAG, "Start Twitter Async");

                DataLoader loader = new TwitterLoader(mContext);
                loader.LoadSearchData(searchTag, new DataLoadCompleteCallback() {
                    @Override
                    public void Complete(boolean isSuccess, JSONObject result) {
                        if (isSuccess) {
                            SNSParser snsParser = new TwitterParser();
                            twitterTimeline = snsParser.parseSearch(result);
                        }
                    }
                });
            } else
                Log.e(TAG, "Twitter Session is Null");

            Log.e(TAG, "FB : " + (facebookTimeline == null ? "NULL" : "Exist") + " / " + "TW : " + (twitterTimeline == null ? "NULL" : "Exist"));

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            contents.addAll(Post.mergePost(facebookTimeline, twitterTimeline));
            Log.e("Main", "Contents Size : " + contents.size());

            timelineAdapter.notifyDataSetChanged();
        }
    }
}
