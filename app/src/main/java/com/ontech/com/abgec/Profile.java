package com.ontech.com.abgec;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class Profile extends Fragment {

    View view;
    Context contextNullSafe;
    LinearLayout editProfile;
    TextView bio, fb, twitter, linkidin, insta, organiztion, designation, year, name, state, country, city ,branch, passout_yr,dob;
    TextView gend;
    String gen, dateOfBirth, bioo, fcb, twt, lin, inst, occup, organ, desig, nam, br, py, countr, stat, cit, dp_uri;

    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_profile, container, false);
        editProfile = view.findViewById(R.id.editProfile);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
      /*  if (check_for_admin()){
            editProfile.setVisibility(View.GONE);
        }*/
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        name = view.findViewById(R.id.name);
        state =  view.findViewById(R.id.state_value);
        city =  view.findViewById(R.id.city_p);
        country =  view.findViewById(R.id.country_p);
        //mobile_no = view.findViewById(R.id.mobile_no);
        branch =  view.findViewById(R.id.branch_p);
        passout_yr =  view.findViewById(R.id.passout_p);
        organiztion =  view.findViewById(R.id.organ_p);
        designation =  view.findViewById(R.id.design_p);
       /* insta =  view.findViewById(R.id.instagram);
        linkidin =  view.findViewById(R.id.linkedin);
        fb =  view.findViewById(R.id.facebook);
        twitter =  view.findViewById(R.id.twitter);*/
        bio =  view.findViewById(R.id.bio);
        gend = view.findViewById(R.id.gender_p);
        //dob = view.findViewById()

        valueGetting();

        editProfile.setOnClickListener(v->{
           /* requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.change_layout, new EditProfile(), "list_announcement")
                    .commit();*/
            Intent intent = new Intent(getContextNullSafety(),Edit.class);
            startActivity(intent);
        });


        /*OnBackPressedCallback callback=new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentManager fm=((FragmentActivity) getContextNullSafety()).getSupportFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                if(fm.getBackStackEntryCount()>0) {
                    fm.popBackStack();
                }
                ft.commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),callback);*/
        return view;
    }


/*
    private boolean check_for_admin(){
        SharedPreferences pref = getContextNullSafety().getSharedPreferences("our_user?", MODE_PRIVATE);
        return pref.getBoolean("alumni", true);
    }
*/

    private void valueGetting() {

        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    gen = snapshot.child("gender").getValue(String.class);
                    dateOfBirth = snapshot.child("dob").getValue(String.class);
                    bioo = snapshot.child("bio").getValue(String.class);
                    fcb = snapshot.child("fb").getValue(String.class);
                    inst = snapshot.child("insta").getValue(String.class);
                    lin = snapshot.child("linedin").getValue(String.class);
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
                    dp_uri = snapshot.child("dp_link").getValue(String.class);



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
                    /*fb.setText(fcb);
                    twitter.setText(twt);
                    linkidin.setText(lin);
                    insta.setText(inst);*/
                    organiztion.setText(organ);
                    designation.setText(desig);
                    name.setText(nam);
                    branch.setText(br);
                    passout_yr.setText(py);
                    country.setText(countr);
                    state.setText(stat);
                    city.setText(cit);
                    gend.setText(gen);
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