package com.ahmadrosid.ayo_jek_driver.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class Signup extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "Signup : ";

    private EditText full_name;
    private EditText no_hp;
    private EditText email;
    private EditText password;
    private EditText plat;
    private TextView btn_sign_in;
    private Button btn_sign_up;

    private String REQUIRED = "REQUIRED";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        castView();
    }

    /***
     * Cast view signup
     */
    private void castView() {
        full_name = (EditText) findViewById(R.id.field_name);
        no_hp = (EditText) findViewById(R.id.field_no_hp);
        plat = (EditText) findViewById(R.id.field_plat);
        email = (EditText) findViewById(R.id.field_email);
        password = (EditText) findViewById(R.id.field_password);
        btn_sign_in = (TextView) findViewById(R.id.btn_sign_in);
        btn_sign_up = (Button) findViewById(R.id.btn_sign_up);
        btn_sign_up.setOnClickListener(this);
        btn_sign_in.setOnClickListener(this);
    }

    /***
     * Form validation
     */
    private void validateForm(){
        if (TextUtils.isEmpty(full_name.getText())){
            full_name.setError(REQUIRED);
        }else if (TextUtils.isEmpty(no_hp.getText())){
            no_hp.setError(REQUIRED);
        }else if(TextUtils.isEmpty(plat.getText())){
            plat.setError(REQUIRED);
        }else if (TextUtils.isEmpty(email.getText())){
            email.setError(REQUIRED);
        }else if (TextUtils.isEmpty(password.getText())){
            password.setError(REQUIRED);
        }else{
            createAccount(email.getText().toString(), password.getText().toString());
        }
    }

    /**
     * Create account and save to database and preference
     * @param email
     * @param password
     */
    private void createAccount(final String email, String password) {
        log(TAG + "createAccount:" + email);

        showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            String user_name = full_name.getText().toString();
                            String user_no_hp = no_hp.getText().toString();
                            String plat_motor = plat.getText().toString();
                            String fcmId = FirebaseInstanceId.getInstance().getToken();
                            String uId = task.getResult().getUser().getUid();
                            writeNewUser(user_name, email, user_no_hp, uId, fcmId, plat_motor);
                        }

                        hideProgressDialog();
                    }
                });
    }

    /**
     * Write new user
     * @param userId
     * @param fcmId
     */
    private void writeNewUser(String user_name, String user_email, String user_no_hp, String userId, String fcmId, String plat_motor) {
        // Write on model
        Driver user = new Driver(userId, fcmId, user_email, user_name, user_no_hp, plat_motor);

        // Save to database
        mDatabase.child("drivers").child(userId).setValue(user.toMap());

        PrefHelper preferenceHelper = new PrefHelper(getApplicationContext());
        preferenceHelper.saveDataDriver(userId, fcmId, user_email, user_name, user_no_hp, plat_motor);

        log("Write new user executed!");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_sign_up:
                validateForm();
                break;
            case R.id.btn_sign_in:
                onBackPressed();
                break;
        }
    }
}
