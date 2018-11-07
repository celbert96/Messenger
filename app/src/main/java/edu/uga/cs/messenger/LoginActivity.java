package edu.uga.cs.messenger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity
{
    private EditText usernameET;
    private EditText passwordET;
    private Button loginBtn;
    private TextView registerTV;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameET = findViewById(R.id.username_et_login);
        passwordET = findViewById(R.id.passwd_et_login);
        loginBtn = findViewById(R.id.signin_btn_login);
        registerTV = findViewById(R.id.register_tv_login);

        getSupportActionBar().hide();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = getText(usernameET);
                String passwd = getText(passwordET);
                if(username.isEmpty() || passwd.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please ensure all fields are filled out.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //register in firebase
            }
        });

        registerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
