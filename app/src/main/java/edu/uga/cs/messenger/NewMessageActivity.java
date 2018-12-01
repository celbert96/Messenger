package edu.uga.cs.messenger;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewMessageActivity extends AppCompatActivity {

    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);
        getSupportActionBar().setTitle("Select User");

        rv = findViewById(R.id.select_user_rv);
        rv.setNestedScrollingEnabled(false);
        rv.setHasFixedSize(true);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(linearLayoutManager);

        getUsersFromFirebaseDatabase();

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

                SelectUserAdapter adapter = new SelectUserAdapter(userList);
                adapter.setHasStableIds(true);
                rv.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}

class SelectUserAdapter extends RecyclerView.Adapter<SelectUserAdapter.UserViewHolder>
{

    ArrayList<User> data;
    public static class UserViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView usernameLabel;
        TextView contentLabel;
        ImageView profilePic;

        public UserViewHolder(@NonNull final View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.select_user_cv);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(itemView.getContext(), ChatActivity.class);
                    itemView.getContext().startActivity(intent);

                }
            });
            usernameLabel = itemView.findViewById(R.id.username_lbl_select_user);
            contentLabel = itemView.findViewById(R.id.email_lbl_select_user);
            profilePic = itemView.findViewById(R.id.profilepicpreview_select_user);
        }
    }

    public SelectUserAdapter(ArrayList<User> data)
    {
        this.data = data;
    }


    @NonNull
    @Override
    public SelectUserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.select_user_list_item, viewGroup, false);
        SelectUserAdapter.UserViewHolder uv = new SelectUserAdapter.UserViewHolder(v);
        return uv;
    }

    @Override
    public void onBindViewHolder(@NonNull final SelectUserAdapter.UserViewHolder userViewHolder, int i) {
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
