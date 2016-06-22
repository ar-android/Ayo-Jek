package com.ahmadrosid.ayo_jek.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmadrosid.ayo_jek.BaseActivity;
import com.ahmadrosid.ayo_jek.R;
import com.ahmadrosid.ayo_jek.helper.PreferenceHelper;
import com.ahmadrosid.ayo_jek.model.User;
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

public class SignActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "SignActivity";

    TextView txt_sign_up;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListener;
    private EditText email;
    private EditText password;
    private Button btn_sign_in;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        txt_sign_up = (TextView) findViewById(R.id.txt_sign_up);
        email = (EditText) findViewById(R.id.field_email);
        password = (EditText) findViewById(R.id.field_password);
        btn_sign_in = (Button) findViewById(R.id.btn_sign_in);

        txt_sign_up.setOnClickListener(this);
        btn_sign_in.setOnClickListener(this);

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
                    mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // Get user value
                                    User user = dataSnapshot.getValue(User.class);
                                    PreferenceHelper preferenceHelper = new PreferenceHelper(getApplicationContext());
                                    preferenceHelper.saveDataProfile(user.full_name, user.no_hp, user.email, user.uid, user.fcm_id);
                                    // End get data user
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                                }
                            });
                    startActivity(new Intent(SignActivity.this, HomeActivity.class));
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    /**
     * Sign in method
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
                            hideProgressDialog();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        }

                    }
                });
    }

    private void validateForm() {
        if (TextUtils.isEmpty(email.getText())) {
            email.setError("REQUIRED");
        } else if (TextUtils.isEmpty(password.getText())) {
            password.setError("REQUIRED");
        } else {
            return;
        }
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
                startActivity(new Intent(getApplicationContext(), SignupActivity.class));
                break;
        }
    }
}
