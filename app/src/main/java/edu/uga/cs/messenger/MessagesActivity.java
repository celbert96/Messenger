package edu.uga.cs.messenger;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MessagesActivity extends AppCompatActivity {

    //TODO (Messages 1): Create Firebase Database for Messages
    //TODO (Messages 2): Create RecyclerView to display convos + user profile pics

    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);


        rv = findViewById(R.id.messages_rv);
        rv.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(layoutManager);

        getUsersFromFirebaseDatabase();



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.messages_activity_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.new_msg_btn:
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), NewMessageActivity.class);
                startActivity(intent);
        }

        return true;
    }

    private void getUsersFromFirebaseDatabase()
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<User> userList = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    User user = snapshot.getValue(User.class);
                    Log.d("GETUSERSFROMFIREBASE:", user.getUid() + " " +  user.getUsername() + " " + user.getImageURL());
                    userList.add(user);
                }

                RVAdapter adapter = new RVAdapter(userList);
                rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}


class RVAdapter extends RecyclerView.Adapter<RVAdapter.UserViewHolder>
{
    ArrayList<User> data;
    public static class UserViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView usernameLabel;
        TextView contentLabel;
        ImageView profilePic;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview);
            usernameLabel = itemView.findViewById(R.id.username_msg);
            contentLabel = itemView.findViewById(R.id.content_msg);
            profilePic = itemView.findViewById(R.id.profilepicpreview);
        }
    }

    public RVAdapter(ArrayList<User> data)
    {
        this.data = data;
    }


    @NonNull
    @Override
    public RVAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_list_item, viewGroup, false);
        UserViewHolder uv = new UserViewHolder(v);
        return uv;
    }

    @Override
    public void onBindViewHolder(@NonNull RVAdapter.UserViewHolder userViewHolder, int i) {
        Picasso.get().load(data.get(i).getImageURL()).into(userViewHolder.profilePic);
        userViewHolder.usernameLabel.setText(data.get(i).getUsername());
        userViewHolder.contentLabel.setText(data.get(i).getUid());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}


