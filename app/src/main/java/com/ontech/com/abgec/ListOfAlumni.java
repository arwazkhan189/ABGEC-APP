package com.ontech.com.abgec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ontech.com.abgec.Adapter.list_adapter;
import com.ontech.com.abgec.Adapter.user_adapter;
import com.ontech.com.abgec.Model.alumnilistModel;
import com.ontech.com.abgec.Model.user_dataModel;

import java.util.ArrayList;
import java.util.Objects;

public class ListOfAlumni extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText search;
    DatabaseReference reference;
    ArrayList<alumnilistModel> list;
    ArrayList<alumnilistModel> mylist;
    ImageView loadimage,back;
    list_adapter list_adapter;
    TextView loadText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_alumni);

        Window window = ListOfAlumni.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(ListOfAlumni.this, R.color.white));

        recyclerView = findViewById(R.id.recyclerView);
        search = findViewById(R.id.input);
        list_adapter = new list_adapter(ListOfAlumni.this,list);
        reference  = FirebaseDatabase.getInstance().getReference().child("ALUMNI_DATA").child("data");
        list = new ArrayList<>();
        mylist = new ArrayList<>();
        loadimage = findViewById(R.id.loadImage);
        loadText = findViewById(R.id.loadText);
        back = findViewById(R.id.imageView4);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ListOfAlumni.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setItemViewCacheSize(500);
        recyclerView.setLayoutManager(layoutManager);

        getList();

        search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search(s+"");
            }
        });

        back.setOnClickListener(v->{
            finish();
        });


    }

    private void getList(){
        list.clear();
        mylist.clear();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    list.add(snapshot.child(Objects.requireNonNull(ds.getKey())).getValue(alumnilistModel.class));
                }

                loadimage.setVisibility(View.GONE);
                loadText.setVisibility(View.GONE);
                list_adapter = new list_adapter(ListOfAlumni.this,list);
                list_adapter.notifyDataSetChanged();
                if(recyclerView!=null)
                    recyclerView.setAdapter(list_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void search (String s) {
        mylist.clear();
        for (alumnilistModel object : list) {
            try {
                if (object.getName().toLowerCase().contains(s.toLowerCase().trim())) {
                    mylist.add(object);
                } else if (object.getBranch().toLowerCase().contains(s.toLowerCase().trim())) {
                    mylist.add(object);
                } else if (object.getPassoutYear().toLowerCase().contains(s.toLowerCase().trim())) {
                    mylist.add(object);
                }
                else if (object.getState().toLowerCase().contains(s.toLowerCase().trim())) {
                    mylist.add(object);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        list_adapter userAdapter = new list_adapter(ListOfAlumni.this, mylist);
        userAdapter.notifyDataSetChanged();
        if (recyclerView != null)
            recyclerView.setAdapter(userAdapter);
    }
}