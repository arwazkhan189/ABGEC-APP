package com.ontech.com.abgec;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class MainActivity extends AppCompatActivity {

    SmoothBottomBar bottomBar;
    Toolbar toolbar;
    NavigationView navView;
    OnBackPressedListener onBackpressedListener;
    DrawerLayout drawer;
    ImageView globe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        navView = findViewById(R.id.navView);
        drawer = findViewById(R.id.drawer);

        setStatusBarTransparent();

       //SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        globe = findViewById(R.id.globe);

        setSupportActionBar(toolbar);

        //set default home fragment and its title
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomePage()).commit();
        navView.setCheckedItem(R.id.nav_home);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.main_blue));
        toggle.syncState();

        globe.setOnClickListener(v->{
            String url = "https://abgec.in/";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            Fragment fragment = null;

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        fragment = new HomePage();
                        drawer.closeDrawer(GravityCompat.START);
                        callFragment(fragment);
                        break;

                    case R.id.nav_contactUs:
                       /* fragment = new HomePage();
                        drawer.closeDrawer(GravityCompat.START);
                        callFragment(fragment);
                        break;*/

                    case R.id.nav_aboutUs:
                       /* fragment = new HomePage();
                        drawer.closeDrawer(GravityCompat.START);
                        //  getSupportActionBar().setTitle("About US");
                        callFragment(fragment);
                        break;*/

                }
                return true;
            }
        });
        if (getSupportFragmentManager().findFragmentById(R.id.container) != null) {
            getSupportFragmentManager()
                    .beginTransaction().
                    remove(Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.container))).commit();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new HomePage())
                .commit();

        bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setItemActiveIndex(0);

        bottomBar.setOnItemSelectedListener((OnItemSelectedListener) i -> {
            if (i == 0) {
                bottomBar.setItemActiveIndex(0);
                /*Intent intent = new Intent(Home.this , Home.class);
                startActivity(intent);*/

                if (getSupportFragmentManager().findFragmentById(R.id.container) != null) {
                    getSupportFragmentManager()
                            .beginTransaction().
                            remove(Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.container))).commit();
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new HomePage())
                        .commit();
            } else if (i == 1) {
                bottomBar.setItemActiveIndex(1);
                if (MainActivity.this.getSupportFragmentManager().findFragmentById(R.id.container) != null) {
                    MainActivity.this.getSupportFragmentManager()
                            .beginTransaction().
                            remove(Objects.requireNonNull(MainActivity.this.getSupportFragmentManager().findFragmentById(R.id.container))).commit();
                }
                MainActivity.this.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new AlumniList(), "list_announcement")
                        .commit();
            } else if (i == 2) {
                if (check_for_student()) {
                    MotionToast.Companion.darkColorToast(MainActivity.this,
                            "Access Denied ☹️",
                            "You do not have authority to access your profile",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(MainActivity.this, R.font.lexend));
                } else {
                    bottomBar.setItemActiveIndex(2);
                    if (MainActivity.this.getSupportFragmentManager().findFragmentById(R.id.container) != null) {
                        MainActivity.this.getSupportFragmentManager()
                                .beginTransaction().
                                remove(Objects.requireNonNull(MainActivity.this.getSupportFragmentManager().findFragmentById(R.id.container))).commit();
                    }
                    MainActivity.this.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, new Profile(), "list_announcement")
                            .commit();
                }
            }
            return false;
        });

    }


    private void callFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setStatusBarTransparent () {
        Window window = MainActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    //on backpress
//    @Override
//    public void onBackPressed() {
//        if (onBackpressedListener != null) {
//            getSupportActionBar().setTitle("Home");
//            navView.setCheckedItem(R.id.nav_home);
//            onBackpressedListener.doBack();
//            drawer.closeDrawer(GravityCompat.START);
//        } else if (onBackpressedListener == null) {
//            finish();
//            super.onBackPressed();
//        }
//    }

    public interface OnBackPressedListener {
        void doBack();
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackpressedListener = onBackPressedListener;
    }

    @Override
    protected void onDestroy() {
        onBackpressedListener = null;
        super.onDestroy();
    }

    private boolean check_for_student(){
        SharedPreferences pref = getSharedPreferences("our_user?", MODE_PRIVATE);
        return pref.getBoolean("student", true);
    }

}