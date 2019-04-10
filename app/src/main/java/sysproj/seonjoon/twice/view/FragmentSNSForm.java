package sysproj.seonjoon.twice.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.UserInformation;
import sysproj.seonjoon.twice.staticdata.SNSPermission;
import sysproj.seonjoon.twice.staticdata.SNSTag;
import sysproj.seonjoon.twice.staticdata.UserSession;


public class FragmentSNSForm extends Fragment {

    private static final String TAG = "SNSFrag";
    private static final int userAddReq = 10001;

    private TextView headText;
    private Button completeButton;
    private TwitterLoginButton twitterLoginButton;
    private LoginButton facebookLoginButton;
    private CallbackManager facebookCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sns_regist, container, false);

        headText = (TextView) root.findViewById(R.id.sns_form_head_text);
        completeButton = (Button) root.findViewById(R.id.sns_form_complete);
        twitterLoginButton = (TwitterLoginButton) root.findViewById(R.id.sns_form_twitter);
        facebookLoginButton = (LoginButton) root.findViewById(R.id.sns_form_facebook);
        facebookLoginButton.setFragment(this);

        headText.setText(getString(R.string.sns_form_head));
        completeButton.setText(getString(R.string.sns_form_complete));

        setCallBack();
        setListener();

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e(TAG, "Request - " + requestCode);

        if (requestCode == userAddReq) {
            if (requestCode == Activity.RESULT_OK)
                Toast.makeText(getContext(), "가입 완료 되었습니다.", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getContext(), "가입에 실패했습니다.", Toast.LENGTH_SHORT).show();

            boolean facebookRes = data.getBooleanExtra(SNSTag.FacebookResultTag, false);
            boolean twitterRes = data.getBooleanExtra(SNSTag.TwitterResultTag, false);

            if (!facebookRes || !twitterRes )
            {
                String fbMessage = "";
                String twMessage = "";

                if (!facebookRes)
                    fbMessage = "Facebook ";

                if (!twitterRes)
                    twMessage = "Twitter ";

                Snackbar.make(getView(),fbMessage + twMessage + "연동에 실패했습니다.", Snackbar.LENGTH_LONG).show();
            }

            ((RegisterActivity) getActivity()).finish();
        } else {
            facebookCallback.onActivityResult(requestCode, resultCode, data);
            twitterLoginButton.onActivityResult(requestCode, resultCode, data);
        }

        Log.e(TAG, "Activity Result " + requestCode);
    }

    private void setListener() {
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setMessage("해당 정보로 가입 하시겠습니까?").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // For Additional Register
                        LoginManager.getInstance().logOut();

                        IDRegister();
                    }
                }).setNegativeButton("취소", null);

                AlertDialog dialog = builder.create();
                dialog.show();
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
                Toast.makeText(getContext(), "취소하셨습니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, error.toString());
                Toast.makeText(getContext(), "네트워크 에러입니다. 잠시 후에 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
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

    private void IDRegister() {
        RegisterActivity activity = (RegisterActivity) getActivity();
        UserInformation information = activity.getData();
        Intent intent = new Intent(getActivity(), UserAddActivity.class);

        Log.e(TAG, information.getId() + ' ' + information.getPassword());
        intent.putExtra("email", information.getId());
        intent.putExtra("password", information.getPassword());

        startActivityForResult(intent, userAddReq);
    }
}
