package sysproj.seonjoon.twice.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.staticdata.UserSession;

public class SNSLinkingActivity extends AppCompatActivity implements CompoundButton.OnClickListener {

    private static final String TAG = "SNSFrag";
    private static final int userAddReq = 10001;

    private Context mContext;

    private Switch facebookSwitch;
    private Switch twitterSwitch;
    private Switch instagramSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_linking_activity);

        mContext = this;

        facebookSwitch = (Switch) findViewById(R.id.link_facebook_switch);
        twitterSwitch = (Switch) findViewById(R.id.link_twitter_switch);
        instagramSwitch = (Switch) findViewById(R.id.link_instagram_switch);

        if (UserSession.FacebookToken != null)
            facebookSwitch.setChecked(true);

        facebookSwitch.setOnClickListener(this);
        twitterSwitch.setOnClickListener(this);
        instagramSwitch.setOnClickListener(this);

        if (UserSession.TwitterToken != null)
            twitterSwitch.setChecked(true);

        setAuthText();
        setListener();
        setActionBar();
    }

    private void setAuthText() {
        new facebookNetworkThread().execute();
        new twitterNetworkThread().execute();
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

        AlertDialog dialog = new AlertDialog.Builder(this).setMessage("Facebook와 연동을 해제 합니다. 계약 동의도 해제 됩니다.")
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

        switch (resId)
        {
            case R.id.link_facebook_switch:
                if (checkStatus)
                    dialog.show();
                else
                    Toast.makeText(mContext, "True", Toast.LENGTH_SHORT).show();
                break;
            case R.id.link_twitter_switch:
                if (checkStatus)
                    dialog.show();
                else
                    Toast.makeText(mContext, "True", Toast.LENGTH_SHORT).show();
                break;
            case R.id.link_instagram_switch:
                if (checkStatus);
                else
                    dialog.show();
                break;
        }

    }

    private class facebookNetworkThread extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            URL facebookauthURL = null;
            HttpURLConnection fconnection = null;
            StringBuilder sb = new StringBuilder();
            try {
                facebookauthURL = new URL("http://100.24.24.64:3366/facebook");

                fconnection = (HttpURLConnection) facebookauthURL.openConnection();
                fconnection.setRequestMethod("GET");
                //fconnection.setDoOutput(true);

                //fconnection.setReadTimeout(3000);
                //fconnection.setConnectTimeout(3000);
                //fconnection.setDoInput(true);
                fconnection.connect();

                //Log.e(TAG, fconnection.getResponseCode() + " - " + fconnection.getURL().toString());

                InputStream is = fconnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String result;
                while ((result = br.readLine()) != null) {
                    sb.append(result + "\n");
                }
                //Log.e("hello", sb.toString());
            } catch (java.io.IOException e) {
                Log.e("helloworld", e.toString());
            } finally {
                fconnection.disconnect();
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    private class twitterNetworkThread extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            URL tauthURL = null;
            HttpURLConnection tconnection = null;
            StringBuilder sb = new StringBuilder();
            try {
                tauthURL = new URL("http://100.24.24.64:3366/twitter");

                tconnection = (HttpURLConnection) tauthURL.openConnection();
                tconnection.setRequestMethod("GET");
                //fconnection.setDoOutput(true);

                //fconnection.setReadTimeout(3000);
                //fconnection.setConnectTimeout(3000);
                //fconnection.setDoInput(true);
                tconnection.connect();

                //Log.e(TAG, fconnection.getResponseCode() + " - " + fconnection.getURL().toString());

                InputStream is = tconnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String result;
                while ((result = br.readLine()) != null) {
                    sb.append(result + "\n");
                }
                //Log.e("t hello", sb.toString());
            } catch (java.io.IOException e) {
                Log.e("t helloworld", e.toString());
            } finally {
                tconnection.disconnect();
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
    //http://100.24.24.64:3366/twitter

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

    private void setListener() {

    }


}
