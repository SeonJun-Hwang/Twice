package sysproj.seonjoon.twice.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.IntentCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import sysproj.seonjoon.twice.DBAccessResultCallback;
import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.manager.DBManager;
import sysproj.seonjoon.twice.staticdata.UserSession;

import static android.widget.Toast.LENGTH_LONG;

public class ChangePasswordActivity extends AppCompatActivity {
    private static final String TAG = "ChangePasswordActivity";

    private Context mContext;
    private EditText changeP, changePok;
    private Button Ok;
    private FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_password_activity);

        mContext = this;

        changeP = (EditText) findViewById(R.id.change_password_text);
        changePok = (EditText) findViewById(R.id.change_password_ok_text);

        Ok = (Button) findViewById(R.id.change_ok);

        user = FirebaseAuth.getInstance().getCurrentUser();

        setActionBar();

        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPassword()) {
                    if (changeP.getText().toString().equals(changePok.getText().toString())) {
                        DBManager.getInstance().ChangeUserPassword(changeP.getText().toString(), new DBAccessResultCallback(){
                            @Override
                            public void AccessCallback(boolean isSuccess) {
                                if (isSuccess) {
                                    Toast.makeText(mContext, "변경이 완료 되었습니다.", LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Toast.makeText(mContext, "비밀번호 변경에 실패 하였습니다.\n 잠시 후 다시 시도해주시기 바랍니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "변경할 비밀번호와 다릅니다", LENGTH_LONG).show();
                        changePok.setText(null);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Password는 8글자 이상이여야 합니다.", LENGTH_LONG).show();
                    changeP.setText(null);
                    changePok.setText(null);
                }
            }
        });

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


    private boolean checkPassword() {
        String password = changeP.getText().toString();

        return password.matches("^[a-zA-Z0-9_]*$") && (password.length() >= UserSession.MIN_PASSWORD_LENGTH);
    }
}
