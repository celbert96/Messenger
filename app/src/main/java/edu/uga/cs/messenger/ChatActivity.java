package edu.uga.cs.messenger;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView rv;
    private DatabaseReference dbr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        String username = intent.getStringExtra("USERNAME");
        if(!username.isEmpty())
        {
            getSupportActionBar().setTitle(username);
        }

        dbr = FirebaseDatabase.getInstance().getReference();
        rv = findViewById(R.id.chat_log_rv);
        rv.setNestedScrollingEnabled(false);
        rv.setHasFixedSize(true);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(linearLayoutManager);

        ArrayList<TestMessage> arrayList = new ArrayList<>();
        arrayList.add(new TestMessage("Chris", "Hey dude! How's it going?", "11/30/2018"));
        arrayList.add(new TestMessage("Chris", "Hey dude! How's it going?", "11/30/2018"));
        arrayList.add(new TestMessage("Chris", "Hey dude! How's it going?", "11/30/2018"));
        arrayList.add(new TestMessage("Chris", "Hey dude! How's it going?", "11/30/2018"));
        arrayList.add(new TestMessage("Chris", "Hey dude! How's it going?", "11/30/2018"));
        arrayList.add(new TestMessage("Chris", "Hey dude! How's it going?", "11/30/2018"));

        ChatLogAdapter cla = new ChatLogAdapter(arrayList);


        rv.setAdapter(cla);

    }
}

class ChatLogAdapter extends RecyclerView.Adapter
{

    ArrayList<TestMessage> data;
    private static final int VIEW_MESSAGE_RECIPIENT = 1;
    private static final int VIEW_MESSAGE_SENDER = 2;
    public static class LeftMessageViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView usernameLabel;
        TextView contentLabel;
        TextView timestampLabel;

        public LeftMessageViewHolder(@NonNull final View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.chat_item_left);
            usernameLabel = itemView.findViewById(R.id.username_lbl_chat_left);
            contentLabel = itemView.findViewById(R.id.content_msg_chat_left);
            timestampLabel = itemView.findViewById(R.id.timestamp_lbl_chat_left);
        }

        public void bind(TestMessage message)
        {
            usernameLabel.setText(message.getUsername());
            contentLabel.setText(message.getMsg());
            timestampLabel.setText(message.getTimestamp());
        }
    }

    public static class RightMessageViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView usernameLabel;
        TextView contentLabel;
        TextView timestampLabel;


        public RightMessageViewHolder(@NonNull final View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.chat_item_right);
            usernameLabel = itemView.findViewById(R.id.username_lbl_chat_right);
            contentLabel = itemView.findViewById(R.id.content_msg_chat_right);
            timestampLabel = itemView.findViewById(R.id.timestamp_lbl_chat_right);
        }

        public void bind(TestMessage message)
        {
            usernameLabel.setText(message.getUsername());
            contentLabel.setText(message.getMsg());
            timestampLabel.setText(message.getTimestamp());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position % 2 == 0)
        {
            return VIEW_MESSAGE_RECIPIENT;
        }
        else
        {
            return VIEW_MESSAGE_SENDER;
        }
    }



    public ChatLogAdapter(ArrayList<TestMessage> data)
    {
        this.data = data;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v;
        if(viewType == ChatLogAdapter.VIEW_MESSAGE_RECIPIENT)
        {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_item_left, viewGroup, false);
            ChatLogAdapter.LeftMessageViewHolder mv = new LeftMessageViewHolder(v);
            return mv;
        }
        else if(viewType == ChatLogAdapter.VIEW_MESSAGE_SENDER)
        {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_item_right, viewGroup, false);
            ChatLogAdapter.RightMessageViewHolder rmv = new ChatLogAdapter.RightMessageViewHolder(v);
            return rmv;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        TestMessage tm = data.get(i);
        switch(viewHolder.getItemViewType())
        {
            case VIEW_MESSAGE_RECIPIENT:
            {
                ((ChatLogAdapter.LeftMessageViewHolder) viewHolder).bind(tm);
                break;
            }
            case VIEW_MESSAGE_SENDER:
            {
                ((ChatLogAdapter.RightMessageViewHolder) viewHolder).bind(tm);
            }
        }
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

class TestMessage
{
    private String username;
    private String msg;
    private String timestamp;
    public TestMessage(String username, String msg, String timestamp)
    {
        this.timestamp = timestamp;
        this.msg  = msg;
        this.username = username;
    }

    public String getMsg() {
        return msg;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
