package com.ontech.com.abgec;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
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
import com.ontech.com.abgec.Adapter.user_adapter;
import com.ontech.com.abgec.Model.user_dataModel;

import java.util.ArrayList;
import java.util.Objects;

import me.ibrahimsn.lib.SmoothBottomBar;


public class AlumniList extends Fragment {

    View view;
    Context contextNullSafe;
    DatabaseReference reference;
    RecyclerView recyclerView;
    EditText search;
    ArrayList<user_dataModel> list;
    ArrayList<user_dataModel> mylist;
    LottieAnimationView loadimage;
    TextView loadText;

    SmoothBottomBar smoothBottomBar;
    SwipeRefreshLayout mSwipeRefreshLayout;
    user_adapter userAdapter;
    FirebaseUser user;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_alumni_list, container, false);

        if (contextNullSafe == null) getContextNullSafety();
//Hide the keyboard
        requireActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_layout);
        list=new ArrayList<>();
        mylist=new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("users");
        loadimage = view.findViewById(R.id.loadImage);
        loadText = view.findViewById(R.id.loadText);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContextNullSafety());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setItemViewCacheSize(500);
        recyclerView.setLayoutManager(layoutManager);

        search=view.findViewById(R.id.input);
        smoothBottomBar=requireActivity().findViewById(R.id.bottomBar);
        smoothBottomBar.setItemActiveIndex(2);

        Fresco.initialize(
                getContextNullSafety(),
                ImagePipelineConfig.newBuilder(getContextNullSafety())
                        .setMemoryChunkType(MemoryChunkType.BUFFER_MEMORY)
                        .setImageTranscoderType(ImageTranscoderType.JAVA_TRANSCODER)
                        .experiment().setNativeCodeDisabled(true)
                        .build());




        getAlumnis();

        mSwipeRefreshLayout.setOnRefreshListener(this::getAlumnis);
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

        search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search(s+"");
            }
        });

        return view;
    }

    private void getAlumnis() {
        mylist.clear();
        list.clear();
        mSwipeRefreshLayout.setRefreshing(true);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (Objects.equals(snapshot.child(Objects.requireNonNull(ds.getKey())).child("id").getValue(String.class), "Alumni")) {
                        list.add(snapshot.child(Objects.requireNonNull(ds.getKey())).getValue(user_dataModel.class));
                    }
                }
                mSwipeRefreshLayout.setRefreshing(false);
                loadimage.setVisibility(View.GONE);
                loadText.setVisibility(View.GONE);
                userAdapter = new user_adapter(contextNullSafe, list);
                userAdapter.notifyDataSetChanged();
                if (recyclerView != null)
                    recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

        @SuppressLint("NotifyDataSetChanged")
        private void search (String s) {
            mylist.clear();
            for (user_dataModel object : list) {
                try {
                    if (object.getName().toLowerCase().contains(s.toLowerCase().trim())) {
                        mylist.add(object);
                    } else if (object.getBranch().toLowerCase().contains(s.toLowerCase().trim())) {
                        mylist.add(object);
                    } else if (object.getPassout().toLowerCase().contains(s.toLowerCase().trim())) {
                        mylist.add(object);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            user_adapter userAdapter = new user_adapter(getContextNullSafety(), mylist);
            userAdapter.notifyDataSetChanged();
            if (recyclerView != null)
                recyclerView.setAdapter(userAdapter);
        }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        contextNullSafe = context;
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