package com.elasdka2.zar3tyseller.Adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.elasdka2.zar3tyseller.DiscountBottomSheetDialog;
import com.elasdka2.zar3tyseller.ItemInfo;
import com.elasdka2.zar3tyseller.Model.OrderItems;
import com.elasdka2.zar3tyseller.Model.SellerItems;
import com.elasdka2.zar3tyseller.Personal;
import com.elasdka2.zar3tyseller.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.MyViewHolder> implements DiscountBottomSheetDialog.BottomSheetListener {
    List<OrderItems> itemsList;
    private ArrayList<OrderItems> orderslist;
    Context context;
    DatabaseReference OrdersRef;
    public OrdersAdapter(ArrayList<OrderItems> orderslist, Context context) {
        this.orderslist = orderslist;
        this.itemsList = new ArrayList<>();
        this.context = context;
    }
    public void AddAll(List<OrderItems> newItem){
        int initsize = itemsList.size();
        itemsList.addAll(newItem);
        notifyItemRangeChanged(initsize,newItem.size());
    }
    public String getLastItemID(){
        return itemsList.get(itemsList.size()-1).getKey();
    }
    public void RemoveLastItem(){

        itemsList.remove(itemsList.size() -1);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View ItemView = LayoutInflater.from(context).inflate(R.layout.order_row,parent,false);
        OrdersRef = FirebaseDatabase.getInstance().getReference("Orders");

        return new MyViewHolder(ItemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Animation myanim= AnimationUtils.loadAnimation(context,R.anim.stb2);
        holder.Card_Item.startAnimation(myanim);

        Glide.with(context.getApplicationContext()).load(orderslist.get(position).getCustomerimg()).into(holder.CustomerImg);
        Glide.with(context.getApplicationContext()).load(orderslist.get(position).getItemimg()).into(holder.Item_Img);

        holder.CustomerName.setText(orderslist.get(position).getCustomername());
        holder.RequestDate.setText(orderslist.get(position).getDate());
        holder.Item_Title.setText(orderslist.get(position).getTitle());
        holder.Item_Price.setText(orderslist.get(position).getPrice());

        holder.AcceptBtn.setOnClickListener(v -> {
            FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
            Bundle args = new Bundle();
            args.putString("ItemTitle",orderslist.get(position).getTitle());
            args.putString("ItemPrice",orderslist.get(position).getPrice());
            args.putString("ItemImg",orderslist.get(position).getItemimg());
            args.putString("ItemQuantity",orderslist.get(position).getQuantity());
            args.putString("CustomerID",orderslist.get(position).getCustomerid());
            args.putString("SellerID",orderslist.get(position).getSellerid());
            args.putString("RequestDate",orderslist.get(position).getDate());

            DiscountBottomSheetDialog discountBottomSheetDialog = new DiscountBottomSheetDialog();
            discountBottomSheetDialog.setArguments(args);

            discountBottomSheetDialog.show(manager,"");
        });

        holder.RejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(v.getRootView().getContext()).create();
                alertDialog.setTitle("Warning !");
                alertDialog.setMessage("Reject " +holder.Item_Title.getText().toString());

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "REJECT",
                        (dialog, which) -> {
                            Map<String, String> OrdersMap = new HashMap<>();
                            OrdersMap.put("CustomerID", orderslist.get(position).getCustomerid());
                            OrdersMap.put("ItemImg", orderslist.get(position).getItemimg());
                            OrdersMap.put("ItemTitle", orderslist.get(position).getTitle());
                            OrdersMap.put("ItemPrice", orderslist.get(position).getPrice());
                            OrdersMap.put("ItemQuantity", orderslist.get(position).getQuantity());
                            OrdersMap.put("RequestDate", orderslist.get(position).getDate());
                            OrdersMap.put("SellerID", orderslist.get(position).getSellerid());
                            OrdersMap.put("State", "Rejected");
                            OrdersRef.push().setValue(OrdersMap).addOnCompleteListener(task -> {
                                if (task.isSuccessful()){

                                }
                            });
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", (dialog, which) -> dialog.dismiss());

                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (orderslist == null) {
            return 0;
        }
        return orderslist.size();
    }

    @Override
    public void onButtonClicked(String text) {

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Item_Title, Item_Price,CustomerName,RequestDate;
        ImageView Item_Img; // Karim
        CircleImageView CustomerImg;
        CardView Card_Item;
        Button AcceptBtn, RejectBtn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Item_Img = itemView.findViewById(R.id.order_item_img_row); // Karim
            Item_Title = itemView.findViewById(R.id.order_item_title_row); // Karim
            RequestDate = itemView.findViewById(R.id.order_request_date_value_row);
            CustomerName = itemView.findViewById(R.id.order_customer_name_row);
            CustomerImg = itemView.findViewById(R.id.order_customer_img_row);
            Item_Price = itemView.findViewById(R.id.order_item_price_row); // Karim
            Card_Item = itemView.findViewById(R.id.order_card_item_row);
            AcceptBtn = itemView.findViewById(R.id.order_accept_btn_row);
            RejectBtn = itemView.findViewById(R.id.order_reject_btn_row);
        }
    }
}