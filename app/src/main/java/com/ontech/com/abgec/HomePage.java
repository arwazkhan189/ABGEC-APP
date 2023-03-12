package com.ontech.com.abgec;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ontech.com.abgec.Adapter.JobAdapter;
import com.ontech.com.abgec.Adapter.list_adapter;
import com.ontech.com.abgec.Model.JobModel;
import com.ontech.com.abgec.Model.alumnilistModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import me.ibrahimsn.lib.SmoothBottomBar;


public class HomePage extends Fragment {
    View view;
    FirebaseUser user;
    FirebaseAuth auth;
    DatabaseReference reference;
    SmoothBottomBar smoothBottomBar;
    SwipeRefreshLayout mSwipeRefreshLayout;
    LottieAnimationView load;
    TextView loadText;

    EditText search;
    ArrayList<JobModel> list=new ArrayList<>();
    ArrayList<JobModel> mylist=new ArrayList<>();
    JobAdapter jobAdapter;
    RecyclerView recyclerView;
    Context contextNullSafe;
    String value = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home_page, container, false);

        smoothBottomBar = view.findViewById(R.id.bottomBar);

        if (contextNullSafe == null) getContextNullSafety();

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_layout);
        smoothBottomBar=getActivity().findViewById(R.id.bottomBar);
        smoothBottomBar.setItemActiveIndex(1);

        search = view.findViewById(R.id.input);
        requireActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        recyclerView = view.findViewById(R.id.recyclerView);

        reference = FirebaseDatabase.getInstance().getReference().child("jobs");

        load = view.findViewById(R.id.loadImage);
        loadText = view.findViewById(R.id.loadText);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContextNullSafety());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setItemViewCacheSize(500);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setLayoutManager(layoutManager);


        if (check_for_admin()){
            value = "Admin";
            get_data(value);
        }
        else {
            value = "noAdmin";
            get_data(value);
        }


        search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search(s+"");
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(this::get_data1);

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


        return view;
    }

    private void get_data1() {

        mSwipeRefreshLayout.setRefreshing(true);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    list.clear();
                    mylist.clear();
                    mSwipeRefreshLayout.setRefreshing(false);
                    load.setVisibility(View.GONE);
                    loadText.setVisibility(View.GONE);
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        list.add(snapshot.child(Objects.requireNonNull(ds.getKey())).getValue(JobModel.class));
                    }

                    Collections.reverse(list);
                    jobAdapter = new JobAdapter(list, getContextNullSafety(),value);
                    jobAdapter.notifyDataSetChanged();
                    if (recyclerView != null)
                        recyclerView.setAdapter(jobAdapter);
                } else {
                    load.setVisibility(View.VISIBLE);
                    loadText.setVisibility(View.VISIBLE);
                   /* mSwipeRefreshLayout.setRefreshing(false);
                    progressBar.setVisibility(View.VISIBLE);*/

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void get_data(String isAdmin) {

            mSwipeRefreshLayout.setRefreshing(true);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        list.clear();
                        mylist.clear();
                        mSwipeRefreshLayout.setRefreshing(false);
                        load.setVisibility(View.GONE);
                        loadText.setVisibility(View.GONE);
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            list.add(snapshot.child(Objects.requireNonNull(ds.getKey())).getValue(JobModel.class));
                        }

                        Collections.reverse(list);
                        jobAdapter = new JobAdapter(list, getContextNullSafety(),value);
                        jobAdapter.notifyDataSetChanged();
                        if (recyclerView != null)
                            recyclerView.setAdapter(jobAdapter);
                    } else {
                        load.setVisibility(View.VISIBLE);
                        loadText.setVisibility(View.VISIBLE);
                   /* mSwipeRefreshLayout.setRefreshing(false);
                    progressBar.setVisibility(View.VISIBLE);*/

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

    private boolean check_for_admin(){
        SharedPreferences pref = getContextNullSafety().getSharedPreferences("our_user?", MODE_PRIVATE);
        return pref.getBoolean("admin", true);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void search (String s) {
        mylist.clear();
        for (JobModel object : list) {
            try {
                if (object.getCompany().toLowerCase().contains(s.toLowerCase().trim())) {
                    mylist.add(object);
                } else if (object.getJobTitle().toLowerCase().contains(s.toLowerCase().trim())) {
                    mylist.add(object);
                } else if (object.getJoblocation().toLowerCase().contains(s.toLowerCase().trim())) {
                    mylist.add(object);
                }
                else if (object.getJobType().toLowerCase().contains(s.toLowerCase().trim())) {
                    mylist.add(object);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        JobAdapter userAdapter = new JobAdapter(mylist,getContextNullSafety(),value);
        userAdapter.notifyDataSetChanged();
        if (recyclerView != null)
            recyclerView.setAdapter(userAdapter);
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
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        contextNullSafe = context;
    }

}