package com.ontech.com.abgec.Adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ontech.com.abgec.Model.user_dataModel;
import com.ontech.com.abgec.Profile;
import com.ontech.com.abgec.R;

import java.util.ArrayList;

public class user_adapter extends RecyclerView.Adapter<user_adapter.ViewHolder> {

    Context context;
    ArrayList<user_dataModel> list ;

    public user_adapter(Context context,ArrayList<user_dataModel> list){
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public user_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_alumni,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull user_adapter.ViewHolder holder, int position) {



        if (position < list.size()) {
            try {
                Uri uri = Uri.parse(list.get(position).getDp_link());
                holder.image.setImageURI(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.name.setText(list.get(position).getName());
            holder.branch.setText(list.get(position).getBranch());
            holder.year.setText(list.get(position).getPassout());

            holder.layout.setOnClickListener(v->{
                Profile profile = new Profile();
                Bundle args = new Bundle();
                args.putString("sending_user_from_sync","addstack");
                args.putString("uid_sending_profile", list.get(position).getUid());
                profile.setArguments(args);
                ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .add(R.id.container, profile)
                        .addToBackStack(null)
                        .commit();
            });
        }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView image;
        TextView name;
        TextView branch;
        TextView year;
        LinearLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.textView5);
            branch = itemView.findViewById(R.id.textView6);
            year = itemView.findViewById(R.id.textView7);
            image = itemView.findViewById(R.id.my_image_view);
            layout = itemView.findViewById(R.id.layout);
        }
    }
}
