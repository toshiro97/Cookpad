package com.example.cookpad.view;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.cookpad.R;
import com.example.cookpad.adapter.ChatAdapter;
import com.example.cookpad.model.ChatMessenger;
import com.example.cookpad.model.User;
import com.example.cookpad.until.PrefManager;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.recyclerChat)
    RecyclerView recyclerChat;
    @BindView(R.id.edtMessenger)
    EditText edtMessenger;
    @BindView(R.id.btnSend)
    ImageView btnSend;

    ChatAdapter chatAdapter;

    RecyclerView.LayoutManager layoutManager;
    List<ChatMessenger> chatMessengerList;
    FirebaseDatabase database;
    DatabaseReference referenceSend;
    DatabaseReference referenceReceive;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        prefManager = new PrefManager(this);
        User userReceive = (User) getIntent().getSerializableExtra("user_chat");
        tvName.setText(userReceive.getName());

        database = FirebaseDatabase.getInstance();
        referenceSend = database.getReference("messenger").child(prefManager.getUser().getId() + "-" + userReceive.getId());
        referenceReceive = database.getReference("messenger").child(userReceive.getId() + "-" + prefManager.getUser().getId());

        chatMessengerList = new ArrayList<>();

        chatAdapter = new ChatAdapter(chatMessengerList,userReceive);



        recyclerChat.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,true);
        recyclerChat.setLayoutManager(layoutManager);
        recyclerChat.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();
        getListMessenger();


    }

    @OnClick(R.id.btnSend)
    public void onViewClicked() {
        String messenger = edtMessenger.getText().toString();
        ChatMessenger chatMessengerSend = new ChatMessenger(messenger);
        chatMessengerSend.setSend(true);
        referenceSend.push().setValue(chatMessengerSend);
        ChatMessenger chatMessengerRecei = new ChatMessenger(messenger);
        referenceReceive.push().setValue(chatMessengerRecei);
        edtMessenger.setText("");
    }

    private void getListMessenger(){
        Query query = referenceSend.orderByKey();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatMessengerList.clear();
                ArrayList<ChatMessenger> chatMessengers = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatMessenger chatMessenger = snapshot.getValue(ChatMessenger.class);
                    chatMessengers.add(chatMessenger);
                }
                for (int i = chatMessengers.size() - 1; i >= 0; i--){
                    chatMessengerList.add(chatMessengers.get(i));
                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
