package sysproj.seonjoon.twice.view;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.Post;

public class FragmentSearch extends Fragment {

    private final static String TAG = "SEARCH_ACTIVITY";

    private Context mContext;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager recyclerLayoutManager;
    private TimelineRecyclerAdapter timelineAdapater;
    private ArrayList<Post> contents;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        contents = new ArrayList<>();
        recyclerView = (RecyclerView) root.findViewById(R.id.main_time_line_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(recyclerLayoutManager);
        timelineAdapater = new TimelineRecyclerAdapter(mContext, contents);
        recyclerView.setAdapter(timelineAdapater);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        return root;
    }

    private void setSearchLayout(View root){

    }

    private class SearchPostAsync extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... strings) {
            String searchTag = strings[0];


            return null;
        }
    }
}
