package edu.uga.cs.messenger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MessagesActivity extends AppCompatActivity {

    //TODO (Messages 1): Create Firebase Database for Messages
    //TODO (Messages 2): Create RecyclerView to display convos + user profile pics

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
    }


    @Override
    public void onBackPressed() {

    }
}
