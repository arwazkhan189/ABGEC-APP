package com.ontech.com.abgec;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.util.Objects;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;
import www.sanju.motiontoast.MotionToast;


public class MainActivity extends AppCompatActivity {

    SmoothBottomBar bottomBar,bottomBarS;
    Toolbar toolbar;
    NavigationView navView;
    OnBackPressedListener onBackpressedListener;
    GoogleSignInClient mGoogleSignInClient;
    DrawerLayout drawer;
    LottieAnimationView add_job;
    Dialog dialog;
    FirebaseAuth auth;
    FirebaseUser user;
    TextView yes,no;
    ImageView globe;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        navView = findViewById(R.id.navView);
        drawer = findViewById(R.id.drawer);
        add_job = findViewById(R.id.add_job);
        setStatusBarTransparent();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
       /* user =
       //SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);*/

        globe = findViewById(R.id.globe);

        setSupportActionBar(toolbar);

        //set default home fragment and its title
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new Post()).commit();
        navView.setCheckedItem(R.id.nav_home);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.main_blue));
        toggle.syncState();

        add_job.setOnClickListener(v -> {
            MainActivity.this.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.change_layout, new AddJob(), "list_announcement")
                    .commit();
        });

        globe.setOnClickListener(v -> {
            String url = "https://abgec.in/";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean connected = (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);

        if (!connected){
            MotionToast.Companion.darkColorToast(MainActivity.this,
                    "No Internet",
                    "Connect with mobile network",
                    MotionToast.TOAST_NO_INTERNET,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(MainActivity.this, R.font.lexend));
        }

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            Fragment fragment = null;

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        fragment = new Post();
                        drawer.closeDrawer(GravityCompat.START);
                        callFragment(fragment);
                        break;

                    case R.id.nav_logout:

                        dialog = new Dialog(MainActivity.this);
                        dialog.setContentView(R.layout.dialog_logout);
                        dialog.setCancelable(false);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        dialog.show();
                        yes = dialog.findViewById(R.id.yes);
                        no = dialog.findViewById(R.id.no);

                        yes.setOnClickListener(v->{
                            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                    .requestIdToken(getString(R.string.default_web_client_id))
                                    .requestEmail()
                                    .build();
                            mGoogleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);

                            mGoogleSignInClient.signOut()
                                    .addOnCompleteListener(MainActivity.this, task -> MotionToast.Companion.darkColorToast(MainActivity.this,
                                            "Logout Successfull",
                                            "Sign out Successfull!",
                                            MotionToast.TOAST_SUCCESS,
                                            MotionToast.GRAVITY_BOTTOM,
                                            MotionToast.LONG_DURATION,
                                            ResourcesCompat.getFont(MainActivity.this, R.font.lexend)));
                            auth.signOut();
                            //deleteCache(MainActivity.this);
                            finish();
                        });


                        no.setOnClickListener(v->{
                            dialog.dismiss();
                            drawer.closeDrawer(GravityCompat.START);
                        });

                    case R.id.list:

                        drawer.closeDrawer(GravityCompat.START);
                        Intent intent = new Intent(MainActivity.this,ListOfAlumni.class);
                        startActivity(intent);
                       /* fragment = new HomePage();
                        drawer.closeDrawer(GravityCompat.START);
                        //  getSupportActionBar().setTitle("About US");
                        callFragment(fragment);
                        break;*/

                }
                return true;
            }
        });

        if (check_for_student()) {
            add_job.setVisibility(View.GONE);
        }

        if (getSupportFragmentManager().findFragmentById(R.id.container) != null) {
            getSupportFragmentManager()
                    .beginTransaction().
                    remove(Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.container))).commit();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new Post())
                .commit();

        bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setItemActiveIndex(0);

        bottomBar.setOnItemSelectedListener((OnItemSelectedListener) i -> {


             if (i == 0) {
                 bottomBar.setItemActiveIndex(0);
                 if (MainActivity.this.getSupportFragmentManager().findFragmentById(R.id.container) != null) {
                     MainActivity.this.getSupportFragmentManager()
                             .beginTransaction().
                             remove(Objects.requireNonNull(MainActivity.this.getSupportFragmentManager().findFragmentById(R.id.container))).commit();
                 }
                 MainActivity.this.getSupportFragmentManager()
                         .beginTransaction()
                         .replace(R.id.container, new Post())
                         .commit();
             }
            else if (i == 1) {
                bottomBar.setItemActiveIndex(1);
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
            } else if (i == 2) {
                bottomBar.setItemActiveIndex(2);
                if (MainActivity.this.getSupportFragmentManager().findFragmentById(R.id.container) != null) {
                    MainActivity.this.getSupportFragmentManager()
                            .beginTransaction().
                            remove(Objects.requireNonNull(MainActivity.this.getSupportFragmentManager().findFragmentById(R.id.container))).commit();
                }
                MainActivity.this.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new AlumniList(), "list_announcement")
                        .commit();
            } else if (i == 3) {
                if (check_for_student()) {
                    MotionToast.Companion.darkColorToast(MainActivity.this,
                            "Access Denied ☹️",
                            "You do not have authority to access your profile",
                            MotionToast.TOAST_ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(MainActivity.this, R.font.lexend));
                }
                else {
                    bottomBar.setItemActiveIndex(3);
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

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
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