package sysproj.seonjoon.twice;

import java.util.ArrayList;

import sysproj.seonjoon.twice.entity.Post;

public interface TimelineDataLoadCallback {

    void DataLoadCallback(ArrayList<Post> result);
}
