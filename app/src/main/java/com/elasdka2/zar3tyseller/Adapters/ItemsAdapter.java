package com.elasdka2.zar3tyseller.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.elasdka2.zar3tyseller.ItemInfo;
import com.elasdka2.zar3tyseller.Model.SellerItems;
import com.elasdka2.zar3tyseller.MyItems;
import com.elasdka2.zar3tyseller.R;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder>  {
    List<SellerItems> itemsList;
    private ArrayList<SellerItems> sellerlist;
    Context context;
    public ItemsAdapter(ArrayList<SellerItems> sellerlist, Context context) {
        this.sellerlist = sellerlist;
        this.itemsList = new ArrayList<>();
        this.context = context;
    }
    public void AddAll(List<SellerItems> newItem){
        int initsize = itemsList.size();
        itemsList.addAll(newItem);
        notifyItemRangeChanged(initsize,newItem.size());
    }
    public String getLastItemID(){
        return itemsList.get(itemsList.size()-1).getId();
    }
    public void RemoveLastItem(){

        itemsList.remove(itemsList.size() -1);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View ItemView = LayoutInflater.from(context).inflate(R.layout.seller_item_row,parent,false);
        return new MyViewHolder(ItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Animation myanim= AnimationUtils.loadAnimation(context,R.anim.stb2);
        holder.Card_Item.startAnimation(myanim);
        holder.Item_Title.setText(sellerlist.get(position).getTitle());
        holder.Item_Description.setText(sellerlist.get(position).getDescription());
        holder.Item_Price.setText(sellerlist.get(position).getPrice());
        holder.Category.setText(sellerlist.get(position).getCategory());
        holder.Item_Date.setText(sellerlist.get(position).getDate());
        holder.Item_Date_To_Show.setText(sellerlist.get(position).getDate_to_show());
        Glide.with(context.getApplicationContext()).load(sellerlist.get(position).getImg_uri()).into(holder.Item_Img);

        holder.Card_Item.setOnClickListener(v -> {

            ItemInfo fragment = new ItemInfo();
            Bundle args = new Bundle();
            args.putString("ItemTitle", holder.Item_Title.getText().toString());
            args.putString("ItemDescription",holder.Item_Description.getText().toString());
            args.putString("ItemPrice",holder.Item_Price.getText().toString());
            args.putString("ItemImg", sellerlist.get(position).getImg_uri());
            args.putString("ItemDate",holder.Item_Date_To_Show.getText().toString());
            args.putString("UploadDate",sellerlist.get(position).getDate());
            args.putString("ItemCategory",holder.Category.getText().toString());
            args.putString("ItemMainCategory",sellerlist.get(position).getMaincategory());
            args.putString("UniqueID","from_ItemsAdapter");
            fragment.setArguments(args);

            FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction1 = manager.beginTransaction();
            fragmentTransaction1.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
            fragmentTransaction1.replace(R.id.Frame_Content, fragment, "ItemInfo");
            fragmentTransaction1.commit();



        });

    }

    @Override
    public int getItemCount() {
        return sellerlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Item_Title, Item_Price, Item_Description,Item_Date,Item_Date_To_Show,
                Category,UserEmail,UserPhone,UserID;
        ImageView Item_Img; // Karim
        CardView Card_Item;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Item_Img = itemView.findViewById(R.id.seller_item_img_row); // Karim
            Item_Title = itemView.findViewById(R.id.seller_item_title_row); // Karim
            Item_Description = itemView.findViewById(R.id.seller_item_description_row); // Karim
            Item_Price = itemView.findViewById(R.id.seller_item_price_row); // Karim
            Item_Date = itemView.findViewById(R.id.seller_item_date_row); // Karim
            Item_Date_To_Show = itemView.findViewById(R.id.seller_item_date_to_show_row); // Karim
            UserID = itemView.findViewById(R.id.seller_item_user_id_row);
            Category = itemView.findViewById(R.id.seller_item_category_row); // Karim
            UserEmail = itemView.findViewById(R.id.seller_item_user_email_row); // Karim
            UserPhone = itemView.findViewById(R.id.seller_item_user_phone_row); // Karim
            Card_Item = itemView.findViewById(R.id.seller_card_item_row);

        }
    }
}