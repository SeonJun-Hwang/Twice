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

    public void FacebookLogin(String uid, DBLoadSuccessCallback callback) {
        Log.e(TAG, "Facebook Login Start");

        DBManager.getInstance().getDB(uid, SNSTag.FacebookDocTag, callback);

        if (loginResult)
            Log.e(TAG, "Facebook Login Success");
        else
            Log.e(TAG, "Facebook Login Failure");
    }

    public void TwitterLogin(String uid, DBLoadSuccessCallback callback) {
        Log.e(TAG, "Twitter Login Start");

        DBManager.getInstance().getDB(uid, SNSTag.TwitterDocTag, callback);
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
