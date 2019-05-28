package sysproj.seonjoon.twice.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.CountDownLatch;

import sysproj.seonjoon.twice.DBAccessResultCallback;
import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.manager.DBManager;
import sysproj.seonjoon.twice.manager.LoginManager;
import sysproj.seonjoon.twice.staticdata.SNSTag;
import sysproj.seonjoon.twice.staticdata.UserSession;

public class RegisterActivity extends Activity {

    private final static String TAG = "RegisterActivity";
    private Context mContext;
    private long parsedTime = 0;
    private TextView headText;
    private AutoCompleteTextView idEdit;
    private EditText passwordEdit;
    private Button duplicateButton;
    private Button nextButton;

    private CheckDuplicateAsnc checkDuplicateAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mContext = this;

        headText = (TextView) findViewById(R.id.twice_form_head_text);
        idEdit = (AutoCompleteTextView) findViewById(R.id.twice_id);
        passwordEdit = (EditText) findViewById(R.id.twice_password);
        duplicateButton = (Button) findViewById(R.id.twice_duplicate);
        nextButton = (Button) findViewById(R.id.twice_next);

        headText.setText(getString(R.string.twice_form_head));
        nextButton.setText(getString(R.string.share_register_tag));

        setListener();
    }

    private boolean checkEnableId() {
        String id = idEdit.getText().toString();
        return id.matches("^[a-z0-9_]*$");
    }

    private boolean checkPassword() {
        String password = passwordEdit.getText().toString();

        return password.matches("^[a-z0-9_]*$") && (password.length() >= UserSession.MIN_PASSWORD_LENGTH);
    }

    private void setListener() {
        duplicateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkEnableId()) {
                    if (checkDuplicateAsync == null) {
                        checkDuplicateAsync = new CheckDuplicateAsnc();
                        checkDuplicateAsync.execute(idEdit.getText().toString());
                    }
                } else
                    Toast.makeText(mContext, "ID는 영어 소문자 및 숫자만 가능합니다.", Toast.LENGTH_SHORT).show();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPassword()) {
                    String email = idEdit.getText().toString() + SNSTag.TWICE_EMAIL_TAIL;
                    String password = passwordEdit.getText().toString();
                    DBManager.getInstance().createUser(RegisterActivity.this, email, password, new DBAccessResultCallback() {
                        @Override
                        public void AccessCallback(boolean isSuccess) {
                            if (isSuccess) {
                                Toast.makeText(mContext, "회원가입 되었습니다.", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(mContext, "회원가입에 실패 하였습니다.\n 잠시 후 다시 시도해주시기 바랍니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else
                    Toast.makeText(mContext, "Password는 8글자 이상이여야 합니다.", Toast.LENGTH_SHORT).show();
            }
        });
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

    private class CheckDuplicateAsnc extends AsyncTask<String, Void, Void> implements DialogInterface.OnClickListener {
        private String input;
        private Boolean result;
        private ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("ID 중복 검사중입니다.");
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {
            result = true;
            input = strings[0];

            //TODO : Check ID Duplicate Code
            final CountDownLatch latch = new CountDownLatch(1);
            DBManager.getInstance().checkDuplicateUser(input, new DBAccessResultCallback() {
                @Override
                public void AccessCallback(boolean isSuccess) {
                    result = isSuccess;
                    latch.countDown();
                }
            });

            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);

            checkDuplicateAsync = null;
            if (progressDialog != null){
                progressDialog.dismiss();
                progressDialog = null;
            }

            if (result) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage(input + "은 사용 가능한 아이디 입니다.\n 사용하시겠습니까?")
                        .setPositiveButton(getString(R.string.register_dialog_able_message_positive), this)
                        .setNegativeButton(getString(R.string.register_dialog_able_message_negative), null);
                AlertDialog resultDialog = builder.create();
                resultDialog.show();
            } else {
                Toast.makeText(mContext, "중복된 ID 입니다.", Toast.LENGTH_SHORT).show();
                //Toast.makeText(mContext, "누락된 정보가 있습니다.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            idEdit.setEnabled(!result);
            passwordEdit.setEnabled(result);
            nextButton.setEnabled(result);
        }
    }
}
