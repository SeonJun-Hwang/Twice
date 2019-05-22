package sysproj.seonjoon.twice.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import sysproj.seonjoon.twice.BuildConfig;
import sysproj.seonjoon.twice.DBLoadSuccessCallback;
import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.loader.DataLoader;
import sysproj.seonjoon.twice.loader.FacebookLoader;
import sysproj.seonjoon.twice.loader.PreferenceLoader;
import sysproj.seonjoon.twice.manager.LoginManager;
import sysproj.seonjoon.twice.parser.FacebookParser;
import sysproj.seonjoon.twice.parser.FacebookTokenParser;
import sysproj.seonjoon.twice.parser.SNSParser;
import sysproj.seonjoon.twice.parser.TokenParser;
import sysproj.seonjoon.twice.parser.TwitterTokenParser;
import sysproj.seonjoon.twice.staticdata.StaticAppData;
import sysproj.seonjoon.twice.staticdata.UserSession;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    private long parsedTime = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    private Context mContext = null;
    private static final String TAG = "LoginActivity";

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    private Button mEmailSignInButton;
    private Button mRegisterButton;
    private CheckBox mSaveIDCheck;
    private CheckBox mAutoLoginCheck;
    private SignInButton googleLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mRegisterButton = (Button) findViewById(R.id.login_register);
        mSaveIDCheck = (CheckBox) findViewById(R.id.login_save_id_button);
        mAutoLoginCheck = (CheckBox) findViewById(R.id.login_auto_login_button);
        googleLoginButton = (SignInButton) findViewById(R.id.login_google_login_button);

        mSaveIDCheck.setText(R.string.login_button_save_id);
        mAutoLoginCheck.setText(R.string.login_button_auto_Login);

        Intent intent = getIntent();
        String savedID = intent.getStringExtra(UserSession.UserIDTag);
        if (savedID != null && !savedID.isEmpty())
            mEmailView.setText(savedID);

        populateAutoComplete();
        setListener();
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {

        /**
         Request Permission
         **/

        return true;
    }

    private void setListener() {
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        googleLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignInOptions ops = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail().requestProfile().build();

                GoogleSignInClient client = GoogleSignIn.getClient(mContext, ops);

                if (client != null) {
                    Intent intent = client.getSignInIntent();
                    startActivityForResult(intent, StaticAppData.GoogleRequestCode);
                }
            }
        });

        mAutoLoginCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean changedVal) {
                mSaveIDCheck.setChecked(changedVal);
                mSaveIDCheck.setEnabled(!changedVal);
            }
        });
    }

    /**
     * Callback received when a permissions request has been completed.
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == StaticAppData.GoogleRequestCode) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    @Override
    public void onBackPressed() {
        if (parsedTime == 0) {
            Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            parsedTime = System.currentTimeMillis();
        } else {
            int seconds = (int) (System.currentTimeMillis() - parsedTime);
            if (seconds > 2000) {
                Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
                parsedTime = 0;
            } else
                super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            if (mAuthTask == null) {
                mAuthTask = new UserLoginTask(email, password);
                mAuthTask.execute();
            }
        }
    }

    private void attemptRegister() {
        Intent loginToRegister = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(loginToRegister);
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        //return email.contains("@");
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= UserSession.MIN_PASSWORD_LENGTH;
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> result) {
        try {
            GoogleSignInAccount account = result.getResult(ApiException.class);
        } catch (ApiException e) {
            Log.e(TAG, "Fail Code : " + e.getStatusCode());
        }
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mID;
        private final String mPassword;
        private final String TAG = "LOGIN_ASYNC";
        private FirebaseUser user = null;
        private ProgressDialog progressDialog;

        UserLoginTask(String id, String password) {
            mID = id;
            mPassword = password;

            progressDialog = new ProgressDialog(mContext);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("로그인 중입니다.");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(final Void... params) {
            // TODO: attempt authentication against a network service.
            boolean login = LoginManager.getInstance().TwiceLogin((LoginActivity) mContext, mID, mPassword);

            if (login) {
                Log.e(TAG, "Login Success");
                user = FirebaseAuth.getInstance().getCurrentUser();

                if (user == null)
                    login = false;
                else {
                    final CountDownLatch countDownLatch = new CountDownLatch(2);

                    LoginManager.getInstance().FacebookLogin(user.getUid(), new DBLoadSuccessCallback() {
                        @Override
                        public void LoadDataCallback(boolean isSuccess, Map<String, Object> result) {
                            if (isSuccess) {
                                TokenParser tokenParser = new FacebookTokenParser();
                                UserSession.FacebookToken = (AccessToken) tokenParser.map2Token(result);

                                Log.e(TAG, UserSession.FacebookToken.getUserId());

                            }
                            countDownLatch.countDown();
                        }
                    });

                    LoginManager.getInstance().TwitterLogin(user.getUid(), new DBLoadSuccessCallback() {
                        @Override
                        public void LoadDataCallback(boolean isSuccess, Map<String, Object> result) {
                            if (isSuccess) {
                                TokenParser tokenParser = new TwitterTokenParser();
                                UserSession.TwitterToken = (TwitterSession) tokenParser.map2Token(result);

                                TwitterCore.getInstance().getSessionManager().setActiveSession(UserSession.TwitterToken);
                            }
                            countDownLatch.countDown();
                        }
                    });

                    try {
                        countDownLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else
                Log.e(TAG, "Failure Login");

            return login;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }

            if (success) {
                user = FirebaseAuth.getInstance().getCurrentUser();

                if (mSaveIDCheck.isChecked()) {
                    PreferenceLoader.savePreference(mContext, BuildConfig.IDPreferenceKey, mID);
                }
                if (mAutoLoginCheck.isChecked()) {
                    PreferenceLoader.savePreference(mContext, BuildConfig.PwdPreferenceKey, mPassword);
                }

                Intent loginToMain = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(loginToMain);
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;

            progressDialog.dismiss();
            progressDialog = null;
        }
    }

}

