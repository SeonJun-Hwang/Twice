package sysproj.seonjoon.twice.view;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import sysproj.seonjoon.twice.OnHashtagClickListener;
import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.manager.LoginManager;

public class MainActivity extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener, OnHashtagClickListener {

    private static final String TAG = "MAIN_ACTIVITY";
    private static final int HOME = 0;
    private static final int SEARCH = 1;

    private Context mContext;
    private FragmentManager fm;

    private DrawerLayout personDrawer;
    private NavigationView personNavigation;
    private ImageView openMenuButton;
    private RadioButton homeButton;
    private RadioButton searchButton;
    private TextView topHomeText;
    private EditText topEditText;
    private Fragment[] fragments = new Fragment[]{null, null};
    private String[] fragmentsTags = new String[]{"Home", "Search"};

    private long parsedTime = 0;
    private static int showingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        fm = getFragmentManager();

        setLayout();
        setListener();

        showHomeFragment();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.drawer_logout:
                askLogout();
                break;

            case R.id.drawer_app_setting:
                gotoSettingActivity();
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {

        if (personDrawer.isDrawerOpen(Gravity.LEFT)) {
            personDrawer.closeDrawer(Gravity.LEFT);
        } else {
            if (parsedTime == 0) {
                Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
                parsedTime = System.currentTimeMillis();
            } else {
                int seconds = (int) (System.currentTimeMillis() - parsedTime);
                if (seconds > 2000) {
                    Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
                    parsedTime = 0;
                } else {
                    super.onBackPressed();
                    finish();
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        LoginManager.getInstance().SignOut();
    }

    @Override
    public void onClickHashTag(String hashTag) {
        showSearchFragment();
        ((FragmentSearch) getFragmentManager().findFragmentByTag(fragmentsTags[1])).startSearch(hashTag);
    }

    private void setLayout() {
        topHomeText = (TextView) findViewById(R.id.main_top_home_text);
        topEditText = (EditText) findViewById(R.id.main_top_search_edit);

        personDrawer = (DrawerLayout) findViewById(R.id.main_drawer_user);
        personNavigation = (NavigationView) findViewById(R.id.drawer_user_navigation);
        personDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        openMenuButton = (ImageView) findViewById(R.id.main_profile_setting);

        homeButton = (RadioButton) findViewById(R.id.main_bottom_home_button);
        searchButton = (RadioButton) findViewById(R.id.main_bottom_search_button);
    }

    private void changeTopViewVisibility(int showPos) {
        switch (showPos) {
            case HOME:
                topHomeText.setVisibility(View.VISIBLE);
                topEditText.setVisibility(View.GONE);
                break;
            case SEARCH:
                topHomeText.setVisibility(View.GONE);
                topEditText.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setListener() {

        topEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        String searchTag = textView.getText().toString();
                        ((FragmentSearch) getFragmentManager().findFragmentByTag(fragmentsTags[1])).startSearch(searchTag);
                        break;
                }

                return false;
            }
        });

        homeButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    showHomeFragment();
                }
            }
        });

        searchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    showSearchFragment();
                }
            }
        });

        // Drawer Set
        openMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "Click Open Menu Button");
                if (!personDrawer.isDrawerOpen(GravityCompat.START))
                    personDrawer.openDrawer(Gravity.LEFT);
            }
        });

        personNavigation.setNavigationItemSelectedListener(this);
    }

    private void askLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setMessage(R.string.main_dialog_logout_message)
                .setPositiveButton(R.string.main_dialog_logout_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);

                        finish();
                    }
                }).create().show();
    }

    private void gotoSettingActivity() {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    private void showHomeFragment() {
        showingFragment = HOME;
        changeTopViewVisibility(showingFragment);

        if (fragments[HOME] == null) {
            fragments[HOME] = new FragmentHome();

            fm.beginTransaction().add(R.id.main_frame_layout, fragments[HOME], fragmentsTags[HOME]).commit();
        }

        fm.beginTransaction().show(fragments[HOME]).commit();
        if (fragments[SEARCH] != null) fm.beginTransaction().hide(fragments[SEARCH]).commit();
    }

    private void showSearchFragment() {
        showingFragment = SEARCH;
        changeTopViewVisibility(showingFragment);

        if (fragments[SEARCH] == null) {
            fragments[SEARCH] = new FragmentSearch();

            fm.beginTransaction().add(R.id.main_frame_layout, fragments[SEARCH], fragmentsTags[SEARCH]).commit();
        }

        fm.beginTransaction().hide(fragments[HOME]).commit();
        fm.beginTransaction().show(fragments[SEARCH]).commit();
    }

    public static int getShowingFragmentNumber() {
        return showingFragment;
    }
}
