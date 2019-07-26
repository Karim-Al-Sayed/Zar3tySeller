package com.elasdka2.zar3tyseller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elasdka2.zar3tyseller.Adapters.MessageAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Message.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Message#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Message extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    //----------------------------------
    private FirebaseAuth mAuth;
    private DatabaseReference MyRef;
    private FirebaseUser MyUser;
    Context context;
    Intent intent;
    String MyUserID,UserID, intent_from;
    //----------------------------------
    MessageAdapter messageAdapter;
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
        final String msg = message;
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

    public Message() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Message newInstance(String param1, String param2) {
        Message fragment = new Message();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.message_frag, container, false);
        ButterKnife.bind(this, v);
        context = getActivity();
        MyUser = FirebaseAuth.getInstance().getCurrentUser();
        MyUserID = MyUser.getUid();

        if (getArguments() != null){
            intent_from = getArguments().getString("UniqueID");
            if (!TextUtils.isEmpty(intent_from)) {
                if (intent_from.equals("from_SellerChatsAdapter")) {
                    UserID = getArguments().getString("user_id");
                }
            }
            message_recycler.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setStackFromEnd(true);
            message_recycler.setLayoutManager(linearLayoutManager);
            ReadMessage(MyUserID,UserID);

        }

        return v;
    }

    private void ReadMessage(final String SellerID, final String CustomerID){
        mchat = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mchat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(CustomerID) && chat.getSender().equals(SellerID) ||
                            chat.getReceiver().equals(SellerID) && chat.getSender().equals(CustomerID)){
                        mchat.add(chat);
                    }
                    messageAdapter = new MessageAdapter(context,mchat);
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
    public void onResume() {
        super.onResume();
        currentUser(MyUser.getUid());

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
