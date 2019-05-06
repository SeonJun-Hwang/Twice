package sysproj.seonjoon.twice.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.UserInformation;
import sysproj.seonjoon.twice.manager.LoginManager;
import sysproj.seonjoon.twice.staticdata.StaticAppData;

public class RegisterActivity extends FragmentActivity {

    private final static String TAG = "RegisterActivity";
    private Context mContext;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Fragment[] fragments = new Fragment[StaticAppData.ConsentFormPageCount];
    private String[] fragmentsTag = new String[StaticAppData.ConsentFormPageCount];
    private long parsedTime = 0;

    private UserInformation userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        fragments[0] = new FragmentConsentForm();
        fragments[1] = new FragmentTwiceForm();
        fragments[2] = new FragmentSNSForm();
        fragmentsTag[0] = "ConsentForm";
        fragmentsTag[1] = "TwiceForm";
        fragmentsTag[2] = "SNSForm";

        ft.add(R.id.register_frame_layout, fragments[1], fragmentsTag[1]);
        ft.commit();
    }

    public void changeFragment(int pageNumber) {
        if (pageNumber < 3) {
            fm = getSupportFragmentManager();
            ft = fm.beginTransaction();

            ft.replace(R.id.register_frame_layout, fragments[pageNumber], fragmentsTag[pageNumber]);
            ft.commit();
        }
    }

    public void setData(String id, String password, String email) {
        userInfo = new UserInformation(id, password, email);
    }

    public Context getmContext() {
        return mContext;
    }

    public UserInformation getData() {
        return userInfo;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragmentsTag[2]);
        if (fragment != null)
            fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (parsedTime == 0) {
            Toast.makeText(this, "한번 더 누르면 로그인 창으로 돌아갑니다.", Toast.LENGTH_SHORT).show();
            parsedTime = System.currentTimeMillis();
        } else {
            int seconds = (int) (System.currentTimeMillis() - parsedTime);
            if (seconds > 2000) {
                Toast.makeText(this, "한번 더 누르면 로그인 창으로 돌아갑니다.", Toast.LENGTH_SHORT).show();

                parsedTime = 0;
            } else
                super.onBackPressed();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        LoginManager.getInstance().SignOut();
    }
}
