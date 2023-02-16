package com.ontech.com.abgec;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;

import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;


public class EditProfile extends Fragment {

    View view;
    TextView personal_btn, date_txt, social_btn, occupation_btn,upload_pic;
    LinearLayout layout_personal,layout_social,layout_occupation;
    Context contextNullSafe;
    ImageView back_btn;
    int x = 0;
    int y =0;

    int z = 0;
    int check_;
    DatePickerDialog.OnDateSetListener mDateSetListener;
    private ImageView imageView;

    // Uri indicates, where the image will be picked from

    // request code
    private final int PICK_IMAGE_REQUEST = 22;

    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;

    private Uri imageUri;


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
        layout_occupation = view.findViewById(R.id.occupation_layout);
        occupation_btn = view.findViewById(R.id.occupation);
        back_btn = view.findViewById(R.id.back);
        upload_pic = view.findViewById(R.id.profile_pic);


        /*upload_pic.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              Dialog dialog = new Dialog(BasicDetails.this);
                                              dialog.setContentView(R.layout.dialog_picture_capture);
                                              Button camera = (Button) dialog.findViewById(R.id.camera);
                                              Button storage = (Button) dialog.findViewById(R.id.storage);
                                              storage.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View view) {
                                                      Intent intent = new Intent();
                                                      intent.setType("image/*");
                                                      intent.setAction(Intent.ACTION_GET_CONTENT);
                                                      resultLauncher.launch(intent);
                                                      dialog.dismiss();
                                                  }
                                              });

                                              camera.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View view) {
                                                      dexterCalling(BasicDetails.this);
                                                      dialog.dismiss();
                                                  }
                                              });
                                              Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                              dialog.show();
                                          }
                                      });
*/




        occupation_btn.setOnClickListener(v->{
            if (z==0) {
                z=1;
                layout_occupation.setVisibility(View.VISIBLE);
                Animation animSlideDown = AnimationUtils.loadAnimation(getContextNullSafety(), R.anim.slide_down);
                layout_occupation.startAnimation(animSlideDown);
            }
            else {
                z=0;
                Animation animSlideUp = AnimationUtils.loadAnimation(getContextNullSafety(), R.anim.slide_up);
                layout_occupation.startAnimation(animSlideUp);
                layout_occupation.setVisibility(View.GONE);
            }
        });

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

        back_btn.setOnClickListener(v -> {
            Toast.makeText(getContextNullSafety(), "hahaj", Toast.LENGTH_SHORT).show();
            FragmentManager fm=((FragmentActivity) getContextNullSafety()).getSupportFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            if(fm.getBackStackEntryCount()>0) {
                fm.popBackStack();
            }
            ft.commit();
        });

        OnBackPressedCallback callback=new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentManager fm=((FragmentActivity) getContextNullSafety()).getSupportFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                if(fm.getBackStackEntryCount()>0) {
                    fm.popBackStack();
                }
                ft.commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),callback);

        return view;

    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            imageUri = data.getData();
            shopImage.setImageURI(imageUri);
            shopImageUri = imageUri.toString();
            findViewById(R.id.shopcover).setVisibility(View.INVISIBLE);
        }
    }

    void dexterCalling(Activity activity)//for granting RunTime permission
    {
        //    Log.d("chekingTemp", "dexterCalling: "+temp);
        Dexter.withActivity(this)

                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_EXTERNAL_STORAGE)

                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                        ImagePicker.with(BasicDetails.this)
                                .cameraOnly()
                                .saveDir(getExternalFilesDir(Environment.DIRECTORY_DCIM))
                                .start(CAMERA_REQUEST);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                        permissionToken.continuePermissionRequest();
                    }
                }).withErrorListener(new PermissionRequestErrorListener() {

                    @Override
                    public void onError(DexterError error) {

                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })

                .onSameThread().check();
    }*/

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