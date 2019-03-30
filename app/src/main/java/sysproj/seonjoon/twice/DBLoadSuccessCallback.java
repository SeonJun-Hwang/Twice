package sysproj.seonjoon.twice;

import java.util.Map;

public interface DBLoadSuccessCallback {

    void LoadDataCallback(boolean isSuccess, Map<String, Object> res);
}
