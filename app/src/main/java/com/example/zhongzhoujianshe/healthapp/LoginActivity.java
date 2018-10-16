package com.example.zhongzhoujianshe.healthapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    // Firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText et_appid;
    private ProgressDialog mAuthProgressDialog;
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //used for login icon-font
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
        TextView login_icon = (TextView) findViewById(R.id.login_icon);
        login_icon.setTypeface(font);
        et_appid = (EditText) findViewById(R.id.et_appid);
        //Get Firebase auth instance
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        createAuthProgressDialog();
        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(LoginActivity.this, MainMenu.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }

        };


        MyRoundCornerButton btn_log = (MyRoundCornerButton) findViewById(R.id.btn_login);
        btn_log.setFillet(true);
        btn_log.setRadius(15);
        btn_log.setBackColor(getResources().getColor(R.color.radiobutton));
        btn_log.setTextColori(getResources().getColor(R.color.white));
        btn_log.setText(getResources().getString(R.string.login));
        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                loginWithPassword();


            }
        });
    }
    private void loginWithPassword() {
        String email = et_appid.getText().toString().trim();
        String password = "111111";

        if (email.equals("")) {
            et_appid.setError("Please enter your email");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mAuthProgressDialog.dismiss();

                if (!task.isSuccessful()) {

                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void createAuthProgressDialog() {
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Loading...");
        mAuthProgressDialog.setMessage("Authenticating with Firebase...");
        mAuthProgressDialog.setCancelable(false);
    }

}
