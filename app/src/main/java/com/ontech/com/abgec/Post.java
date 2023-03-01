package com.ontech.com.abgec;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import me.ibrahimsn.lib.SmoothBottomBar;


public class Post extends Fragment {


    View view;
    SmoothBottomBar smoothBottomBar;
    Context contextNullSafe;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_post, container, false);
        smoothBottomBar = view.findViewById(R.id.bottomBar);

        if (contextNullSafe == null) getContextNullSafety();

        smoothBottomBar= requireActivity().findViewById(R.id.bottomBar);
        smoothBottomBar.setItemActiveIndex(0);



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