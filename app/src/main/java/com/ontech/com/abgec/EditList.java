package com.ontech.com.abgec;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class EditList extends Fragment {

    View view;
    int x =0;
    Context contextNullSafe;
    String name,branch,passout,organisation,designation,number,city,state,country,email,uid,pushkey;
    EditText name_ed,branch_ed,passout_ed,organisation_ed,designation_ed,number_ed,city_ed,state_ed,country_ed,email_ed;
    TextView submit;
    ImageView back;
    DatabaseReference reference;
    Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_list, container, false);

        name_ed = view.findViewById(R.id.name);
        branch_ed = view.findViewById(R.id.country_p);
        passout_ed = view.findViewById(R.id.passout_yr);
        organisation_ed = view.findViewById(R.id.organisation);
        designation_ed = view.findViewById(R.id.designation);
        number_ed = view.findViewById(R.id.number);
        city_ed = view.findViewById(R.id.city);
        state_ed = view.findViewById(R.id.state);
        country_ed = view.findViewById(R.id.country);
        email_ed = view.findViewById(R.id.email);
        back = view.findViewById(R.id.back);
        submit  = view.findViewById(R.id.submit);


        try {
            assert getArguments() != null;
            name = getArguments().getString("name");
            branch = getArguments().getString("branch");
            passout = getArguments().getString("passout");
            organisation = getArguments().getString("organisation");
            designation = getArguments().getString("designation");
            number = getArguments().getString("number");
            city = getArguments().getString("city");
            state = getArguments().getString("state");
            country = getArguments().getString("country");
            email = getArguments().getString("email");
            pushkey = getArguments().getString("pushkey");

        } catch (Exception e) {
            e.printStackTrace();
        }

        reference = FirebaseDatabase.getInstance().getReference().child("ALUMNI_DATA").child("data").child(pushkey);

        name_ed.setText(name);
        branch_ed.setText(branch);
        passout_ed.setText(passout);
        organisation_ed.setText(organisation);
        designation_ed.setText(designation);
        number_ed.setText(number);
        city_ed.setText(city);
        country_ed.setText(country);
        state_ed.setText(state);
        email_ed.setText(email);

        submit.setOnClickListener(v->{
            dataSend();

        });

        back.setOnClickListener(v->{
            back();
        });

        return view;
    }


    private void dataSend(){
        reference.child("name").setValue(name_ed.getText().toString());
        reference.child("branch").setValue(branch_ed.getText().toString());
        reference.child("city").setValue(city_ed.getText().toString());
        reference.child("country").setValue(country_ed.getText().toString());
        reference.child("designation").setValue(designation_ed.getText().toString());
        reference.child("email").setValue(email_ed.getText().toString());
        reference.child("mobileNumber").setValue(number_ed.getText().toString());
        reference.child("organisation").setValue(organisation_ed.getText().toString());
        reference.child("passoutYear").setValue(passout_ed.getText().toString());
        reference.child("state").setValue(state_ed.getText().toString());

        dialog = new Dialog(getContextNullSafety());
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.loading);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 1000);
    }

    private void back(){
        assert getFragmentManager() != null;
        getFragmentManager().beginTransaction().remove(EditList.this).commit();
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