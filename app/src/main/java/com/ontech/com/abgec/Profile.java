package com.ontech.com.abgec;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImageTranscoderType;
import com.facebook.imagepipeline.core.MemoryChunkType;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import me.ibrahimsn.lib.SmoothBottomBar;


public class Profile extends Fragment {

    View view;
    Context contextNullSafe;
    LinearLayout editProfile,posted_jobs;
    TextView bio, organiztion, designation, year, name, state, country, city ,branch, passout_yr,dob,occupation;
    TextView gend;
    Dialog dialog;
    CircleImageView fb, twitter, linkidin, insta,whatsapp;
    String gen, dateOfBirth, bioo, fcb, twt, lin, inst, occup, organ, desig, nam, br, py, countr, stat, cit,dp_link,phone;
    String uid_of_user,addtostack;
    SmoothBottomBar smoothBottomBar;
    DatabaseReference reference, user_ref;
    SimpleDraweeView profile_pic;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_profile, container, false);

        editProfile = view.findViewById(R.id.editProfile);
        posted_jobs = view.findViewById(R.id.layout);
        whatsapp = view.findViewById(R.id.whatsapp);

        requireActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        try {
            addtostack=getArguments().getString("sending_user_from_sync");
            uid_of_user = getArguments().getString("uid_sending_profile");
        } catch (Exception e) {
            e.printStackTrace();
        }

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
      /*  if (check_for_admin()){
            editProfile.setVisibility(View.GONE);
        }*/

        if(uid_of_user==null) {
            uid_of_user = user.getUid();//check for uid bundle if yes then don't do this and vice-versa.
        }

        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        reference = FirebaseDatabase.getInstance().getReference().child("users");
        name = view.findViewById(R.id.name);
        state =  view.findViewById(R.id.state_value);
        city =  view.findViewById(R.id.city_p);
        country =  view.findViewById(R.id.country_p);
        //mobile_no = view.findViewById(R.id.mobile_no);
        branch =  view.findViewById(R.id.branch_p);
        passout_yr =  view.findViewById(R.id.passout_p);
        organiztion =  view.findViewById(R.id.organ_p);
        designation =  view.findViewById(R.id.design_p);
        insta =  view.findViewById(R.id.instagram);
        linkidin =  view.findViewById(R.id.linkedin);
        fb =  view.findViewById(R.id.facebook);
        twitter =  view.findViewById(R.id.twitter);
        occupation = view.findViewById(R.id.occup_p);
        bio =  view.findViewById(R.id.bio);
        gend = view.findViewById(R.id.gender_p);
        profile_pic = view.findViewById(R.id.profile_img);



        if (!check_for_student()){
            whatsapp.setVisibility(View.VISIBLE);
            smoothBottomBar=requireActivity().findViewById(R.id.bottomBar);
            smoothBottomBar.setItemActiveIndex(3);
        }

        //dob = view.findViewById()
        if (contextNullSafe == null) getContextNullSafety();
//Hide the keyboard
        requireActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );


        if (uid_of_user.equals(user.getUid())) {
            editProfile.setVisibility(View.VISIBLE);
            posted_jobs.setVisibility(View.VISIBLE);
        }


        Fresco.initialize(
                getContextNullSafety(),
                ImagePipelineConfig.newBuilder(getContextNullSafety())
                        .setMemoryChunkType(MemoryChunkType.BUFFER_MEMORY)
                        .setImageTranscoderType(ImageTranscoderType.JAVA_TRANSCODER)
                        .experiment().setNativeCodeDisabled(true)
                        .build());



        valueGetting();



        OnBackPressedCallback callback=new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(((FragmentActivity) getContextNullSafety()).getSupportFragmentManager().findFragmentById(R.id.drawer) != null) {
                    ((FragmentActivity) getContextNullSafety()).getSupportFragmentManager()
                            .beginTransaction().
                            remove(Objects.requireNonNull(((FragmentActivity) getContextNullSafety()).getSupportFragmentManager().findFragmentById(R.id.drawer))).commit();
                }
                ((FragmentActivity) getContextNullSafety()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container,new Post())
                        .commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),callback);

        editProfile.setOnClickListener(v->{

           /* if(!name.getText().toString().equals("")) {
                Intent intent = new Intent(getContextNullSafety(), Edit.class);
                intent.putExtra("gender", gen);
                intent.putExtra("dob", dateOfBirth);
                intent.putExtra("bio", bioo);
                intent.putExtra("fb", fcb);
                intent.putExtra("instagram", inst);
                intent.putExtra("linkedin", lin);
                intent.putExtra("twitter", twt);
                intent.putExtra("occupation", occup);
                intent.putExtra("organisation", organ);
                intent.putExtra("designation", desig);
                intent.putExtra("name", nam);
                intent.putExtra("branch", br);
                intent.putExtra("passout_yr", py);
                intent.putExtra("country", countr);
                intent.putExtra("state", stat);
                intent.putExtra("city", cit);
                intent.putExtra("dp_link", dp_link);
                startActivity(intent);
            }
            else {*/

                dialog = new Dialog(getContextNullSafety());
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.loading2);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
                new  Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        dialog.dismiss();
                        Intent intent = new Intent(getContextNullSafety(), Edit.class);
                        startActivity(intent);
                    }

                },1000);


           // }

        });

        return view;
    }
    private void openTwitter(String twitterUsername) {

        try {
            // Get the Twitter app's URI
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + twitterUsername));

            // Check if the Twitter app is installed
            PackageManager packageManager = getContextNullSafety().getPackageManager();
            List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, 0);

            if (resolveInfoList.size() > 0) {
                // If the Twitter app is installed, open it
                startActivity(intent);
            } else {
                // If the Twitter app is not installed, open Twitter in a browser
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + twitterUsername));
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void openFacebook(String facebookId) {
        // Replace with Facebook ID

        try {
            // Get the Facebook app's URI
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/" + facebookId));

            // Check if the Facebook app is installed
            PackageManager packageManager = getContextNullSafety().getPackageManager();
            List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, 0);

            if (resolveInfoList.size() > 0) {
                // If the Facebook app is installed, open it
                startActivity(intent);
            } else {
                // If the Facebook app is not installed, open Facebook in a browser
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + facebookId));
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


