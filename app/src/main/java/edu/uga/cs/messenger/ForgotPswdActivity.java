package edu.uga.cs.messenger;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ForgotPswdActivity extends AppCompatActivity
{

    private EditText emailET;
    private Button forgotPswdBtn;
    private FirebaseAuth mAuth;
    private TextView registerTV;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pswd);

        emailET = findViewById(R.id.email_et);
        forgotPswdBtn = findViewById(R.id.forgot_password_btn);
        registerTV = findViewById(R.id.register_tv);

        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();

        registerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        forgotPswdBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d("Password Reset", "Successful");
                String userEmail = emailET.getText().toString();
                if (userEmail.equals("")) {
                    Toast.makeText(ForgotPswdActivity.this, "Please enter your email!", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Log.d("Password Reset", "Successful");
                                Toast.makeText(ForgotPswdActivity.this, "Please Check your email to reset your password", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                intent.setClass(getApplicationContext(), RegisterActivity.class);
                                startActivity(intent);
                            } else {
                                String er = task.getException().getMessage();
                                Log.d("Password Reset", "Failed");
                                Toast.makeText(ForgotPswdActivity.this, "Error occured: " + er, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

}
