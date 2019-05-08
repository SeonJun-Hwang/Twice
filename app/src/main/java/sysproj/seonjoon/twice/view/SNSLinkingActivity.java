package sysproj.seonjoon.twice.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;
import java.util.Map;

import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.UserInformation;
import sysproj.seonjoon.twice.staticdata.SNSPermission;
import sysproj.seonjoon.twice.staticdata.SNSTag;
import sysproj.seonjoon.twice.staticdata.UserSession;


public class SNSLinkingActivity extends Activity {

    private static final String TAG = "SNSFrag";
    private static final int userAddReq = 10001;

    private TextView headText;
    private Button completeButton;
    private TwitterLoginButton twitterLoginButton;
    private LoginButton facebookLoginButton;
    private CallbackManager facebookCallback;

    private Button facebookinfobtn; // facebook 개인정보 동의 확인서를 보여주기 위한 버튼
    private TextView facebookinfo; // facebook 개인정보 동의 확인서 내용 text
    private Button facebookAgreeBtn; //facebook 개인정보 동의버튼
    private int facebookinfoison = 0;

    private Button twitterinfobtn;
    private TextView twitterinfo;
    private Button twitterAgreeBtn;
    private int twitterinfoison = 0;

    private TextView instargraminfo;
    private Button instargraminfobtn;
    private Button instargramAgreeBtn;
    private int instargraminfoison = 0;

    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sns_regist);

        headText = (TextView) findViewById(R.id.sns_form_head_text);
        completeButton = (Button) findViewById(R.id.sns_form_complete);
        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.sns_form_twitter);
        facebookLoginButton = (LoginButton) findViewById(R.id.sns_form_facebook);

        headText.setText(getString(R.string.sns_form_head));
        completeButton.setText(getString(R.string.sns_form_complete));

        facebookinfobtn = (Button)findViewById(R.id.facebookinfobtn);
        facebookinfo = (TextView)findViewById(R.id.facebookinfo);
        facebookAgreeBtn = (Button)findViewById(R.id.facebook_Auth_AgreeBtn);
        /*instargraminfobtn = (Button)findViewById(R.id.instargraminfobtn);
        instargraminfo = (TextView)findViewById(R.id.instargraminfo);
        instargramAgreeBtn = (Button)findViewById(R.id.instargram_Auth_AgreeBtn);*/
        twitterinfobtn = (Button)findViewById(R.id.twitterinfobtn);
        twitterinfo = (TextView)findViewById(R.id.twitterinfo);
        twitterAgreeBtn = (Button)findViewById(R.id.twitter_Auth_AgreeBtn);

        setAuthText();
        setButtonState();
        setCallBack();
        setListener();
    }

    private void setAuthText(){
        new facebookNetworkThread().execute();
        new twitterNetworkThread().execute();
    }

    private class facebookNetworkThread extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... voids) {
            URL facebookauthURL = null;
            HttpURLConnection fconnection = null;
            StringBuilder sb = new StringBuilder();
            try {
                facebookauthURL = new URL("http://100.24.24.64:3366/facebook");

                fconnection = (HttpURLConnection)facebookauthURL.openConnection();
                fconnection.setRequestMethod("GET");
                //fconnection.setDoOutput(true);

                //fconnection.setReadTimeout(3000);
                //fconnection.setConnectTimeout(3000);
                //fconnection.setDoInput(true);
                fconnection.connect();

                //Log.e(TAG, fconnection.getResponseCode() + " - " + fconnection.getURL().toString());

                InputStream is = fconnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                String result;
                while((result = br.readLine())!=null){
                    sb.append(result+"\n");
                }
                Log.e("hello",sb.toString());
            } catch (java.io.IOException e) {
                Log.e("helloworld",e.toString());
            }finally {
                fconnection.disconnect();
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s != null)
                facebookinfo.setText(s);
            else
                Log.e("null error","null error");
        }
    }

    private class twitterNetworkThread extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... voids) {
            URL tauthURL = null;
            HttpURLConnection tconnection = null;
            StringBuilder sb = new StringBuilder();
            try {
                tauthURL = new URL("http://100.24.24.64:3366/twitter");

                tconnection = (HttpURLConnection)tauthURL.openConnection();
                tconnection.setRequestMethod("GET");
                //fconnection.setDoOutput(true);

                //fconnection.setReadTimeout(3000);
                //fconnection.setConnectTimeout(3000);
                //fconnection.setDoInput(true);
                tconnection.connect();

                //Log.e(TAG, fconnection.getResponseCode() + " - " + fconnection.getURL().toString());

                InputStream is = tconnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                String result;
                while((result = br.readLine())!=null){
                    sb.append(result+"\n");
                }
                Log.e("t hello",sb.toString());
            } catch (java.io.IOException e) {
                Log.e("t helloworld",e.toString());
            }finally {
                tconnection.disconnect();
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s != null)
                twitterinfo.setText(s);
            else
                Log.e("t null error","null error");
        }
    }
    //http://100.24.24.64:3366/twitter

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        Log.e(TAG, "Request - " + requestCode);
//
//        if (requestCode == userAddReq) {
//            if (requestCode == Activity.RESULT_OK)
//                Toast.makeText(this, "가입 완료 되었습니다.", Toast.LENGTH_SHORT).show();
//            else
//                Toast.makeText(this, "가입에 실패했습니다.", Toast.LENGTH_SHORT).show();
//
//            boolean facebookRes = data.getBooleanExtra(SNSTag.FacebookResultTag, false);
//            boolean twitterRes = data.getBooleanExtra(SNSTag.TwitterResultTag, false);
//
//            if (!facebookRes || !twitterRes )
//            {
//                String fbMessage = "";
//                String twMessage = "";
//
//                if (!facebookRes)
//                    fbMessage = "Facebook ";
//
//                if (!twitterRes)
//                    twMessage = "Twitter ";
//
//                Snackbar.make(fbMessage + twMessage + "연동에 실패했습니다.", Snackbar.LENGTH_LONG).show();
//            }
//
//        } else {
//            facebookCallback.onActivityResult(requestCode, resultCode, data);
//            twitterLoginButton.onActivityResult(requestCode, resultCode, data);
//        }
//
//        Log.e(TAG, "Activity Result " + requestCode);
    }

    private void setButtonState(){
        facebookLoginButton.setEnabled(false);
        facebookAgreeBtn.setEnabled(true);
    }
    private void setListener() {

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                builder.setMessage("해당 정보로 가입 하시겠습니까?").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // For Additional Register
                        LoginManager.getInstance().logOut();

                    }
                }).setNegativeButton("취소", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        //facebook info button을 눌렀을때 개인정보 동의 확인서가 뜨게 하는것
        facebookinfobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (facebookinfoison == 0) {
                    facebookinfo.setVisibility(View.VISIBLE);
                    facebookLoginButton.setVisibility(View.VISIBLE);
                    facebookAgreeBtn.setVisibility(View.VISIBLE);
                    facebookinfoison = 1;
                }else{
                    facebookinfo.setVisibility(View.GONE);
                    facebookLoginButton.setVisibility(View.GONE);
                    facebookAgreeBtn.setVisibility(View.GONE);
                    facebookinfoison = 0;
                }
            }
        });
        //동의시 로그인 버튼 활성화
        facebookAgreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facebookLoginButton.setEnabled(true);
                facebookAgreeBtn.setEnabled(false);
            }
        });

        //twitter
        twitterinfobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (twitterinfoison == 0) {
                    twitterinfo.setVisibility(View.VISIBLE);
                    twitterLoginButton.setVisibility(View.VISIBLE);
                    twitterAgreeBtn.setVisibility(View.VISIBLE);
                    twitterinfoison = 1;
                }else{
                    twitterinfo.setVisibility(View.GONE);
                    twitterLoginButton.setVisibility(View.GONE);
                    twitterAgreeBtn.setVisibility(View.GONE);
                    twitterinfoison = 0;
                }
            }
        });
        twitterAgreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                twitterLoginButton.setEnabled(true);
                twitterAgreeBtn.setEnabled(false);
            }
        });
    }

    private void setCallBack() {
        facebookCallback = CallbackManager.Factory.create();
        facebookLoginButton.setReadPermissions(SNSPermission.getFacebookPermission());

        LoginManager.getInstance().registerCallback(facebookCallback, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                facebookLoginButton.setEnabled(false);
                UserSession.FacebookToken = loginResult.getAccessToken();

                // Log.e(TAG, "FB Token : " + UserSession.facebookToken.getToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(mContext, "취소하셨습니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, error.toString());
                Toast.makeText(mContext, "네트워크 에러입니다. 잠시 후에 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                twitterLoginButton.setEnabled(false);

                Log.e(TAG, "Success Twitter Login");

                UserSession.TwitterToken = result.data;
            }

            @Override
            public void failure(TwitterException exception) {
                Log.e(TAG, exception.toString());
            }
        });
    }

}
