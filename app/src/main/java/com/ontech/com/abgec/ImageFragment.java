package com.ontech.com.abgec;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ontech.com.abgec.Adapter.ImageAdapter;
import com.ontech.com.abgec.Adapter.JobAdapter;
import com.ontech.com.abgec.Model.ImageModel;
import com.ontech.com.abgec.Model.JobModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ImageFragment extends Fragment {

    View view;
    ArrayList<ImageModel> list =  new ArrayList<>();
    DatabaseReference reference;
    RecyclerView recyclerView;

    LottieAnimationView loadimage;
    TextView loadText;
    int counter=0;
    Context contextNullSafe;
    ImageAdapter imageRVAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_image, container, false);

        if (contextNullSafe == null) getContextNullSafety();

        reference = FirebaseDatabase.getInstance().getReference().child("Gallery");

        recyclerView = view.findViewById(R.id.idRVImages);

        loadimage = view.findViewById(R.id.loadImage);
        loadText = view.findViewById(R.id.loadText);

        prepareRecyclerView();

        GridLayoutManager layoutManager = new GridLayoutManager(getContextNullSafety(),3);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setItemViewCacheSize(500);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    private void prepareRecyclerView() {

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    list.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                       //Log.e("CheckValue", String.valueOf(snapshot.child(Objects.requireNonNull(ds.getKey())).child("imagepath").getValue(String.class)));
                        list.add(snapshot.child(Objects.requireNonNull(ds.getKey())).getValue(ImageModel.class));
                    }
                    Collections.reverse(list);
                    loadimage.setVisibility(View.GONE);
                    loadText.setVisibility(View.GONE);
                    imageRVAdapter = new ImageAdapter(getContextNullSafety(),list);
                    imageRVAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(imageRVAdapter);
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