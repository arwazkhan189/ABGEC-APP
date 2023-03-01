package com.ontech.com.abgec.Adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ontech.com.abgec.Model.JobModel;
import com.ontech.com.abgec.R;

import java.util.ArrayList;
import java.util.List;

public class jobAdapter extends RecyclerView.Adapter<jobAdapter.ViewHolder> {

    List<JobModel> list;

    Context context;
    FirebaseAuth auth;
    FirebaseUser user;
    View view;
    Boolean click=true;
    List<String> x;
    DatabaseReference reference;


    public jobAdapter(List<JobModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public jobAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_job,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull jobAdapter.ViewHolder holder, int position) {


        x = new ArrayList<>();
        x.add("b");
        x.add("c");
        System.out.println(x.add("a"));
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("jobs");

        if (position < list.size()) {
            try {
                Uri uri = Uri.parse(list.get(position).getImageLink());
                holder.image.setImageURI(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.company.setText(list.get(position).getCompany());
            holder.jobTitle.setText(list.get(position).getJobTitle());
            holder.jobLocation.setText(list.get(position).getJoblocation());
            holder.jobType.setText(list.get(position).getJobType());
            holder.job_function.setText(list.get(position).getJobFunction());
            holder.job_mode.setText(list.get(position).getJobMode());
            holder.salary.setText(list.get(position).getSalary());
            holder.level.setText(list.get(position).getExperience());
        }

        if (list.get(position).getUid().equals(user.getUid())){
            holder.delete.setVisibility(View.VISIBLE);

            holder.delete.setOnClickListener(v->{
                reference.child(list.get(position).getPushkey()).removeValue();
            });
        }

        holder.layout.setOnClickListener(v->{
            if (click) {
                holder.job_function.setVisibility(View.VISIBLE);
                holder.job_mode.setVisibility(View.VISIBLE);
                holder.salary.setVisibility(View.VISIBLE);
                holder.level.setVisibility(View.VISIBLE);
                click = false;
            }
            else {
                holder.job_function.setVisibility(View.GONE);
                holder.job_mode.setVisibility(View.GONE);
                holder.salary.setVisibility(View.GONE);
                holder.level.setVisibility(View.GONE);
                click = true;
            }
        });

        holder.apply.setOnClickListener(v->{
            String url = list.get(position).getUrl();
            Intent i = new Intent(Intent.ACTION_VIEW);
              i.setData(Uri.parse(url));
            view.getContext().startActivity(i);
        });

        holder.share.setOnClickListener(v->{
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Download App");
            String message = "Company Name - " + list.get(position).getCompany() +"\n" + "Tijob - " + list.get(position).getJobTitle() +"\n" +
                    "Location - " + list.get(position).getJoblocation()  + "Mode - " + list.get(position).getJobMode() +"\n" + "Salary - " + list.get(position).getSalary() +"\n" +
                    "Function - " + list.get(position).getJobFunction() + "Experience Level - " + list.get(position).getExperience() +"\n" + "Type - " + list.get(position).getJobType() +
                    "\n\n⭐ ABGEC ⭐";
            intent.putExtra(Intent.EXTRA_TEXT, message);
            view.getContext().startActivity(Intent.createChooser(intent, "Share using"));
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView company,jobType,jobTitle,jobLocation,apply,job_function,job_mode,salary,level;
        SimpleDraweeView image;
        ImageView delete,share;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            company = itemView.findViewById(R.id.company_name);
            jobType = itemView.findViewById(R.id.job_type);
            jobTitle = itemView.findViewById(R.id.job_title);
            jobLocation = itemView.findViewById(R.id.job_location);
            image = itemView.findViewById(R.id.job_img);
            layout = itemView.findViewById(R.id.job_layout);
            apply = itemView.findViewById(R.id.apply);
            job_function = itemView.findViewById(R.id.job_function);
            job_mode = itemView.findViewById(R.id.job_mode);
            salary = itemView.findViewById(R.id.salary);
            level = itemView.findViewById(R.id.level);
            delete = itemView.findViewById(R.id.delete);
            share = itemView.findViewById(R.id.share);
        }
    }

}
