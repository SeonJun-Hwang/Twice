package sysproj.seonjoon.twice.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import sysproj.seonjoon.twice.DBAccessResultCallback;
import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.manager.DBManager;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AccountActivity";

    private Context mContext;
    
    private TextView IDText, passwordText, signoutText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity);

        IDText = (TextView) findViewById(R.id.account_ID);
        passwordText = (TextView) findViewById(R.id.account_password_text);
        signoutText = (TextView) findViewById(R.id.account_signout_text);

        mContext = this;
        
        String ID = DBManager.getInstance().getUser().getEmail();
        int pos = ID.indexOf("@");
        IDText.setText("아이디 : " + ID.substring(0, pos));

        setActionBar();

        passwordText.setOnClickListener(this);
        signoutText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.account_password_text:
                gotoChangePasswordActivity();
                break;
            case R.id.account_signout_text:
                createAskingDialog(signoutText).show();
                break;

        }
    }

    private AlertDialog createAskingDialog(final TextView textView) {
        return new AlertDialog.Builder(this)
                .setMessage("정말로 회원탈퇴 하시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
                        DBManager.getInstance().DeleteUser(new DBAccessResultCallback() {
                            @Override
                            public void AccessCallback(boolean isSuccess) {
                                if (isSuccess) {
                                    Toast.makeText(mContext, "회원 탈퇴가 완료 되었습니다.", Toast.LENGTH_LONG).show();
                                    dialogInterface.dismiss();
                                    Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                    Toast.makeText(mContext, "오류가 발생하였습니다.\n 잠시후에 다시 시도해 주세요", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).create();
    }

    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setBackgroundDrawable(new ColorDrawable(getColor(R.color.timelineHeadBack)));// #464A4F
        }
    }

    private void gotoChangePasswordActivity() {
        Intent intent = new Intent(AccountActivity.this, ChangePasswordActivity.class);
        startActivity(intent);
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
