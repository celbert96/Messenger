package edu.uga.cs.messenger;

import android.content.Intent;
import android.support.annotation.NonNull;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MessagesActivity extends AppCompatActivity {

    private RecyclerView rv;
    private String currentUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);


        currentUID = FirebaseAuth.getInstance().getUid();
        rv = findViewById(R.id.messages_rv);
        rv.setNestedScrollingEnabled(false);
        rv.setHasFixedSize(true);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(layoutManager);

        loadUsers();



    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUsers();
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

    private void getUsersFromFirebaseDatabase(final ArrayList<String> uids)
    {
        Log.d("UIDSSIZE", Integer.toString(uids.size()));
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<User> userList = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    User user = snapshot.getValue(User.class);
                    Log.d("GETUSERSFROMFIREBASE:", user.getUid() + " " +  user.getUsername() + " " + user.getImageURL());
                    if(uids.contains(user.getUid()))
                        userList.add(user);
                }

                RVAdapter adapter = new RVAdapter(userList);
                adapter.setHasStableIds(true);
                rv.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadUsers()
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/messages");
        final ArrayList<String> uids = new ArrayList<>();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Message message = snapshot.getValue(Message.class);
                    if(isValidMessage(message))
                    {
                        uids.add(getUIDFromMessage(message));
                        getUsersFromFirebaseDatabase(uids);


                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });

        Log.d("UIDSIZEUIDS", Integer.toString(uids.size()));
    }

    private boolean isValidMessage(Message m)
    {
        return (m.getSenderID().equals(currentUID)) ||
                (m.getRecipientID().equals(currentUID));
    }

    private String getUIDFromMessage(Message m)
    {
        if(m.getSenderID().equals(currentUID))
        {
            return m.getRecipientID();
        }
        else
        {
            return m.getSenderID();
        }
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

        public UserViewHolder(@NonNull final View itemView) {
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
    public void onBindViewHolder(@NonNull final RVAdapter.UserViewHolder userViewHolder, int i) {
        Picasso.get().load(data.get(i).getImageURL()).fit().into(userViewHolder.profilePic);
        userViewHolder.usernameLabel.setText(data.get(i).getUsername());
        userViewHolder.contentLabel.setText(data.get(i).getUid());

        userViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), ChatActivity.class);
                intent.putExtra("USER", data.get(userViewHolder.getAdapterPosition()));
                view.getContext().startActivity(intent);
            }
        });

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


