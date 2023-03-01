package com.ontech.com.abgec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
    EditText editText;
    DatabaseReference reference;
    ArrayList<alumnilistModel> list;
    ImageView loadimage;
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
        editText = findViewById(R.id.input);
        list_adapter = new list_adapter(ListOfAlumni.this,list);
        reference  = FirebaseDatabase.getInstance().getReference().child("ALUMNI_DATA").child("data");
        list = new ArrayList<>();

        loadimage = findViewById(R.id.loadImage);
        loadText = findViewById(R.id.loadText);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ListOfAlumni.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setItemViewCacheSize(500);
        recyclerView.setLayoutManager(layoutManager);

        getList();

    }

    private void getList(){
        list.clear();
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
}