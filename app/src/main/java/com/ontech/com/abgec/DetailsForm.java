package com.ontech.com.abgec;

import static android.content.Context.MODE_PRIVATE;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class DetailsForm extends Fragment {


    View view;
    TextView submit,name,state,country,city;
    AutoCompleteTextView branch,passout_yr;
    ConstraintLayout lay;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;
    Context contextNullSafe;
    String phone,token,uid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        assert getArguments() != null;
        phone = getArguments().getString("phone");
        token = getArguments().getString("token");
        uid = getArguments().getString("uid");

        view = inflater.inflate(R.layout.fragment_details_form, container, false);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        submit = view.findViewById(R.id.submit_txt);
        name = view.findViewById(R.id.name);
        state = view.findViewById(R.id.state);
        city = view.findViewById(R.id.city);
        country = view.findViewById(R.id.country);
        //mobile_no = view.findViewById(R.id.mobile_no);
        branch = view.findViewById(R.id.branch);
        passout_yr = view.findViewById(R.id.passout_yr);

        String[] department={"CSE - Computer Science", "IT - Information Technology", "ME - Mining","ETNT - Electronics & Telecommunication",
                "MECH - Mechanical","EE - Electrical","Civil"};


        String[] year={"1970" ,"1971" ,"1972", "1973", "1974", "1975" ,"1976", "1977","1978","1979", "1980", "1981" ,"1982" ,"1983" ,"1984", "1985" ,"1986" ,"1987" ,"1988", "1989", "1990", "1991","1992", "1993" ,"1994", "1995" ,"1996" ,"1997", "1998", "1999", "2000","2001","2002" ,"2003" ,"2004" ,"2005" ,"2006" ,"2007" ,
                "2008", "2009", "2010" ,"2011" ,"2012", "2013", "2014", "2015" ,"2016" ,"2017" ,"2018" ,"2019" ,"2020" ,"2021" ,"2022", "2023"};

        ArrayAdapter<String> adapter= new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, department);
        branch.setThreshold(1);
        branch.setAdapter(adapter);


        ArrayAdapter<String>  adapter1 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, year);
        passout_yr.setThreshold(1);
        passout_yr.setAdapter(adapter1);

        lay = view.findViewById(R.id.lay);
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

        submit.setOnClickListener(v-> {
            if(!name.getText().toString().trim().equals("")){
                if(!branch.getText().toString().trim().equals("")){
                    if(!passout_yr.getText().toString().trim().equals("")){
                        if(!country.getText().toString().trim().equals("")){
                            if(!state.getText().toString().trim().equals("")){
                                if(!city.getText().toString().trim().equals("")) {
                                        datasend();
                                    }
                                else{
                                    city.setError("Empty");
                                    Snackbar.make(lay,"Please Add City",Snackbar.LENGTH_LONG)
                                            .setActionTextColor(Color.parseColor("#171746"))
                                            .setTextColor(Color.parseColor("#FF7F5C"))
                                            .setBackgroundTint(Color.parseColor("#171746"))
                                            .show();
                                }
                            }
                            else{
                                state.setError("Empty");
                                Snackbar.make(lay,"Please Add State",Snackbar.LENGTH_LONG)
                                        .setActionTextColor(Color.parseColor("#171746"))
                                        .setTextColor(Color.parseColor("#FF7F5C"))
                                        .setBackgroundTint(Color.parseColor("#171746"))
                                        .show();
                            }
                        }
                        else{
                            country.setError("Empty");
                            Snackbar.make(lay,"Please Add Country",Snackbar.LENGTH_LONG)
                                    .setActionTextColor(Color.parseColor("#171746"))
                                    .setTextColor(Color.parseColor("#FF7F5C"))
                                    .setBackgroundTint(Color.parseColor("#171746"))
                                    .show();
                        }
                    }
                    else{
                        passout_yr.setError("Empty");
                        Snackbar.make(lay,"Please Add Passout Year",Snackbar.LENGTH_LONG)
                                .setActionTextColor(Color.parseColor("#171746"))
                                .setTextColor(Color.parseColor("#FF7F5C"))
                                .setBackgroundTint(Color.parseColor("#171746"))
                                .show();
                    }
                }
                else{
                    branch.setError("Empty");
                    Snackbar.make(lay,"Please Add Branch.",Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.parseColor("#171746"))
                            .setTextColor(Color.parseColor("#FF7F5C"))
                            .setBackgroundTint(Color.parseColor("#171746"))
                            .show();
                }
            }
            else{
                name.setError("Empty");
                Snackbar.make(lay,"Please Add Name.",Snackbar.LENGTH_LONG)
                        .setActionTextColor(Color.parseColor("#171746"))
                        .setTextColor(Color.parseColor("#FF7F5C"))
                        .setBackgroundTint(Color.parseColor("#171746"))
                        .show();
            }
        });

        return  view;
    }

    private void datasend(){
        reference.child("name").setValue(name.getText().toString());
        reference.child("branch").setValue(branch.getText().toString());
        reference.child("passout").setValue(passout_yr.getText().toString());
        reference.child("country").setValue(country.getText().toString());
        reference.child("state").setValue(state.getText().toString());
        reference.child("city").setValue(city.getText().toString());
        reference.child("id").setValue("Alumni");
        reference.child("phone").setValue(phone);
        reference.child("token").setValue(token);
        reference.child("uid").setValue(uid);
        reference.child("id").setValue("admin");
        reference.child("dp_link").setValue("");

        reference.child("gender").setValue("");
        reference.child("dob").setValue("");
        reference.child("bio").setValue("");
        reference.child("fb").setValue("");
        reference.child("insta").setValue("");
        reference.child("twitter").setValue("");
        reference.child("linkedin").setValue("");
        reference.child("occupation").setValue("");
        reference.child("organization").setValue("");
        reference.child("designation").setValue("");

        SharedPreferences pref = getContextNullSafety().getSharedPreferences("details?", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isDetailsFilled", true);
        editor.apply();

        Intent myIntent = new Intent(getActivity(), MainActivity.class);
        ActivityOptions options =
                ActivityOptions.makeCustomAnimation(getContextNullSafety(), R.anim.fade_in, R.anim.fade_out);
        getContextNullSafety().startActivity(myIntent, options.toBundle());
        getActivity().finish();
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