package sysproj.seonjoon.twice.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.manager.DBManager;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AccountActivity";

    private Context mContext;
    private ImageButton updatePasswordBtn, signoutBtn;
    private TextView IDText, emailText, passwordText, signoutTest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity);

        IDText = (TextView) findViewById(R.id.account_ID_text);
        emailText = (TextView) findViewById(R.id.account_email_text);
        passwordText = (TextView) findViewById(R.id.account_password_text);
        signoutTest = (TextView) findViewById(R.id.account_signout_text);

        mContext = this;

        String ID = DBManager.getInstance().getUser().getEmail();
        int pos = ID.indexOf("@");

        String tempEmail = DBManager.getInstance().getUser().getProviderData().get(0).getEmail();

        IDText.setText(ID.substring(0, pos));
        emailText.setText(tempEmail);

        setActionBar();

        passwordText.setOnClickListener(this);
        signoutTest.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.account_password_text:

                break;
            case R.id.account_signout_text:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        v.getContext());

                // 제목셋팅
                alertDialogBuilder.setTitle("회원탈퇴");

                // AlertDialog 셋팅
                alertDialogBuilder
                        .setMessage("회원탈퇴 하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("예",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        DBManager.getInstance().DeleteUser();
                                        Toast.makeText(getApplicationContext(), "회원탈퇴가 완료 되었습니다.", Toast.LENGTH_LONG).show();
                                        FirebaseAuth.getInstance().signOut();
                                        Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                        .setNegativeButton("아니오",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        // 다이얼로그를 취소한다
                                        dialog.cancel();
                                    }
                                });
                // 다이얼로그 생성
                AlertDialog alertDialog = alertDialogBuilder.create();
                // 다이얼로그 보여주기
                alertDialog.show();
                break;

        }
    }

    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setBackgroundDrawable(new ColorDrawable(getColor(R.color.timelineHeadBack)));// #464A4F
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
