package com.elasdka2.zar3tyseller.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.elasdka2.zar3tyseller.Message;
import com.elasdka2.zar3tyseller.Model.ChatSeller;
import com.elasdka2.zar3tyseller.Model.Users;
import com.elasdka2.zar3tyseller.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SellerChatsAdapter extends RecyclerView.Adapter<SellerChatsAdapter.ViewHolder> {
    private Context context;
    private List<Users> mUsers;
    private String theLastMessage;
    public SellerChatsAdapter(Context context, List<Users> mUsers){
        this.context = context;
        this.mUsers = mUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_item_row,parent,false);

        return new SellerChatsAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users user = mUsers.get(position);
        holder.UserName.setText(user.getUserName());
        Glide.with(context).load(user.getImgUri()).into(holder.UserImg);
        lastMessage(user.getUser_ID(), holder.LastMsg);
        holder.ChatCard.setOnClickListener(v -> {
           /* Intent intent = new Intent(context, Message.class);
            intent.putExtra("UniqueID","from_SellerChatsAdapter");
            intent.putExtra("user_id",user.getUser_ID());
            context.startActivity(intent);
            ((Activity)context).overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            */

            Message fragment = new Message();
            Bundle args = new Bundle();
            args.putString("UniqueID", "from_SellerChatsAdapter");
            args.putString("user_id",user.getUser_ID());
            fragment.setArguments(args);
            FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction1 = manager.beginTransaction();
            fragmentTransaction1.replace(R.id.Frame_Content, fragment);
            fragmentTransaction1.commit();
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public void RemoveChat(int position){
        mUsers.remove(position);
        notifyItemRemoved(position);
    }

    public void RestoreChat(Users user, int position){
        mUsers.add(position,user);
        notifyItemInserted(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView UserName,LastMsg;
        CircleImageView UserImg;
        CardView ChatCard;
        ConstraintLayout view_foreground, view_background;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            UserName = itemView.findViewById(R.id.user_username_row);
            LastMsg = itemView.findViewById(R.id.user_last_msg_row);
            UserImg = itemView.findViewById(R.id.user_profile_img_row);
            ChatCard = itemView.findViewById(R.id.chat_card);
            view_foreground = itemView.findViewById(R.id.view_foreground);
            view_background = itemView.findViewById(R.id.view_background);
        }
    }
    //check for last message
    private void lastMessage(final String userid, final TextView last_msg){
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatSeller chatSeller = snapshot.getValue(ChatSeller.class);
                    if (firebaseUser != null && chatSeller != null) {
                        if (chatSeller.getReceiver().equals(firebaseUser.getUid()) && chatSeller.getSender().equals(userid) ||
                                chatSeller.getReceiver().equals(userid) && chatSeller.getSender().equals(firebaseUser.getUid())) {
                            theLastMessage = chatSeller.getMessage();
                        }

                    }
                }

                if ("default".equals(theLastMessage)) {
                    last_msg.setText("No Message");
                } else {
                    last_msg.setText(theLastMessage);
                }

                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
