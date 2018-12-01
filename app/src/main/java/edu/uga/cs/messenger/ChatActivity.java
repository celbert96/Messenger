package edu.uga.cs.messenger;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView rv;
    private Button sendBtn;
    private String currentUID;
    private User recipientUser;
    private EditText msgET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        recipientUser = (User) intent.getSerializableExtra("USER");
        String username = recipientUser.getUsername();
        if(!username.isEmpty())
        {
            getSupportActionBar().setTitle(username);
        }

        currentUID = FirebaseAuth.getInstance().getUid();



        rv = findViewById(R.id.chat_log_rv);
        rv.setNestedScrollingEnabled(false);
        rv.setHasFixedSize(true);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(linearLayoutManager);

        getMessagesFromFirebaseDatabase();

        msgET = findViewById(R.id.chat_msg_et);
        sendBtn = findViewById(R.id.chat_msg_send_btn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = msgET.getText().toString();
                DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("/messages").push();
                if(!input.isEmpty())
                {
                    String timestamp = new SimpleDateFormat("MM/dd/YYYY HH:mm", Locale.getDefault()).format(new Date());
                    Message m = new Message(recipientUser.getUid(), FirebaseAuth.getInstance().getUid(), timestamp, input);
                    dbr.setValue(m).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Log.d("SendMessage", "Success");
                            }
                            else
                            {
                                Log.d("SendMessage", "failed");
                            }
                        }
                    });
                }
            }
        });



    }

    private void getMessagesFromFirebaseDatabase()
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/messages");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<Message> messages = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Message message = snapshot.getValue(Message.class);
                    if(isValidMessage(message))
                        messages.add(message);
                }

                ChatLogAdapter adapter = new ChatLogAdapter(messages);
                adapter.setHasStableIds(true);
                rv.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean isValidMessage(Message m)
    {
        return (m.getSenderID().equals(currentUID) && m.getRecipientID().equals(recipientUser.getUid())) ||
                (m.getRecipientID().equals(currentUID) && m.getSenderID().equals(recipientUser.getUid()));
    }
}


/*
* This Adapter contains two ViewHolder classes: LeftMessageViewHolder and
* RightMessageViewHolder. Both ViewHolders display similar data, but inflate
* different CardView layouts.
 */
class ChatLogAdapter extends RecyclerView.Adapter
{

    ArrayList<Message> data; //Message data to be displayed

    //constants to determine which ViewHolder to inflate
    private static final int VIEW_MESSAGE_RECIPIENT = 1;
    private static final int VIEW_MESSAGE_SENDER = 2;


    //Will be used when displaying received messages
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

        public void bind(Message message)
        {
            usernameLabel.setText("Them");
            contentLabel.setText(message.getContent());
            timestampLabel.setText(message.getTimestamp());
        }
    }

    //Will be used when displaying sent messages
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

        public void bind(Message message)
        {
            usernameLabel.setText("You");
            contentLabel.setText(message.getContent());
            timestampLabel.setText(message.getTimestamp());
        }
    }

    //Determine if the message is "sent" or "received"
    @Override
    public int getItemViewType(int position) {

        Message m = data.get(position);
        if(m.getSenderID().equals(FirebaseAuth.getInstance().getUid()))
        {
            return VIEW_MESSAGE_SENDER;
        }
        else
        {
            return VIEW_MESSAGE_RECIPIENT;
        }
    }



    //constructor for the ChatLogAdapter
    public ChatLogAdapter(ArrayList<Message> data)
    {
        this.data = data;
    }

    //Inflate the correct view based on the value of viewType
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

        //if this line of code is reached, there was an error.
        Log.d("ERROR", "Error in onCreateViewHolder - unable to inflate the correct view");
        return null;
    }

    //Bind the correct viewHolder
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Message m = data.get(i);
        switch(viewHolder.getItemViewType())
        {
            case VIEW_MESSAGE_RECIPIENT:
            {
                ((ChatLogAdapter.LeftMessageViewHolder) viewHolder).bind(m);
                break;
            }
            case VIEW_MESSAGE_SENDER:
            {
                ((ChatLogAdapter.RightMessageViewHolder) viewHolder).bind(m);
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
