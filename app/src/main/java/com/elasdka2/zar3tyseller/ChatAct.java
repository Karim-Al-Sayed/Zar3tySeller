package com.elasdka2.zar3tyseller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elasdka2.zar3tyseller.Adapters.ChatAdapter;
import com.elasdka2.zar3tyseller.Model.Chat;
import com.elasdka2.zar3tyseller.Model.Tokens;
import com.elasdka2.zar3tyseller.Model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatAct extends AppCompatActivity {
    //----------------------------------
    private FirebaseAuth mAuth;
    private DatabaseReference MyRef;
    private FirebaseUser MyUser;
    Context context;
    String MyUserID,UserID, intent_from;
    //----------------------------------
    ChatAdapter messageAdapter;
    List<Chat> mchat;
    boolean notify = false;
    //----------------------------------
    @BindView(R.id.message_message_edt)
    EditText Send_EditText;
    @BindView(R.id.message_linear_send_btn)
    ImageButton Send_btn;
    @BindView(R.id.message_recycler)
    RecyclerView message_recycler;

    @OnClick(R.id.message_linear_send_btn)
    public void Send(){
        notify = true;
        String msg = Send_EditText.getText().toString();
        if (TextUtils.isEmpty(msg)){
            Toast.makeText(context, "You Can't Send Empty Message", Toast.LENGTH_LONG).show();
            Send_EditText.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(UserID)){
            Toast.makeText(context, "To User ID Is Empty", Toast.LENGTH_LONG).show();
            return;
        }
        SendMessage(MyUserID,UserID,msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_act);
        ButterKnife.bind(this);
        context = ChatAct.this;

        MyUser = FirebaseAuth.getInstance().getCurrentUser();
        if (MyUser != null) {
            MyUserID = MyUser.getUid();
        }

        if (getIntent() != null) {
            intent_from = Objects.requireNonNull(getIntent().getExtras()).getString("UniqueID");
            if (!TextUtils.isEmpty(intent_from)) {
                if (intent_from.equals("from_DisplayChatsAdapter")) {
                    UserID = getIntent().getExtras().getString("SellerID");
                }

                message_recycler.setHasFixedSize(true);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                linearLayoutManager.setStackFromEnd(true);
                message_recycler.setLayoutManager(linearLayoutManager);
                ReadMessage(MyUserID,UserID);
                UpdateToken(FirebaseInstanceId.getInstance().getToken());
            }
        }

    }

    private void SendMessage(String sender, String receiver, String message){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> ChatMap = new HashMap<>();
        ChatMap.put("sender",sender);
        ChatMap.put("receiver",receiver);
        ChatMap.put("message",message);
        reference.child("Chats").push().setValue(ChatMap);

        HashMap<String,Object> ChatListMap = new HashMap<>();
        ChatListMap.put("User_ID",receiver);
        reference.child("ChatList").child(sender).child(receiver).setValue(ChatListMap).addOnCompleteListener(task -> {
            Toast.makeText(context, "Sent .", Toast.LENGTH_SHORT).show();
            Send_EditText.setText("");
        });
        reference = FirebaseDatabase.getInstance().getReference("Users").child("Sellers").child(MyUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String UserName = dataSnapshot.child("UserName").getValue(String.class);
                String UserID = dataSnapshot.child("User_ID").getValue(String.class);
                String SellerImg = dataSnapshot.child("ImgUri").getValue(String.class);

                Users users = new Users();
                users.setUserName(UserName);
                users.setUser_ID(UserID);
                users.setImgUri(SellerImg);

               /* if (notify) {
                    sendSellerNotification(receiver, users.getFirstName() + " " + users.getLastName(), msg);
                }
                notify = false;*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void ReadMessage(final String CustomerID, final String SellerID) {
        mchat = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mchat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat != null && (chat.getTo().equals(CustomerID) && chat.getFrom().equals(SellerID) ||
                            chat.getTo().equals(SellerID) && chat.getFrom().equals(CustomerID))) {
                        mchat.add(chat);
                    }
                    messageAdapter = new ChatAdapter(context,mchat);
                    message_recycler.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // add user to chat fragment
        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(MyUser.getUid())
                .child(UserID);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatRef.child("User_ID").setValue(UserID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(UserID)
                .child(MyUser.getUid());
        chatRefReceiver.child("User_ID").setValue(MyUser.getUid());
    }

    private void UpdateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SellerTokens");
        Tokens token1 = new Tokens(token);
        reference.child(MyUser.getUid()).setValue(token1);
    }
    private void currentUser(String userid){
        SharedPreferences.Editor editor = context.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
        editor.putString("currentuser", userid);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentUser(MyUserID);
    }

    @Override
    protected void onPause() {
        super.onPause();
        currentUser("none");
    }
}
