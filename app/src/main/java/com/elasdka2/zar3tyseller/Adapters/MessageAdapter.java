package com.elasdka2.zar3tyseller.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elasdka2.zar3tyseller.Model.Chat;
import com.elasdka2.zar3tyseller.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder>  {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    Context context;
    private List<Chat> mChat;
    FirebaseUser MyUser;

    public MessageAdapter(Context context, List<Chat> mChat) {
        this.mChat = mChat;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT){
            View ItemView = LayoutInflater.from(context).inflate(R.layout.chat_item_right,parent,false);
            return new MyViewHolder(ItemView);
        }else {
            View ItemView = LayoutInflater.from(context).inflate(R.layout.chat_item_left,parent,false);
            return new MyViewHolder(ItemView);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Chat chat = mChat.get(position);
        holder.message.setText(chat.getMessage());
       // holder.username.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView message, username;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.show_message);
            username = itemView.findViewById(R.id.chat_item_left_user_name);
        }
    }

    @Override
    public int getItemViewType(int position) {
        MyUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(MyUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }
}
