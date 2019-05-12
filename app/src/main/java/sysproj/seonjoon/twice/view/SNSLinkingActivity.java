package sysproj.seonjoon.twice.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DialogTitle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import sysproj.seonjoon.twice.DBAccessResultCallback;
import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.manager.DBManager;
import sysproj.seonjoon.twice.staticdata.SNSPermission;
import sysproj.seonjoon.twice.staticdata.SNSTag;
import sysproj.seonjoon.twice.staticdata.UserSession;

public class SNSLinkingActivity extends AppCompatActivity implements CompoundButton.OnClickListener {

    private static final String TAG = "SNSFrag";
    private static final int userAddReq = 10001;

    private Context mContext;

    private Switch facebookSwitch;
    private Switch twitterSwitch;
    private Switch instagramSwitch;
    private CallbackManager facebookCallback;

    private FacebookAsync facebookAuth;
    private TwitterAsync twitterAuth;
    private InstagramAsync instagramAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_linking_activity);

        mContext = this;

        facebookSwitch = (Switch) findViewById(R.id.link_facebook_switch);
        twitterSwitch = (Switch) findViewById(R.id.link_twitter_switch);
        instagramSwitch = (Switch) findViewById(R.id.link_instagram_switch);

        if (UserSession.FacebookToken != null) {
            facebookSwitch.setChecked(true);
        }

        facebookSwitch.setOnClickListener(this);
        twitterSwitch.setOnClickListener(this);
        instagramSwitch.setOnClickListener(this);

        if (UserSession.TwitterToken != null) {
            twitterSwitch.setChecked(true);
        }

        facebookCallback = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(facebookCallback, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        setListener();
        setActionBar();
    }

    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setBackgroundDrawable(new ColorDrawable(getColor(R.color.timelineHeadBack)));// #464A4F
        }
    }

    @Override
    public void onClick(View view) {

        int resId = view.getId();
        final CompoundButton compoundButton = (CompoundButton) view;
        final boolean checkStatus = !compoundButton.isChecked();

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        compoundButton.setChecked(!checkStatus);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        compoundButton.setChecked(checkStatus);
                    }
                }).create();

        switch (resId) {
            case R.id.link_facebook_switch:
                if (checkStatus) {
                    dialog.setMessage("Facebook 연동 및 사용 동의가 해제됩니다.\n정말로 해제하시겠습니까?");
                    dialog.show();
                } else {
                    if (facebookAuth == null) {
                        facebookAuth = new FacebookAsync();
                        facebookAuth.execute();
                    }
                }
                break;
            case R.id.link_twitter_switch:
                if (checkStatus) {
                    dialog.setMessage("Twitter 연동 및 사용 동의가 해제됩니다.\n정말로 해제하시겠습니까?");
                    dialog.show();
                } else {
                    if (twitterAuth == null) {
                        twitterAuth = new TwitterAsync();
                        twitterAuth.execute();
                    }
                }
                break;
            case R.id.link_instagram_switch:
                if (checkStatus) {
                    dialog.setMessage("Instagram 연동 및 사용 동의가 해제됩니다.\n정말로 해제하시겠습니까?");
                    dialog.show();
                } else {
                    instagramAuth = new InstagramAsync();
                    instagramAuth.execute();
                }
                break;
        }

    }

    private class FacebookAsync extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            URL facebookauthURL = null;
            HttpURLConnection fconnection = null;
            StringBuilder sb = new StringBuilder();
            try {
                facebookauthURL = new URL("http://100.24.24.64:3366/facebook");

                fconnection = (HttpURLConnection) facebookauthURL.openConnection();
                fconnection.setRequestMethod("GET");
                fconnection.connect();

                InputStream is = fconnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                String result;
                while ((result = br.readLine()) != null) {
                    sb.append(result).append("\n");
                }
            } catch (java.io.IOException e) {
                Log.e(TAG, "Facebook Async IOException");
            } finally {
                if (fconnection != null)
                    fconnection.disconnect();
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            facebookAuth = null;

            SNSDialog dialog = new SNSDialog(mContext, SNSTag.Facebook);
            dialog.setTitle("Facebook 연동");
            dialog.setIcon(R.drawable.facebook);
            dialog.setMessage(s);
            dialog.show();
        }
    }

    private class TwitterAsync extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            URL tauthURL = null;
            HttpURLConnection tconnection = null;
            StringBuilder sb = new StringBuilder();
            try {
                tauthURL = new URL("http://100.24.24.64:3366/twitter");

                tconnection = (HttpURLConnection) tauthURL.openConnection();
                tconnection.setRequestMethod("GET");
                tconnection.connect();

                InputStream is = tconnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                String result;
                while ((result = br.readLine()) != null) {
                    sb.append(result).append("\n");
                }
                //Log.e("t hello", sb.toString());
            } catch (java.io.IOException e) {
                Log.e("t helloworld", e.toString());
            } finally {
                if (tconnection != null)
                    tconnection.disconnect();
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            twitterAuth = null;

            SNSDialog dialog = new SNSDialog(mContext, SNSTag.Twitter);
            dialog.setTitle("Twitter 연동");
            dialog.setIcon(R.drawable.twitter);
            dialog.setMessage(s);
            dialog.show();
        }
    }
    //http://100.24.24.64:3366/twitter

    private class InstagramAsync extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            URL tauthURL = null;
            HttpURLConnection tconnection = null;
            StringBuilder sb = new StringBuilder();
            try {
                tauthURL = new URL("http://100.24.24.64:3366/instagram");

                tconnection = (HttpURLConnection) tauthURL.openConnection();
                tconnection.setRequestMethod("GET");
                tconnection.connect();

                InputStream is = tconnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                String result;
                while ((result = br.readLine()) != null) {
                    sb.append(result).append("\n");
                }
                //Log.e("t hello", sb.toString());
            } catch (java.io.IOException e) {
                Log.e("t helloworld", e.toString());
            } finally {
                if (tconnection != null)
                    tconnection.disconnect();
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            instagramAuth = null;

            SNSDialog dialog = new SNSDialog(mContext, SNSTag.Instagram);
            dialog.setTitle("Instagram 연동");
            dialog.setIcon(R.drawable.instagram);
            dialog.setMessage(s);
            dialog.show();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        facebookCallback.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setListener() {

    }

    private class SNSDialog extends Dialog {

        private final Context context;
        private DialogTitle title;
        private ImageView icon;
        private TextView message;
        private CheckBox argumentAgree;
        private Button SNSLogin;
        private Button negativeButton;

        int snsTag;

        protected SNSDialog(Context context, int snsTag) {
            super(context, snsTag);
            this.context = context;
            this.snsTag = snsTag;

            View view = View.inflate(context, R.layout.dialog_alert_custom, null);

            title = (DialogTitle) view.findViewById(R.id.alert_title);
            icon = (ImageView) view.findViewById(R.id.alert_title_icon);
            message = (TextView) view.findViewById(R.id.alert_message);
            argumentAgree = (CheckBox) view.findViewById(R.id.alert_agree_check);
            negativeButton = (Button) view.findViewById(R.id.alert_negative);

            setContentView(view);

            setNegativeButton();
            setSNSButton(view, snsTag);
            setCheckbox();
        }

        void setNegativeButton() {
            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (snsTag) {
                        case SNSTag.Facebook:
                            facebookSwitch.setChecked(false);
                            break;
                        case SNSTag.Twitter:
                            twitterSwitch.setChecked(false);
                            break;
                        case SNSTag.Instagram:
                            instagramSwitch.setChecked(false);
                            break;
                    }
                    dismiss();
                    cancel();
                }
            });
        }

        private void setSNSButton(View view, int snsTag) {
            if (snsTag == SNSTag.Facebook) {
                SNSLogin = (LoginButton) view.findViewById(R.id.alert_facebook_login);
                ((LoginButton) SNSLogin).setReadPermissions(SNSPermission.getFacebookPermission());
                ((LoginButton) SNSLogin).registerCallback(facebookCallback, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        UserSession.FacebookToken = loginResult.getAccessToken();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        if (user != null) {
                            DBManager.getInstance().saveFacebookToken(user.getUid(), SNSTag.FacebookDocTag, new DBAccessResultCallback() {
                                @Override
                                public void AccessCallback(boolean isSuccess) {
                                    if (isSuccess)
                                        Toast.makeText(context, "저장되었습니다.", Toast.LENGTH_LONG).show();
                                    else
                                        Toast.makeText(context, "잠시 후에 다시 시도해 주시기 바랍니다.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(context, "Facebook 에러입니다. 잠시후에 다시 시도해주시기 바랍니다.", Toast.LENGTH_LONG).show();
                    }
                });

            }
            else if ( snsTag == SNSTag.Twitter){
                SNSLogin = (TwitterLoginButton) view.findViewById(R.id.alert_twitter_login);

            }
            else if (snsTag == SNSTag.Instagram){
                SNSLogin = (InstagramLoginButton) view.findViewById(R.id.alert_instagram_login);
                SNSLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(mContext, "Instagram Login", Toast.LENGTH_SHORT).show();;
                    }
                });
            }

            SNSLogin.setVisibility(View.VISIBLE);
        }

        private void setCheckbox() {
            argumentAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean changedValue) {
                    if (changedValue)
                        SNSLogin.setEnabled(true);
                    else
                        SNSLogin.setEnabled(false);
                }
            });
        }

        public void setTitle(@Nullable CharSequence title) {
            if (this.title != null)
                this.title.setText(title);
            else
                Log.e(TAG, "Title Is Empty");
        }

        public void setMessage(@Nullable CharSequence message) {
            this.message.setText(message);
        }

        public void setIcon(int iconId) {
            this.icon.setImageResource(iconId);
        }

    }

}
