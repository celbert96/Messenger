package edu.uga.cs.messenger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        String username = intent.getStringExtra("USERNAME");
        if(!username.isEmpty() && username != "")
        {
            getSupportActionBar().setTitle(username);
        }

    }
}
