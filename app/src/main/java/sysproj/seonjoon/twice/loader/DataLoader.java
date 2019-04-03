package sysproj.seonjoon.twice.loader;

import sysproj.seonjoon.twice.DataLoadCompleteCallback;

public interface DataLoader {

    void LoadTimeLineData(DataLoadCompleteCallback callbacks);
}
