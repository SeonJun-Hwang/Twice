package sysproj.seonjoon.twice.manager;

import android.app.Activity;
import android.util.Log;

import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseAuth;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

import sysproj.seonjoon.twice.DBAccessResultCallback;
import sysproj.seonjoon.twice.DBLoadSuccessCallback;
import sysproj.seonjoon.twice.parser.FacebookTokenParser;
import sysproj.seonjoon.twice.parser.TokenParser;
import sysproj.seonjoon.twice.parser.TwitterTokenParser;
import sysproj.seonjoon.twice.staticdata.SNSTag;
import sysproj.seonjoon.twice.staticdata.UserSession;

public class LoginManager {

    private static final String TAG = "LoginManager";

    private static LoginManager instance = null;
    private boolean loginResult;

    private LoginManager() {
    }

    public boolean FacebookLogin(String uid) {
        Log.e(TAG, "Facebook Login Start");

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        DBManager.getInstance().getDB(uid, SNSTag.FacebookDocTag, new DBLoadSuccessCallback() {
            @Override
            public void LoadDataCallback(boolean isSuccess, Map<String, Object> result) {
                if (isSuccess) {
                    TokenParser tokenParser = new FacebookTokenParser();
                    UserSession.FacebookToken = (AccessToken) tokenParser.map2Token(result);
                }
                loginResult = isSuccess;
                countDownLatch.countDown();
            }
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (loginResult)
            Log.e(TAG, "Facebook Login Success");
        else
            Log.e(TAG, "Facebook Login Failure");
        return true;
    }

    public boolean TwitterLogin(String uid) {
        Log.e(TAG, "Twitter Login Start");

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        DBManager.getInstance().getDB(uid, SNSTag.TwitterDocTag, new DBLoadSuccessCallback() {
            @Override
            public void LoadDataCallback(boolean isSuccess, Map<String, Object> result) {
                if (isSuccess) {
                    TokenParser tokenParser = new TwitterTokenParser();
                    UserSession.TwitterToken = (TwitterSession) tokenParser.map2Token(result);

                    TwitterCore.getInstance().getSessionManager().setActiveSession(UserSession.TwitterToken);
                }
                loginResult = isSuccess;
                countDownLatch.countDown();
            }
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean TwiceLogin(Activity activity, String id, String password) {
        loginResult = false;

        DBManager.getInstance().loginUser(activity, id, password, new DBAccessResultCallback() {
            @Override
            public void AccessCallback(boolean isSuccess) {
                loginResult = isSuccess;
            }
        });

        return loginResult;
    }

    public void SignOut(){
        FirebaseAuth.getInstance().signOut();
        TwitterCore.getInstance().getSessionManager().clearActiveSession();
        AccessToken.setCurrentAccessToken(null);
    }

    public static LoginManager getInstance() {
        if (instance == null)
            instance = new LoginManager();

        return instance;
    }
}
