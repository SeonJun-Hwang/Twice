package sysproj.seonjoon.twice.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.DBAccessResultCallback;
import sysproj.seonjoon.twice.manager.DBManager;
import sysproj.seonjoon.twice.staticdata.SNSTag;
import sysproj.seonjoon.twice.staticdata.UserSession;

public class UserAddActivity extends Activity {

    private static final String TAG = "USER_ADD";
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add);

        mContext = this;

        Intent intent = getIntent();
        String email = intent.getStringExtra("email") + SNSTag.TWICE_EMAIL_TAIL;
        String password = intent.getStringExtra("password");

        DBManager.getInstance().createUser(this, email, password, new DBAccessResultCallback() {
            @Override
            public void AccessCallback(boolean isSuccess) {
                // Link SNS Data
                new LinkSNSAsync().execute();
            }
        });
    }

    private void thisActivityFinish(boolean... result){
        Intent resultIntent = new Intent();

        resultIntent.putExtra(SNSTag.FacebookResultTag, result[0]);
        resultIntent.putExtra(SNSTag.TwitterResultTag, result[1]);
        setResult(Activity.RESULT_OK, resultIntent);

        FirebaseAuth.getInstance().signOut();
        finish();
    }

    private class LinkSNSAsync extends AsyncTask<Void, Void, Void> {

        private static final String TAG = "LinkSNSAsync";

        private boolean locking;
        private boolean status[] = new boolean[]{ true, true, true };

        private String Set2String(Set<String> input)
        {
            String result = new String();

            for(String item : input)
                result += item + '\t';
            return result;
        }

        @Override
        protected Void doInBackground(Void... strings) {
            Log.e(TAG, "Start Link SNS");

            locking = true;

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            // Busy Lock
            while (user == null);

            Log.e(TAG, user.getUid());

            // TODO : Facebook Registration
            Log.e(TAG, "Start Facebook Link ");
            if (UserSession.FacebookToken != null)
            {
                Map<String, Object> data = new HashMap<>();

                data.put(SNSTag.FacebookUIDTag, UserSession.FacebookToken.getUserId());
                data.put(SNSTag.FacebookTokenTag, UserSession.FacebookToken.getToken());
                data.put(SNSTag.FacebookAIDTag, UserSession.FacebookToken.getApplicationId());
                data.put(SNSTag.FacebookPermissionTag, Set2String(UserSession.FacebookToken.getPermissions()));
                data.put(SNSTag.FacebookDPermissionTag, Set2String(UserSession.FacebookToken.getDeclinedPermissions()));
                data.put(SNSTag.FacebookTokenSourceTag, UserSession.FacebookToken.getSource().toString());
                data.put(SNSTag.FacebookExpTimeTag, UserSession.FacebookToken.getExpires().toString());
                data.put(SNSTag.FacebookLastTimeTag, UserSession.FacebookToken.getLastRefresh().toString());
                data.put(SNSTag.FacebookDataAccExpTimeTag, UserSession.FacebookToken.getDataAccessExpirationTime().toString());

//                DBManager.getInstance()
//                        .addDB(user.getUid(), SNSTag.FacebookDocTag ,data, new DBAccessResultCallback() {
//                            @Override
//                            public void AccessCallback(boolean result) {
//                                Log.e(TAG, "Facebook Result : " + result);
//                                status[0] = result;
//                                locking = false;
//                            }
//                        });
            }
            else
                locking = false;

            while(locking);

            Log.e(TAG, "Start Twitter Link ");
            locking =true;

            // TODO : Twitter Registraion
            if (UserSession.TwitterToken != null)
            {
                Map<String, Object> data = new HashMap<>();

                data.put(SNSTag.TwitterTokenTag, UserSession.TwitterToken.getAuthToken().token);
                data.put(SNSTag.TwitterTokenSecretTag, UserSession.TwitterToken.getAuthToken().secret);
                data.put(SNSTag.TwitterUNameTag, UserSession.TwitterToken.getUserName());
                data.put(SNSTag.TwitterUIDTag, UserSession.TwitterToken.getUserId());

//                DBManager.getInstance()
//                        .addDB(user.getUid(), SNSTag.TwitterDocTag, data, new DBAccessResultCallback() {
//                            @Override
//                            public void AccessCallback(boolean isSuccess) {
//                                Log.e(TAG, "Twitter Result" + isSuccess);
//                                status[1] = isSuccess;
//                                locking = false;
//                            }
//                        });
            }
            else
                locking= false;

            while(locking);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            thisActivityFinish(status);
        }
    }
}
