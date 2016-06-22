package com.ahmadrosid.ayo_jek_driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmadrosid.ayo_jek_driver.BaseActivity;
import com.ahmadrosid.ayo_jek_driver.R;
import com.ahmadrosid.ayo_jek_driver.helper.PrefHelper;
import com.ahmadrosid.ayo_jek_driver.models.Driver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Sign extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "Sign";

    private TextView txt_sign_up;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListener;
    private EditText email;
    private EditText password;
    private Button btn_sign_in;
    private DatabaseReference mDatabase;
    private ViewGroup viewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        txt_sign_up = (TextView) findViewById(R.id.txt_sign_up);
        email = (EditText) findViewById(R.id.field_email);
        password = (EditText) findViewById(R.id.field_password);
        btn_sign_in = (Button) findViewById(R.id.btn_sign_in);

        txt_sign_up.setOnClickListener(this);
        btn_sign_in.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        auth();
        mAuth.addAuthStateListener(authListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                signIn(email.getText().toString(), password.getText().toString());
                finish();
                break;
            case R.id.txt_sign_up:
                startActivity(new Intent(getApplicationContext(), Signup.class));
                break;
        }
    }


    /**
     * Authentication
     */
    private void auth() {
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser users = firebaseAuth.getCurrentUser();
                if (users != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + users.getUid());

                    String userId = users.getUid();
                    mDatabase.child("drivers").child(userId).addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // Get user value
                                    Driver driver = dataSnapshot.getValue(Driver.class);
                                    PrefHelper preferenceHelper = new PrefHelper(getApplicationContext());
                                    preferenceHelper.saveDataDriver(driver.uid, driver.fcm_id, driver.email, driver.name, driver.no_hp, driver.plat);
                                    // End get data user
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    log(TAG + "getUser:onCancelled" + String.valueOf(databaseError.toException()));
                                }
                            });
                    startActivity(new Intent(Sign.this, MainActivity.class));
                } else {
                    // User is signed out
                    log(TAG + "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    /**
     * Sign in method
     *
     * @param email
     * @param password
     */
    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        validateForm();

        showProgressDialog();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }

                        hideProgressDialog();
                    }
                });
    }

    /***
     * Form validation
     * @return
     */
    private boolean validateForm() {
        if (TextUtils.isDigitsOnly(email.toString().toString())){
            email.setError("REQUIRED");
            showToas("Email tidak boleh kosong!");
            return false;
        }else if (TextUtils.isDigitsOnly(password.getText().toString())){
            password.setError("REQUIRED");
            showToas("Password belum di isi!");
            return false;
        }else{
            return true;
        }
    }

    /**
     * Show Toast
     * @param s
     */
    private void showToas(String s){
        Snackbar snack = Snackbar.make(viewGroup, s, Snackbar.LENGTH_LONG);

        snack.getView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            public boolean canDisplaySnackbar;

            @Override
            public void onViewAttachedToWindow(View v) {
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                new CountDownTimer(3000, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        canDisplaySnackbar = false;
                    }
                }.start();
            }
        });
        snack.show();
    }
}
