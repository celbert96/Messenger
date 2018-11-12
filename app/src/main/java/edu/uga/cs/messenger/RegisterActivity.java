package edu.uga.cs.messenger;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class RegisterActivity extends AppCompatActivity
{
    private EditText usernameET;
    private EditText emailET;
    private EditText passwordET;
    private Button registerBtn;
    private TextView existingAccountTV;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameET = findViewById(R.id.username_et_register);
        emailET = findViewById(R.id.email_et_register);
        passwordET = findViewById(R.id.passwd_et_register);
        registerBtn = findViewById(R.id.register_btn_register);
        existingAccountTV = findViewById(R.id.existing_account_tv_register);

        //TODO: Confirm Password ET

        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerNewUser();
            }
        });

        existingAccountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });


    }

    private void registerNewUser()
    {
        final String username = getText(usernameET);
        String email = getText(emailET);
        String passwd = getText(passwordET);
        if(username.isEmpty() || email.isEmpty() || passwd.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please ensure all fields are filled out.", Toast.LENGTH_SHORT).show();
            return;
        }
        //register user in firebase
        mAuth.createUserWithEmailAndPassword(email, passwd)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent();
                            intent.putExtra("username", username);
                            intent.setClass(getApplicationContext(), SelectImageActivity.class);
                            startActivity(intent);

                        }
                    }

                })

                .addOnFailureListener(RegisterActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("FAILURE", "Failed to create user: " + e.getMessage());
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private String getText(EditText et)
    {

        if(et == null || et.getText().toString().isEmpty())
        {
            return "";
        }
        else
        {
            return et.getText().toString();
        }

    }
}
