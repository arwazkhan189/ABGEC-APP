package com.ontech.com.abgec;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;


public class EditProfile extends Fragment {

    View view;
    TextView personal_btn, date_txt, social_btn;
    LinearLayout layout_personal,layout_social;
    Context contextNullSafe;
    int x = 0;
    int y =0;
    int check_;
    DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        layout_personal = view.findViewById(R.id.personal_layout);
        personal_btn = view.findViewById(R.id.personal_btn);
        date_txt = view.findViewById(R.id.date);
        social_btn = view.findViewById(R.id.social);
        layout_social = view.findViewById(R.id.social_links);


        social_btn.setOnClickListener(v->{
            if (y==0) {
                y=1;
                layout_social.setVisibility(View.VISIBLE);
                Animation animSlideDown = AnimationUtils.loadAnimation(getContextNullSafety(), R.anim.slide_down);
                layout_social.startAnimation(animSlideDown);
            }
            else {
                y=0;
                Animation animSlideUp = AnimationUtils.loadAnimation(getContextNullSafety(), R.anim.slide_up);
                layout_social.startAnimation(animSlideUp);
                layout_social.setVisibility(View.GONE);
            }
        });

        date_txt.setOnClickListener(v->{
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    getActivity(),
                    mDateSetListener,
                    year,month,day);
            check_=0;
            dialog.show();
        });
        
        personal_btn.setOnClickListener(v->{
            if (x==0) {
                x=1;
                layout_personal.setVisibility(View.VISIBLE);
                Animation animSlideDown = AnimationUtils.loadAnimation(getContextNullSafety(), R.anim.slide_down);
                layout_personal.startAnimation(animSlideDown);
            }
            else {
                x=0;
                Animation animSlideUp = AnimationUtils.loadAnimation(getContextNullSafety(), R.anim.slide_up);
                layout_personal.startAnimation(animSlideUp);
                layout_personal.setVisibility(View.GONE);
            }
        });

        mDateSetListener = (datePicker, year, month, day) -> {

            String d = String.valueOf(day);
            String m = String.valueOf(month + 1);
            Log.e("month", m + "");
            month = month + 1;
            Log.e("month", month + "");
            if (String.valueOf(day).length() == 1)
                d = "0" + day;
            if (String.valueOf(month).length() == 1)
                m = "0" + month;
            String date = d + "." + m + "." + year;
            if (check_ == 0) {
                date_txt.setText(date);
                //date = year + m + d;
                // fd_dot=year+"-"+m+"-"+d;
            }
        };

        return view;
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