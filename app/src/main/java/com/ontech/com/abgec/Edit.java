package com.ontech.com.abgec;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImageTranscoderType;
import com.facebook.imagepipeline.core.MemoryChunkType;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import www.sanju.motiontoast.MotionToast;

public class Edit extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ImageView imageView;

    // Uri indicates, where the image will be picked from

    // request code
    private final int PICK_IMAGE_REQUEST = 22;
    String selectedImagePath="";
    public ActivityResultLauncher<Intent> resultLauncher;
    private String shopImageUri;
    private Uri imageUri;
    private SimpleDraweeView shopImage;
    Dialog dialog;
    TextView personal_btn, date_txt, social_btn, occupation_btn, male, female, submit;
    LinearLayout layout_personal, layout_social, layout_occupation;
    Context contextNullSafe;
    ImageView back_btn;
    int x = 0;
    int y = 0;
    LinearLayout upload_pic;
    ConstraintLayout lay;
    int z = 0;
    int check_;
    ArrayAdapter ad;
    DatePickerDialog.OnDateSetListener mDateSetListener;
    private static final int CAMERA_REQUEST = 1888;
    public final int PERMISSION_REQUEST_CODE = 1001;
    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;
    String gender = "";

    EditText bio, fb, twitter, linkidin, insta, organiztion, designation, year, name, state, country, city;
    AutoCompleteTextView branch, passout_yr;
    TextView gend;

    LottieAnimationView animation;
    String[] values = {"Service", "Self-Employed", "Retired", "Other"};
    Spinner spino;

    String gen, dateOfBirth, bioo, fcb, twt, lin, inst, occup, organ, desig, nam, br, py, countr, stat, cit, dp_uri;

    public static final int REQUEST_CODE_STORAGE_PERMISSION = 1;

    private static final int pic_id = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Window window = Edit.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(Edit.this, R.color.white));

        spino = findViewById(R.id.spinner);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        upload_pic = findViewById(R.id.profile_pic);
        gend = findViewById(R.id.gender);
        shopImage = findViewById(R.id.image);
        layout_personal = findViewById(R.id.personal_layout);
        personal_btn = findViewById(R.id.personal_btn);
        date_txt = findViewById(R.id.date);
        social_btn = findViewById(R.id.social);
        layout_social = findViewById(R.id.social_links);
        layout_occupation = findViewById(R.id.occupation_layout);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        occupation_btn = findViewById(R.id.occupation);
        back_btn = findViewById(R.id.back);
        //year = findViewById(R.id.)
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        //occupation = findViewById(R.id.occupation_edt);
        lay = findViewById(R.id.lay1);
        //Editext
        submit = findViewById(R.id.submit);
        name = findViewById(R.id.name);
        state = findViewById(R.id.state);
        city = findViewById(R.id.city);
        country = findViewById(R.id.country);
        //mobile_no = view.findViewById(R.id.mobile_no);
        branch = findViewById(R.id.country_p);
        passout_yr = findViewById(R.id.passout_yr);
        organiztion = findViewById(R.id.company);
        designation = findViewById(R.id.designation);
        insta = findViewById(R.id.instagram);
        linkidin = findViewById(R.id.linkedin);
        fb = findViewById(R.id.facebook);
        twitter = findViewById(R.id.twitter);
        bio = findViewById(R.id.bio);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //spinner
        spino.setOnItemSelectedListener(this);
        ad = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                values);
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);
        spino.setAdapter(ad);



        //ValueGetting();
        valueGetting();

        Fresco.initialize(
                Edit.this,
                ImagePipelineConfig.newBuilder(Edit.this)
                        .setMemoryChunkType(MemoryChunkType.BUFFER_MEMORY)
                        .setImageTranscoderType(ImageTranscoderType.JAVA_TRANSCODER)
                        .experiment().setNativeCodeDisabled(true)
                        .build());

        submit.setOnClickListener(v -> {

            if (!name.getText().toString().trim().equals("")) {
                if (!branch.getText().toString().trim().equals("")) {
                    if (!passout_yr.getText().toString().trim().equals("")) {
                        if (!country.getText().toString().trim().equals("")) {
                            if (!state.getText().toString().trim().equals("")) {
                                if (!city.getText().toString().trim().equals("")) {
                                    if (!gender.equals("")) {
                                        if (!date_txt.getText().toString().trim().equals("")) {
                                            uploadImage();
                                        } else {
                                            date_txt.setError("Empty");
                                            layout_personal.setVisibility(View.VISIBLE);
                                            Snackbar.make(lay, "Please Add Your Date of Birth", Snackbar.LENGTH_LONG)
                                                    .setActionTextColor(Color.parseColor("#171746"))
                                                    .setTextColor(Color.parseColor("#FF7F5C"))
                                                    .setBackgroundTint(Color.parseColor("#171746"))
                                                    .show();
                                        }
                                    } else {
                                        gend.setError("Empty");
                                        layout_personal.setVisibility(View.VISIBLE);
                                        Snackbar.make(lay, "Please Select Your Gender", Snackbar.LENGTH_LONG)
                                                .setActionTextColor(Color.parseColor("#171746"))
                                                .setTextColor(Color.parseColor("#FF7F5C"))
                                                .setBackgroundTint(Color.parseColor("#171746"))
                                                .show();
                                    }
                                } else {
                                    city.setError("Empty");
                                    Snackbar.make(lay, "Please Add City", Snackbar.LENGTH_LONG)
                                            .setActionTextColor(Color.parseColor("#171746"))
                                            .setTextColor(Color.parseColor("#FF7F5C"))
                                            .setBackgroundTint(Color.parseColor("#171746"))
                                            .show();
                                }
                            } else {
                                state.setError("Empty");
                                Snackbar.make(lay, "Please Add State", Snackbar.LENGTH_LONG)
                                        .setActionTextColor(Color.parseColor("#171746"))
                                        .setTextColor(Color.parseColor("#FF7F5C"))
                                        .setBackgroundTint(Color.parseColor("#171746"))
                                        .show();
                            }
                        } else {
                            country.setError("Empty");
                            Snackbar.make(lay, "Please Add Country", Snackbar.LENGTH_LONG)
                                    .setActionTextColor(Color.parseColor("#171746"))
                                    .setTextColor(Color.parseColor("#FF7F5C"))
                                    .setBackgroundTint(Color.parseColor("#171746"))
                                    .show();
                        }
                    } else {
                        passout_yr.setError("Empty");
                        Snackbar.make(lay, "Please Add Passout Year", Snackbar.LENGTH_LONG)
                                .setActionTextColor(Color.parseColor("#171746"))
                                .setTextColor(Color.parseColor("#FF7F5C"))
                                .setBackgroundTint(Color.parseColor("#171746"))
                                .show();
                    }
                } else {
                    branch.setError("Empty");
                    Snackbar.make(lay, "Please Add Branch.", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.parseColor("#171746"))
                            .setTextColor(Color.parseColor("#FF7F5C"))
                            .setBackgroundTint(Color.parseColor("#171746"))
                            .show();
                }
            } else {
                name.setError("Empty");
                Snackbar.make(lay, "Please Add Name.", Snackbar.LENGTH_LONG)
                        .setActionTextColor(Color.parseColor("#171746"))
                        .setTextColor(Color.parseColor("#FF7F5C"))
                        .setBackgroundTint(Color.parseColor("#171746"))
                        .show();
            }
        });


        occupation_btn.setOnClickListener(v -> {
            if (z == 0) {
                z = 1;
                layout_occupation.setVisibility(View.VISIBLE);
                Animation animSlideDown = AnimationUtils.loadAnimation(Edit.this, R.anim.slide_down);
                layout_occupation.startAnimation(animSlideDown);
            } else {
                z = 0;
                Animation animSlideUp = AnimationUtils.loadAnimation(Edit.this, R.anim.slide_up);
                layout_occupation.startAnimation(animSlideUp);
                layout_occupation.setVisibility(View.GONE);
            }
        });

        social_btn.setOnClickListener(v -> {
            if (y == 0) {
                y = 1;
                layout_social.setVisibility(View.VISIBLE);
                Animation animSlideDown = AnimationUtils.loadAnimation(Edit.this, R.anim.slide_down);
                layout_social.startAnimation(animSlideDown);
            } else {
                y = 0;
                Animation animSlideUp = AnimationUtils.loadAnimation(Edit.this, R.anim.slide_up);
                layout_social.startAnimation(animSlideUp);
                layout_social.setVisibility(View.GONE);
            }
        });

        date_txt.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    Edit.this,
                    mDateSetListener,
                    year, month, day);
            check_ = 0;
            dialog.show();
        });


        personal_btn.setOnClickListener(v -> {
            if (x == 0) {
                x = 1;
                layout_personal.setVisibility(View.VISIBLE);
                Animation animSlideDown = AnimationUtils.loadAnimation(Edit.this, R.anim.slide_down);
                layout_personal.startAnimation(animSlideDown);
            } else {
                x = 0;
                Animation animSlideUp = AnimationUtils.loadAnimation(Edit.this, R.anim.slide_up);
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
            finish();
        });

        upload_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Ask for permission
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Edit.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);
                } else {
                       /* Dialog dialog = new Dialog(Edit.this);
                        dialog.setContentView(R.layout.dialog_picture_capture);
                        TextView camera = dialog.findViewById(R.id.camera);
                        TextView storage = dialog.findViewById(R.id.storage);
                        storage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {*/
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    resultLauncher.launch(intent);
                    //dialog.dismiss();
                }
                /*        });

                        camera.setOnClickListener(new View.OnClickListener() {
                                                      @Override
                                                      public void onClick(View view) {
                                dexterCalling(Edit.this);
                                dialog.dismiss();
                                                          Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                          // Start the activity with camera_intent, and request pic id
                                                          startActivityForResult(camera_intent, pic_id);
                                                      }
                                                  });


                        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    }
            }*/
            }
        });

        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                if (result.getData() != null) {
                    imageUri = result.getData().getData();
                    shopImage.setVisibility(View.VISIBLE);
                    shopImageUri = imageUri.toString();
                    shopImage.setImageURI(imageUri);
                    Log.e("check_uri",imageUri.toString());
                    addImageNote(imageUri);
                }
            }
        });
    }

    private void valueGetting() {

        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    gen = snapshot.child("gender").getValue(String.class);
                    dateOfBirth = snapshot.child("dob").getValue(String.class);
                    bioo = snapshot.child("bio").getValue(String.class);
                    fcb = snapshot.child("fb").getValue(String.class);
                    inst = snapshot.child("insta").getValue(String.class);
                    lin = snapshot.child("linkedin").getValue(String.class);
                    twt = snapshot.child("twitter").getValue(String.class);
                    occup = snapshot.child("occupation").getValue(String.class);
                    organ = snapshot.child("organization").getValue(String.class);
                    desig = snapshot.child("designation").getValue(String.class);
                    nam = snapshot.child("name").getValue(String.class);
                    br = snapshot.child("branch").getValue(String.class);
                    py = snapshot.child("passout").getValue(String.class);
                    countr = snapshot.child("country").getValue(String.class);
                    stat = snapshot.child("state").getValue(String.class);
                    cit = snapshot.child("city").getValue(String.class);
                    dp_uri = snapshot.child("dp_link").getValue(String.class);

                    male.setOnClickListener(v -> {
                        male.setBackgroundResource(R.drawable.bg_selector);
                        female.setBackgroundResource(R.drawable.bg_male);
                        gender = "Male";
                    });

                    if (!dp_uri.equals("")) {
                        shopImage.setVisibility(View.VISIBLE);
                        try {
                            Uri uri = Uri.parse(dp_uri);
                            shopImage.setImageURI(uri);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    female.setOnClickListener(v -> {
                        female.setBackgroundResource(R.drawable.bg_selector);
                        male.setBackgroundResource(R.drawable.bg_male);
                        gender = "Female";
                    });

                    //setting values
                    if (gen.equals("Male")) {
                        male.setBackgroundResource(R.drawable.bg_selector);
                        female.setBackgroundResource(R.drawable.bg_male);
                        gender = "Male";
                    } else {
                        female.setBackgroundResource(R.drawable.bg_selector);
                        male.setBackgroundResource(R.drawable.bg_male);
                        gender = "Male";
                    }

                    date_txt.setText(dateOfBirth);
                    bio.setText(bioo);
                    fb.setText(fcb);
                    twitter.setText(twt);
                    linkidin.setText(lin);
                    insta.setText(inst);
                    organiztion.setText(organ);
                    designation.setText(desig);
                    name.setText(nam);
                    branch.setText(br);
                    passout_yr.setText(py);
                    country.setText(countr);
                    state.setText(stat);
                    city.setText(cit);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ValueGetting(){
        Intent intent = getIntent();
        nam =  intent.getStringExtra("name");
        dateOfBirth =  intent.getStringExtra("dob");
        gen =  intent.getStringExtra("gender");
        bioo =  intent.getStringExtra("bio");
        fcb =  intent.getStringExtra("fb");
        twt =  intent.getStringExtra("twitter");
        lin =  intent.getStringExtra("linkedin");
        inst =  intent.getStringExtra("instagram");
        occup =  intent.getStringExtra("occupation");
        organ =  intent.getStringExtra("organisation");
        desig =  intent.getStringExtra("designation");
        br =  intent.getStringExtra("branch");
        py =  intent.getStringExtra("passout_yr");
        countr =  intent.getStringExtra("country");
        stat =  intent.getStringExtra("state");
        cit =  intent.getStringExtra("city");
        dp_uri =  intent.getStringExtra("dp_link");

        male.setOnClickListener(v -> {
            male.setBackgroundResource(R.drawable.bg_selector);
            female.setBackgroundResource(R.drawable.bg_male);
            gender = "Male";
        });


        female.setOnClickListener(v -> {
            female.setBackgroundResource(R.drawable.bg_selector);
            male.setBackgroundResource(R.drawable.bg_male);
            gender = "Female";
        });

        //setting values
        Log.e("checking_gender",gen);
        if (!gen.equals("")) {
            if (gen.equals("Male")) {
                male.setBackgroundResource(R.drawable.bg_selector);
                female.setBackgroundResource(R.drawable.bg_male);
            } else {
                female.setBackgroundResource(R.drawable.bg_selector);
                male.setBackgroundResource(R.drawable.bg_male);
            }
            gender = "Male";
        }

        if (!dp_uri.equals("")){
            try {
                Uri uri = Uri.parse(dp_uri);
                shopImage.setImageURI(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        date_txt.setText(dateOfBirth);
        bio.setText(bioo);
        fb.setText(fcb);
        twitter.setText(twt);
        linkidin.setText(lin);
        insta.setText(inst);
        organiztion.setText(organ);
        designation.setText(desig);
        name.setText(nam);
        branch.setText(br);
        passout_yr.setText(py);
        country.setText(countr);
        state.setText(stat);
        city.setText(cit);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }




    private void uploadImage()
    {
        if (!selectedImagePath.equals("")) {

            // Code for showing progressDialog while uploading
           /* ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();*/

            //animation.setVisibility(View.VISIBLE);

                //if image is there...
            dialog = new Dialog(Edit.this);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.loading);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();


                String pushkey = reference.push().getKey();


                //for image storing
                String imagepath = "Profile/" + name.getText().toString().trim() + pushkey + ".png";

                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(imagepath);
                            /*final String randomKey = UUID.randomUUID().toString();
                            BitmapDrawable drawable = (BitmapDrawable) imageNote.getDrawable();
                            Bitmap bitmap_up = drawable.getBitmap();
                            String path = MediaStore.Images.Media.insertImage(requireContext().getApplicationContext().getContentResolver(), bitmap_up, "" + randomKey, null);*/

                try {
                    InputStream stream = new FileInputStream(new File(selectedImagePath));

                    storageReference.putStream(stream)
                            .addOnSuccessListener(taskSnapshot ->
                                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(
                                            task -> {
                                                String image_link = Objects.requireNonNull(task.getResult()).toString();
                                                reference. child("dp_link").setValue(image_link);

                                                /*MotionToast.Companion.darkColorToast(Edit.this,
                                                        "Posted Successfully!!",
                                                        "Hurray\uD83C\uDF89\uD83C\uDF89",
                                                        MotionToast.TOAST_SUCCESS,
                                                        MotionToast.GRAVITY_BOTTOM,
                                                        MotionToast.LONG_DURATION,
                                                        ResourcesCompat.getFont(Edit.this, R.font.lexend));*/
                                                //back();
                                            }));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(imageUri)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    dialog.dismiss();
                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    //progressDialog.dismiss();
                                   // animation.setVisibility(View.GONE);
                                    finish();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            //animation.setVisibility(View.GONE);

                            Toast
                                    .makeText(Edit.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
                    /*.addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });*/
        }
        else {
            dataSend();
            dialog = new Dialog(Edit.this);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.loading);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                    finish();
                }
            }, 1000);

        }
    }

    private void dataSend() {



        //  animation.setVisibility(View.GONE);
        occup = spino.getSelectedItem().toString();

        //uploadImage();
        reference.child("gender").setValue(gender);
        reference.child("dob").setValue(date_txt.getText().toString());
        reference.child("bio").setValue(bio.getText().toString());
        reference.child("fb").setValue(fb.getText().toString());
        reference.child("insta").setValue(insta.getText().toString());
        reference.child("twitter").setValue(twitter.getText().toString());
        reference.child("linkedin").setValue(linkidin.getText().toString());
        reference.child("occupation").setValue(occup);
        reference.child("organization").setValue(organiztion.getText().toString());
        reference.child("designation").setValue(designation.getText().toString());

        //reference.child("year").setValue(year.getText().toString());
        reference.child("name").setValue(name.getText().toString());
        reference.child("branch").setValue(branch.getText().toString());
        reference.child("passout").setValue(passout_yr.getText().toString());
        reference.child("country").setValue(country.getText().toString());
        reference.child("state").setValue(state.getText().toString());
        reference.child("city").setValue(city.getText().toString());



    }


    private void addImageNote(Uri imageUri) {

        shopImage.setVisibility(View.VISIBLE);
        selectedImagePath = compressImage(imageUri + "");
        shopImage.setImageBitmap(BitmapFactory.decodeFile(selectedImagePath));
        //findViewById(R.id.imageRemoveImage).setVisibility(View.VISIBLE);

    }


    private void back(){
        if(((FragmentActivity) Edit.this).getSupportFragmentManager().findFragmentById(R.id.drawer) != null) {
            ((FragmentActivity) Edit.this).getSupportFragmentManager()
                    .beginTransaction().
                    remove(Objects.requireNonNull(((FragmentActivity) Edit.this).getSupportFragmentManager().findFragmentById(R.id.drawer))).commit();
        }
        ((FragmentActivity) Edit.this).getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container,new Profile())
                .commit();
    }

    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(Uri.parse(imageUri),Edit.this);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight+1;
        int actualWidth = options.outWidth+1;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            assert scaledBitmap != null;
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Edit.this.getExternalFilesDir(null).getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");

    }

    private static String getRealPathFromURI(Uri uri, Context context) {
        Uri returnUri = uri;
        Cursor returnCursor = context.getContentResolver().query(returnUri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));
        File file = new File(context.getFilesDir(), name);
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1 * 1024 * 1024;
            int bytesAvailable = inputStream.available();

            //int bufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            Log.e("File Size", "Size " + file.length());
            inputStream.close();
            outputStream.close();
            Log.e("File Path", "Path " + file.getPath());
            Log.e("File Size", "Size " + file.length());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return file.getPath();
    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            Log.e("column",index+"");
            return cursor.getString(index)+"";
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = Math.min(heightRatio, widthRatio);
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }


}


    /*private void valueGetting() {

        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    gen = snapshot.child("gender").getValue(String.class);
                    dateOfBirth = snapshot.child("dob").getValue(String.class);
                    bioo = snapshot.child("bio").getValue(String.class);
                    fcb = snapshot.child("fb").getValue(String.class);
                    inst = snapshot.child("insta").getValue(String.class);
                    lin = snapshot.child("linedin").getValue(String.class);
                    twt = snapshot.child("twitter").getValue(String.class);
                    occup = snapshot.child("occupation").getValue(String.class);
                    organ = snapshot.child("organization").getValue(String.class);
                    desig = snapshot.child("designation").getValue(String.class);
                    nam = snapshot.child("name").getValue(String.class);
                    br = snapshot.child("branch").getValue(String.class);
                    py = snapshot.child("passout").getValue(String.class);
                    countr = snapshot.child("country").getValue(String.class);
                    stat = snapshot.child("state").getValue(String.class);
                    cit = snapshot.child("city").getValue(String.class);
                    dp_uri = snapshot.child("dp_link").getValue(String.class);

                    male.setOnClickListener(v -> {
                        male.setBackgroundResource(R.drawable.bg_selector);
                        female.setBackgroundResource(R.drawable.bg_male);
                        gender = "Male";
                    });

                    if (!dp_uri.equals("")) {
                        shopImage.setVisibility(View.VISIBLE);
                    }

                    female.setOnClickListener(v -> {
                        female.setBackgroundResource(R.drawable.bg_selector);
                        male.setBackgroundResource(R.drawable.bg_male);
                        gender = "Female";
                    });

                    //setting values
                    if (gen.equals("Male")) {
                        male.setBackgroundResource(R.drawable.bg_selector);
                        female.setBackgroundResource(R.drawable.bg_male);
                        gender = "Male";
                    } else {
                        female.setBackgroundResource(R.drawable.bg_selector);
                        male.setBackgroundResource(R.drawable.bg_male);
                        gender = "Male";
                    }

                    date_txt.setText(dateOfBirth);
                    bio.setText(bioo);
                    fb.setText(fcb);
                    twitter.setText(twt);
                    linkidin.setText(lin);
                    insta.setText(inst);
                    organiztion.setText(organ);
                    designation.setText(desig);
                    name.setText(nam);
                    branch.setText(br);
                    passout_yr.setText(py);
                    country.setText(countr);
                    state.setText(stat);
                    city.setText(cit);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/



   /* void removeImage(){
        imageNote.setImageBitmap(null);
        imageNote.setVisibility(View.GONE);
        view.findViewById(R.id.imageRemoveImage).setVisibility(View.GONE);
        selectedImagePath = "";
    }*/






