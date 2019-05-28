package sysproj.seonjoon.twice.loader;

import org.json.JSONObject;

import sysproj.seonjoon.twice.DataLoadCompleteCallback;

public interface DataLoader {

    void LoadUserProfileData(DataLoadCompleteCallback callback);
    JSONObject LoadUserProfileData();
    void LoadTimeLineData(DataLoadCompleteCallback callback);
    JSONObject LoadTimeLineData();
    void LoadTimeLineData(DataLoadCompleteCallback callback, long maxId);
    void LoadSearchData(String searchTag, DataLoadCompleteCallback callback);
}
