package sysproj.seonjoon.twice.loader;

import sysproj.seonjoon.twice.DataLoadCompleteCallback;

public interface DataLoader {

    void LoadUserProfileData(DataLoadCompleteCallback callback);
    void LoadTimeLineData(DataLoadCompleteCallback callback);
    void LoadTimeLineData(DataLoadCompleteCallback callback, long maxId);
    void LoadSearchData(String searchTag, DataLoadCompleteCallback callback);
}