/*
    private boolean check_for_admin(){
        SharedPreferences pref = getContextNullSafety().getSharedPreferences("our_user?", MODE_PRIVATE);
        return pref.getBoolean("alumni", true);
    }
*/

    private boolean check_for_student(){
        SharedPreferences pref = getContextNullSafety().getSharedPreferences("our_user?", MODE_PRIVATE);
        return pref.getBoolean("student", true);
    }

    private void valueGetting() {

        reference = FirebaseDatabase.getInstance().getReference().child("users").child(uid_of_user);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    gen = snapshot.child("gender").getValue(String.class);
                    dateOfBirth = snapshot.child("dob").getValue(String.class);
                    bioo = snapshot.child("bio").getValue(String.class);
                    fcb = snapshot.child("fb").getValue(String.class);
                    inst = snapshot.child("insta").getValue(String.class);
                    lin = snapshot.child("linkedin").getValue(String.class);
                    twt = snapshot.child("twitter").getValue(String.class);
                    occup = snapshot.child("occupation").getValue(String.class);
                    organ = snapshot.child("organization").getValue(String.class);
                    desig = snapshot.child("designation").getValue(String.class);
                    nam = snapshot.child("name").getValue(String.class);
                    br = snapshot.child("branch").getValue(String.class);
                    py = snapshot.child("passout").getValue(String.class);
                    countr = snapshot.child("country").getValue(String.class);
                    stat = snapshot.child("state").getValue(String.class);
                    cit = snapshot.child("city").getValue(String.class);
                    dp_link =snapshot.child("dp_link").getValue(String.class);
                    phone = snapshot.child("phone").getValue(String.class);

                    try {
                        Uri uri = Uri.parse(dp_link);
                        profile_pic.setImageURI(uri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    if(!fcb.equals("")){
                        fb.setVisibility(View.VISIBLE);
                    }
                    if(!inst.equals("")){
                        insta.setVisibility(View.VISIBLE);
                    }if(!twt.equals("")){
                        twitter.setVisibility(View.VISIBLE);
                    }if(!lin.equals("")){
                        linkidin.setVisibility(View.VISIBLE);
                    }


                    //setting values
                    /*if (gen.equals("Male")) {
                        male.setBackgroundResource(R.drawable.bg_selector);
                        female.setBackgroundResource(R.drawable.bg_male);
                        gender = "Male";
                    } else {
                        female.setBackgroundResource(R.drawable.bg_selector);
                        male.setBackgroundResource(R.drawable.bg_male);
                        gender = "Male";
                    }*/

                   // date_txt.setText(dateOfBirth);
                    bio.setText(bioo);
                    organiztion.setText(organ);
                    designation.setText(desig);
                    occupation.setText(occup);
                    name.setText(nam);
                    branch.setText(br);
                    if (!py.equals("")) {
                        passout_yr.setVisibility(View.VISIBLE);
                        passout_yr.setText(py);
                    }
                    country.setText(countr);
                    state.setText(stat);
                    city.setText(cit);

                    if (gen.equals("Female")){
                        gend.setText("(She/her)");
                    }
                    else
                        gend.setText("(He/him)");
                }

                whatsapp.setOnClickListener(v->{
                    String phoneNumber = phone;
                    String message = "Hello,sir how are you?"; // Replace with message

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + message));

                    startActivity(intent);
                });

                if (!lin.equals("")) {
                    linkidin.setOnClickListener(v -> {
                        try {
                            String url = lin;
                            Intent linkedInAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            linkedInAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                            startActivity(linkedInAppIntent);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Invalid link", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                if (!twt.equals("")) {
                    twitter.setOnClickListener(v -> {
                        openTwitter(twt);
                    });
                }

                if (!inst.equals("")) {
                    insta.setOnClickListener(v -> {
                        Intent insta_in;
                        String scheme = "http://instagram.com/_u/"+inst;
                        String path = "https://instagram.com/"+inst;
                        String nomPackageInfo ="com.instagram.android";
                        try {
                            requireContext().getPackageManager().getPackageInfo(nomPackageInfo, 0);
                            insta_in = new Intent(Intent.ACTION_VIEW, Uri.parse(scheme));
                        } catch (Exception e) {
                            insta_in = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
                        }
                        startActivity(insta_in);
                    });
                }

                if (!fcb.equals("")) {
                    fb.setOnClickListener(v -> {
                        openFacebook(fcb);
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public Context getContextNullSafety() {
        if (getContext() != null) return getContext();
        if (getActivity() != null) return getActivity();
        if (contextNullSafe != null) return contextNullSafe;
        if (getView() != null && getView().getContext() != null) return getView().getContext();
        if (requireContext() != null) return requireContext();
        if (requireActivity() != null) return requireActivity();
        if (requireView() != null && requireView().getContext() != null)
            return requireView().getContext();

        return null;

    }
}