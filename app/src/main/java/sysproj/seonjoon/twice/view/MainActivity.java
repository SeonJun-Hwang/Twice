package sysproj.seonjoon.twice.view;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.manager.LoginManager;

public class MainActivity extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MAIN_ACTIVITY";

    private Context mContext;
    private FragmentManager fm;

    private DrawerLayout personDrawer;
    private NavigationView personNavigation;
    private ImageView openMenuButton;
    private RadioButton homeButton;
    private RadioButton searchButton;
    private Fragment[] fragments = new Fragment[]{new FragmentHome(), null};
    private final String[] fragmentTags = new String[]{"Home", "Search"};

    private long parsedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.main_frame_layout, fragments[0]).commit();

        setLayout();
        setListener();
    }

    private void setLayout() {
        personDrawer = (DrawerLayout) findViewById(R.id.main_drawer_user);
        personNavigation = (NavigationView) findViewById(R.id.drawer_user_navigation);
        personDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        openMenuButton = (ImageView) findViewById(R.id.main_profile_setting);

        homeButton = (RadioButton) findViewById(R.id.main_bottom_home_button);
        searchButton = (RadioButton) findViewById(R.id.main_bottom_search_button);
    }

    private void setListener() {

        homeButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    fm.beginTransaction().show(fragments[0]).commit();

                    if (fragments[1] != null) fm.beginTransaction().show(fragments[1]).commit();
                }
            }
        });

        searchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (fragments[1] == null) {
                        fragments[1] = new FragmentSearch();
                        fm.beginTransaction().add(R.id.main_frame_layout, fragments[1]).commit();
                    }

                    fm.beginTransaction().hide(fragments[0]).commit();
                    fm.beginTransaction().show(fragments[1]).commit();
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


}
