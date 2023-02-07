package com.ontech.com.abgec;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.ontech.com.abgec.Adapter.IntroViewPagerAdapter;
import com.ontech.com.abgec.Model.intro_ScreenItem;

import java.util.ArrayList;
import java.util.List;

public class Onboarding extends AppCompatActivity {

    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter;
    TabLayout tabIndicator;
    LinearLayout btnGetStarted;
    LinearLayout btnNext;
    int position = 0;
    TextView tvSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        if (restorePrefData()) {
            Intent mainActivity = new Intent(getApplicationContext(),Login.class );
            startActivity(mainActivity);
            finish();
        }

        savePrefsData();

        Window window = Onboarding.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(Onboarding.this, R.color.white));
        // ini views
        btnNext = findViewById(R.id.next);
        btnGetStarted = findViewById(R.id.getstarted);
        tabIndicator = findViewById(R.id.tab_indicator);
        tvSkip = findViewById(R.id.tv_skip);

        //upar bar same color


        // fill list screen
        final List<intro_ScreenItem> mList = new ArrayList<>();
        mList.add(new intro_ScreenItem("SELECT ITEMS", "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", R.drawable.ic_welcome));
        mList.add(new intro_ScreenItem("PAYMENT", "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", R.drawable.ic_welcome));
        mList.add(new intro_ScreenItem("DELIVERY", "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", R.drawable.ic_welcome));
        // setup viewpager
        screenPager = findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this, mList);
        screenPager.setAdapter(introViewPagerAdapter);
        // setup tablayout with viewpager
        tabIndicator.setupWithViewPager(screenPager);


        // next button click Listner
        btnNext.setOnClickListener(v -> {

            position = screenPager.getCurrentItem();
            if (position < mList.size()) {
                position++;
                screenPager.setCurrentItem(position);
            }
            if (position == mList.size() - 1) { // when we rech to the last screen
                // TODO : show the GETSTARTED Button and hide the indicator and the next button
                loaddLastScreen();
            }
        });
        // tablayout add change listener
        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == mList.size() - 1) {
                    loaddLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        // skip button click listener
        tvSkip.setOnClickListener(v -> {
            screenPager.setCurrentItem(mList.size());
            //savePrefsData();
            //hkk
        });
        btnGetStarted.setOnClickListener(v -> {
            screenPager.setCurrentItem(mList.size());
            //savePrefsData();
            Intent intent = new Intent(Onboarding.this, Login.class);
            startActivity(intent);
            finish();
        });
    }



    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        return pref.getBoolean("isIntroOpnend", false);
    }

    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnend", true);
        editor.apply();
    }

    // show the GETSTARTED Button and hide the indicator and the next button
    private void loaddLastScreen() {
        btnNext.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        tvSkip.setVisibility(View.INVISIBLE);
        //tabIndicator.setVisibility(View.INVISIBLE);
        // TODO : ADD an animation the getstarted button
        // setup animation
        //btnGetStarted.setAnimation(btnAnim);
    }
}