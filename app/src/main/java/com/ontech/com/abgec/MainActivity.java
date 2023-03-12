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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ontech.com.abgec.fcm.topic;

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
    Dialog dialog;
    FirebaseAuth auth;
    FirebaseUser user;
    Boolean closed = false;
    TextView yes,no,text1,text2;
    ImageView globe;
    FloatingActionButton add,job,post;
    Animation rotateOpen,rotateClose,fromBottom,toBottom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        navView = findViewById(R.id.navView);
        drawer = findViewById(R.id.drawer);
        setStatusBarTransparent();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
       /* user =
       //SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);*/

        rotateOpen =  AnimationUtils.loadAnimation(this,R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(this,R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(this,R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(this,R.anim.to_bottom_anim);


        text1 = findViewById(R.id.textView19);
        text2 = findViewById(R.id.textView20);
        globe = findViewById(R.id.globe);
        add = findViewById(R.id.add_f);
        job = findViewById(R.id.job);
        post = findViewById(R.id.post);

        setSupportActionBar(toolbar);

        //set default home fragment and its title
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new Post()).commit();
        navView.setCheckedItem(R.id.nav_home);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.main_blue));
        toggle.syncState();

        globe.setOnClickListener(v -> {
            String url = "https://abgec.in/";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        add.setOnClickListener(v->{
            OnAddButtonClick();
        });



        //edit fab button on click
        job.setOnClickListener(v->{

            OnAddButtonClick();
            MainActivity.this.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.change_layout, new AddJob(), "list_announcement")
                    .commit();


        });
        //setting fab button on click
        post.setOnClickListener(v->{

            OnAddButtonClick();
            Toast.makeText(this,"Search",Toast.LENGTH_SHORT).show();
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
                        drawer.closeDrawer(GravityCompat.START);
                        navView.getMenu().getItem(0).setCheckable(true);
                        break;

                    case R.id.list:

                        drawer.closeDrawer(GravityCompat.START);
                        Intent intent = new Intent(MainActivity.this,ListOfAlumni.class);
                        navView.getMenu().getItem(2).setCheckable(false);
                        startActivity(intent);
                        break;

                    case R.id.gallery:
                        fragment = new ImageFragment();
                        drawer.closeDrawer(GravityCompat.START);
                        navView.getMenu().getItem(1).setCheckable(false);
                        callFragment2(fragment);
                        break;

                    case R.id.privacy:
                        fragment = new Privacy();
                        drawer.closeDrawer(GravityCompat.START);
                        navView.getMenu().getItem(3).setCheckable(false);
                        callFragment2(fragment);
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
                            dialog.dismiss();
                            navView.getMenu().getItem(8).setCheckable(false);
                            //deleteCache(MainActivity.this);
                            auth.signOut();
                            startActivity(new Intent(MainActivity.this , Login.class));
                            finish();
                        });


                        no.setOnClickListener(v->{
                            dialog.dismiss();
                            navView.getMenu().getItem(8).setCheckable(false);
                            drawer.closeDrawer(GravityCompat.START);
                        });
                        break;

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
            add.setVisibility(View.GONE);
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

    @Override
    protected void onStart() {
        super.onStart();
        topic topic=new topic();
        String val = "";
        topic.noti("","" , val);
        Log.e("notification_intent",val);
        if (val.equals("fromjob")){
            Toast.makeText(MainActivity.this, "Getting from Job section notification  ", Toast.LENGTH_SHORT).show();
        }
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    private void OnAddButtonClick() {
        setVisibility(closed);
        setAnimation(closed);
        closed = !closed;
    }
    private void setAnimation(boolean closed) {
        if(!closed){
            job.startAnimation(fromBottom);
            post.startAnimation(fromBottom);
            add.startAnimation(rotateOpen);
        }else{
            job.startAnimation(toBottom);
            post.startAnimation(toBottom);
            add.startAnimation(rotateClose);
        }
    }
    // used to set visibility to VISIBLE / INVISIBLE
    private void setVisibility(boolean closed) {
        if(!closed)
        {
            job.setVisibility(View.VISIBLE);
            post.setVisibility(View.VISIBLE);
            text2.setVisibility(View.VISIBLE);
            text1.setVisibility(View.VISIBLE);
        }else{
            job.setVisibility(View.GONE);
            post.setVisibility(View.GONE);
            text2.setVisibility(View.GONE);
            text1.setVisibility(View.GONE);
        }
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
    private void callFragment2(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.replace(R.id.change_layout, fragment);
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