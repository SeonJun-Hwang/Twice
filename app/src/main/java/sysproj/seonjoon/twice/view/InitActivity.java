package sysproj.seonjoon.twice.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

import java.util.Map;

import sysproj.seonjoon.twice.DBAccessResultCallback;
import sysproj.seonjoon.twice.manager.DBManager;
import sysproj.seonjoon.twice.manager.LoginManager;
import sysproj.seonjoon.twice.manager.PreferenceManager;
import sysproj.seonjoon.twice.parser.FacebookTokenParser;
import sysproj.seonjoon.twice.parser.TokenParser;
import sysproj.seonjoon.twice.staticdata.SNSTag;
import sysproj.seonjoon.twice.staticdata.StaticAppData;
import sysproj.seonjoon.twice.staticdata.UserSession;

public class InitActivity extends AppCompatActivity {

    private final static String TAG = "InitActivity";

    private Context mContext;
    private Intent intent = null;
    private String uID;
    private String uPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Context N Key
        mContext = this;

        loadSharedPreference();
        twitterInitialize();
        facebookInitialize();
        checkPermissions();

        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, "Server Connect", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    @Override
    public void onBackPressed() {
        // To Prevent finish application by back-button
    }

    private void loadSharedPreference() {
        uID = PreferenceManager.getInstance().getString(this, UserSession.UserFileName, UserSession.UserIDTag);
        uPassword = PreferenceManager.getInstance().getString(this, UserSession.UserFileName, UserSession.UserPasswordTag);
    }

    private void facebookInitialize() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    private void twitterInitialize() {
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(UserSession.TwitterAPI, UserSession.TwitterAPISecret))
                .debug(true)
                .build();
        Twitter.initialize(config);

    }

    @Override
    protected void onStart() {
        super.onStart();

        intent = new Intent(InitActivity.this, LoginActivity.class);

        // Check Auto Login
        if (!uID.isEmpty() && !uPassword.isEmpty()) {

            LoginManager.getInstance().TwiceLogin(this, uID, uPassword);

            DBManager.getInstance().loginUser(this, uID, uPassword, new DBAccessResultCallback() {
                @Override
                public void AccessCallback(boolean isSuccess) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user != null) {
                        intent = new Intent(InitActivity.this, MainActivity.class);
                        Map<String, Object> loadData = DBManager.getInstance().getDB(user.getUid(),SNSTag.FacebookDocTag);

                        TokenParser parser = new FacebookTokenParser();
                        UserSession.FacebookToken = (AccessToken) parser.map2Token(loadData);

                        Log.e(TAG, user.getUid());
                    }
                }
            });
        }
        else
        {
            if (!uID.isEmpty())
                intent.putExtra(UserSession.UserIDTag, uID);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case StaticAppData.PERMISSION_WRITE_CONTACT:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Init", "Write Contact Permission Granted");
                }
                break;
            case StaticAppData.PERMISSION_READ_CONTACT:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Init", "Read Contact Permission Granted");
                }
                break;
        }
    }

    private void checkPermissions() {
        // Permission List
        String[] permissionList = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS};
        int[] permissionCodeList = {StaticAppData.PERMISSION_READ_CONTACT, StaticAppData.PERMISSION_WRITE_CONTACT};

        // Check Permissions
        for (int i = 0; i < permissionCodeList.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissionList[i]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{permissionList[i]}, permissionCodeList[i]);
            }
        }
    }
}
