package edu.uga.cs.messenger;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

        ArrayList<TestUser> testUsers = new ArrayList<>();
        testUsers.add(new TestUser("Chris", "This is a test message", R.drawable.default_profile_pic));
        testUsers.add(new TestUser("Chris", "This is a test message", R.drawable.default_profile_pic));
        testUsers.add(new TestUser("Chris", "This is a test message", R.drawable.default_profile_pic));
        testUsers.add(new TestUser("Chris", "This is a test message", R.drawable.default_profile_pic));
        testUsers.add(new TestUser("Chris", "This is a test message", R.drawable.default_profile_pic));
        testUsers.add(new TestUser("Chris", "This is a test message", R.drawable.default_profile_pic));

        RVAdapter adapter = new RVAdapter(testUsers);
        rv.setAdapter(adapter);

    }


    @Override
    public void onBackPressed() {

    }
}


class RVAdapter extends RecyclerView.Adapter<RVAdapter.UserViewHolder>
{
    ArrayList<TestUser> data;
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

    public RVAdapter(ArrayList<TestUser> data)
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
        userViewHolder.profilePic.setImageResource(data.get(i).photoID);
        userViewHolder.usernameLabel.setText(data.get(i).username);
        userViewHolder.contentLabel.setText(data.get(i).content);
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


class TestUser {
    String username;
    String content;
    int photoID;

    public TestUser(String username, String content, int photoID)
    {
        this.username = username;
        this.content = content;
        this.photoID = photoID;
    }

}

