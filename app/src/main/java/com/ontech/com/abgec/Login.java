package com.ontech.com.abgec;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentTransaction;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.chaos.view.PinView;
import com.furkanakdemir.surroundcardview.SurroundCardView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.tomer.fadingtextview.FadingTextView;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import www.sanju.motiontoast.MotionToast;

public class Login extends AppCompatActivity {

    FadingTextView fadingTextView;
    String[] text
            = {"ABGEC",
            "Welcomes", "You"};

    TextView get_Otp, edtPhone, textView2, textView3, verify;
    View otp_verify;
    int val = 0;
    Animation animFadein, fade_out;
    FirebaseUser user;
    String DeviceToken;
    int count;
    private String verificationId;
    private FirebaseAuth mAuth;
    int downspeed;
    int upspeed;
    PinView pinView;
    LinearLayout number;
    ImageView back;
    DatabaseReference reference, admin_reference;
    SurroundCardView alumni, student;

    // Creating an Editor object to edit(write to the file)

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //setStatusBarTransparent();

      /*  fadingTextView
                = findViewById(R.id.fadingTextView);
        fadingTextView.setTexts(text);
*/
        mAuth = FirebaseAuth.getInstance();
        pinView = findViewById(R.id.pin_view);
        otp_verify = findViewById(R.id.otp);
        textView2 = findViewById(R.id.textView19);
        get_Otp = findViewById(R.id.get_otp);
        edtPhone = findViewById(R.id.phone_number);
        number = findViewById(R.id.layoutSearch);
        back = findViewById(R.id.back_img);
        textView3 = findViewById(R.id.textView25);
        reference = FirebaseDatabase.getInstance().getReference().child("users");
        admin_reference = FirebaseDatabase.getInstance().getReference().child("admin");
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        fade_out = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_out);
        setStatusBarTransparent();
        verify = findViewById(R.id.verify);
        alumni = findViewById(R.id.alumni);
        student = findViewById(R.id.student);



        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean connected = (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);

        if (!connected){
            MotionToast.Companion.darkColorToast(Login.this,
                    "No Internet",
                    "Connect with mobile network",
                    MotionToast.TOAST_NO_INTERNET,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(Login.this, R.font.lexend));
        }

        get_Otp.setOnClickListener(v -> {
            //check_for_admin();

            DatabaseReference admin_ref = FirebaseDatabase.getInstance().getReference().child("admin");
            admin_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Log.e("valuehggh", ds.getKey());
                        if (Objects.requireNonNull(ds.getKey()).equals(edtPhone.getText().toString())) {
                            next();
                            count = 2;
                            break;
                        } else {
                            next_user();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        });

        alumni.setOnClickListener(v -> {
            if (!alumni.isCardSurrounded()) {
                alumni.setSurroundStrokeWidth(R.dimen.width_card);
                alumni.surround();
                student.release();
                val = 1;
            }
        });

        student.setOnClickListener(v -> {
            if (!student.isCardSurrounded()) {
                student.setSurroundStrokeWidth(R.dimen.width_card);
                student.surround();
                alumni.release();
                val = 2;
            }
        });

        back.setOnClickListener(v -> {
            otp_verify.startAnimation(fade_out);
            otp_verify.setVisibility(View.GONE);
            onAnimate(number);
            onAnimate(textView2);
            onAnimate(get_Otp);
            onAnimate(alumni);
            onAnimate(student);
            onAnimate(back);
            back.setVisibility(View.GONE);
        });

        getting_device_token();


        verify.setOnClickListener(v ->

        {
         /*   if (Objects.requireNonNull(pinView.getText()).toString().trim().length() == 6) {
                String otp_text = Objects.requireNonNull(pinView.getText()).toString().trim();
                Log.e("pinView", "==========");
                verifyCode(otp_text);
            } else {
                Toast.makeText(this, "Please enter a valid OTP.", Toast.LENGTH_SHORT).show();
            }
*/
            if (TextUtils.isEmpty(pinView.getText().toString())) {
                // if the OTP text field is empty display
                // a message to user to enter OTP
                Toast.makeText(Login.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
            } else {
                // if OTP field is not empty calling
                // method to verify the OTP.
                verifyCode(pinView.getText().toString());
            }
        });

        pinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ch = s + "";
                if (ch.length() == 6) {
                    String otp_text = Objects.requireNonNull(pinView.getText()).toString().trim();
                    //Log.e("pinView", "==========");
                    verifyCode(otp_text);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }

    private void next_user() {
        if ((edtPhone.getText().toString().length() == 10) && (val != 0)) {

            offanimate(number);
            offanimate(textView2);
            offanimate(get_Otp);
            offanimate(alumni);
            offanimate(student);

            new Handler(Looper.myLooper()).postDelayed(() -> {
                otp_verify.setVisibility(View.VISIBLE);
                back.setVisibility(View.VISIBLE);
                otp_verify.startAnimation(animFadein);

            }, 600);
            textView3.setText("Successfully sent a verification code on " + edtPhone.getText().toString());
            String phone = "+91" + edtPhone.getText().toString();
            sendVerificationCode(phone);
            Toast.makeText(Login.this, "Please wait while we process.", Toast.LENGTH_SHORT).show();

        } else {
            if (edtPhone.getText().toString().length() < 10)
                Toast.makeText(Login.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
            else if (val == 0) {
                student.setVisibility(View.VISIBLE);
                alumni.setVisibility(View.VISIBLE);
                Toast.makeText(Login.this, "Please choose which option suits you", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void next() {
        offanimate(number);
        offanimate(textView2);
        offanimate(get_Otp);
        offanimate(alumni);
        offanimate(student);

        new Handler(Looper.myLooper()).postDelayed(() -> {
            otp_verify.setVisibility(View.VISIBLE);
            back.setVisibility(View.VISIBLE);
            otp_verify.startAnimation(animFadein);

        }, 600);
        textView3.setText("Successfully sent a verification code on " + edtPhone.getText().toString());
        String phone = "+91" + edtPhone.getText().toString();
        sendVerificationCode(phone);
        Toast.makeText(this, "Please wait while we process.", Toast.LENGTH_SHORT).show();

    }

    /*private void check_for_admin() {
        Log.e("num", edtPhone.getText().toString());
        //String pkey=reference.push().getKey();


        DatabaseReference admin_ref = FirebaseDatabase.getInstance().getReference().child("admin");
        admin_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (Objects.requireNonNull(ds.getKey()).equals(edtPhone.getText().toString())) {
                        count = 2;
                        val = 3;
                        Log.e("value", String.valueOf(count));
                        break;
                    }
                }
                *//*if(count==2){
                    //Log.e("admin entry","entered");
                    *//**//*getSharedPreferences("useris?",MODE_PRIVATE).edit()
                            .putString("the_user_is?","home").apply();*//**//*

                    reference.child(user.getUid()).child("phone").setValue(user.getPhoneNumber());
                    reference.child(user.getUid()).child("name").setValue("admin");
                    reference.child(user.getUid()).child("token").setValue(DeviceToken);
                    reference.child(user.getUid()).child("phone").setValue();
                    Intent i = new Intent(Login.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else{*//*

                //}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }*/

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            // initializing our callbacks for on
            // verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // when we receive the OTP it
            // contains a unique id which
            // we are storing in our string
            // which we have already created.
            verificationId = s;
        }

        // this method is called when user
        // receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // below line is used for getting OTP code
            // which is sent in phone auth credentials.
            final String code = phoneAuthCredential.getSmsCode();

            // checking if the code
            // is null or not.
            if (code != null) {
                // if the code is not null then
                // we are setting that code to
                // our OTP edittext field.
                pinView.setText(code);
                Log.e("inside code block", "==========");
                // after setting this code
                // to OTP edittext field we
                // are calling our verifycode method.
                verifyCode(code);
            }
        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            // Log.e("error",e+"");
        }
    };

    // below method is use to verify code from Firebase.
    private void verifyCode(String code) {
        // below line is used for getting getting
        // credentials from our verification id and code.
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

            // after getting credential we are
            // calling sign in method.
            signInWithCredential(credential);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // if the code is correct and the task is successful
                            // we are sending our user to new activity.
                            Log.e("task successfull", "Success");
                            user = mAuth.getCurrentUser();
                            assert user != null;
                            //reference.child(user.getUid()).child("phone").setValue(user.getPhoneNumber());
                            //reference.child(user.getUid()).child("token").setValue(DeviceToken);
                            //reference.child(user.getUid()).child("uid").setValue(user.getUid());
                            if (val == 2) {

                                SharedPreferences pref = getApplicationContext().getSharedPreferences("our_user?", MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putBoolean("student", true);
                                editor.putBoolean("alumni", false);
                                editor.putBoolean("admin", false);
                                editor.apply();

                               /* getSharedPreferences("our_user?", MODE_PRIVATE).edit()
                                        .putString("user_is?", "student").apply();*/
                                reference.child(user.getUid()).child("id").setValue("Student");
                                sendToMain();
                            }
                            else if (val == 1) {
                                /*getSharedPreferences("our_user?", MODE_PRIVATE).edit()
                                        .putString("user_is?", "alumni").apply();*/


                                SharedPreferences pref = getApplicationContext().getSharedPreferences("our_user?", MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putBoolean("alumni", true);
                                editor.putBoolean("student", false);
                                editor.putBoolean("admin", false);
                                editor.apply();

                                reference.child(user.getUid()).child("id").setValue("Alumni");
                                sendToForm();
                            }
                            else if (count == 2) {
                               /* getSharedPreferences("our_user?", MODE_PRIVATE).edit()
                                        .putString("user_is?", "admin").apply();*/

                                SharedPreferences pref = getApplicationContext().getSharedPreferences("our_user?", MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putBoolean("admin", true);
                                editor.putBoolean("alumni", false);
                                editor.putBoolean("student", false);
                                editor.apply();

                                reference.child(user.getUid()).child("id").setValue("Admin");
                                sendToMain();
                            }
                        }
                        else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Log.e("task result", task.getException().getMessage());
                            pinView.setError("Wrong Pin");
                            /*MotionToast.Companion.darkColorToast(Login.this,
                                    "Failed ☹️",
                                    "Please Enter a valid OTP",
                                    MotionToastStyle.ERROR,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.LONG_DURATION,
                                    ResourcesCompat.getFont(Login.this, R.font.lexend));*/
                        }
                    }
                });
    }


    private void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)            // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Home_gateway();
        }
    }
    private void Home_gateway() {
        Intent mainIntent = new Intent(Login.this , MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private void sendToForm() {

        reference.child(user.getUid()).child("phone").setValue(user.getPhoneNumber());
        reference.child(user.getUid()).child("token").setValue(DeviceToken);
        reference.child(user.getUid()).child("uid").setValue(user.getUid());
        DetailsForm fragment = new DetailsForm();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.layout_login, fragment).addToBackStack(null).commit();
    }

    private void sendToMain(){

        reference.child(user.getUid()).child("phone").setValue(user.getPhoneNumber());
        reference.child(user.getUid()).child("token").setValue(DeviceToken);
        reference.child(user.getUid()).child("uid").setValue(user.getUid());


        startActivity(new Intent(Login.this , MainActivity.class));
        finish();
    }


    private void setStatusBarTransparent() {
        Window window = Login.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    void offanimate(View view){
        ObjectAnimator move=ObjectAnimator.ofFloat(view, "translationX",-800f);
        move.setDuration(1000);
        ObjectAnimator alpha2= ObjectAnimator.ofFloat(view, "alpha",0);
        alpha2.setDuration(500);
        AnimatorSet animset=new AnimatorSet();
        animset.play(alpha2).with(move);
        animset.start();
    }
    void onAnimate(View view){
        ObjectAnimator move=ObjectAnimator.ofFloat(view, "translationX",0f);
        move.setDuration(1000);
        ObjectAnimator alpha2= ObjectAnimator.ofFloat(view, "alpha",100);
        alpha2.setDuration(500);
        AnimatorSet animset =new AnimatorSet();
        animset.play(alpha2).with(move);
        animset.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getting_device_token() {
        ConnectivityManager connectivityManager = (ConnectivityManager)this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkCapabilities nc = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        if(nc!=null) {
            downspeed = nc.getLinkDownstreamBandwidthKbps()/1000;
            upspeed = nc.getLinkUpstreamBandwidthKbps()/1000;
        }else{
            downspeed=0;
            upspeed=0;
        }

        if((upspeed!=0 && downspeed!=0) || getWifiLevel()!=0) {
            FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
                if (!TextUtils.isEmpty(token)) {
                    // Log.d("token", "retrieve token successful : " + token);
                } else {
                    //Log.w("token121", "token should not be null...");
                }
            }).addOnFailureListener(e -> {
                //handle e
            }).addOnCanceledListener(() -> {
                //handle cancel
            }).addOnCompleteListener(task ->
            {
                try {
                    DeviceToken = task.getResult();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }


    public int getWifiLevel()
    {
        try {
            WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            int linkSpeed = wifiManager.getConnectionInfo().getRssi();
            return WifiManager.calculateSignalLevel(linkSpeed, 5);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}