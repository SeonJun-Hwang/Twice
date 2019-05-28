package sysproj.seonjoon.twice.view.custom.InstagramLogin;

public interface InstagramLoginCallBack {

    void onSuccess(final String token);
    void onCancel(String failMessage);
}
