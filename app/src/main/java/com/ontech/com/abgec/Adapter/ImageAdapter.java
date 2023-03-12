package com.ontech.com.abgec.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ontech.com.abgec.Model.ImageModel;
import com.ontech.com.abgec.Model.JobModel;
import com.ontech.com.abgec.Model.alumnilistModel;
import com.ontech.com.abgec.R;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>{

    ArrayList<ImageModel> list;
    Context context;

    public ImageAdapter(Context context,ArrayList<ImageModel> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout,parent,false);
        return new ImageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, int position) {
        if (position < list.size()) {
            try {
                Uri uri = Uri.parse(list.get(position).getImagepath());
                holder.image.setImageURI(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (list.get(position).getText() != null) {
                holder.loadText.setText(list.get(position).getText());
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView image;
        TextView loadText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image  = itemView.findViewById(R.id.idIVImage);
            loadText = itemView.findViewById(R.id.loadText);
        }
    }
}