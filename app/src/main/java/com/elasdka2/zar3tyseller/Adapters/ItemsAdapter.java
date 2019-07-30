package com.elasdka2.zar3tyseller.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.elasdka2.zar3tyseller.ItemInfo;
import com.elasdka2.zar3tyseller.Model.SellerItems;
import com.elasdka2.zar3tyseller.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder>  {
    List<SellerItems> itemsList;
    private ArrayList<SellerItems> sellerlist;
    Context context;
    DatabaseReference SalesRef;
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
        SalesRef = FirebaseDatabase.getInstance().getReference("Sales");

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
            notifyItemRangeChanged(position, getItemCount());
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

        holder.SettingBtn.setOnClickListener(v -> {
            //Creating the instance of PopupMenu
            PopupMenu popup = new PopupMenu(context.getApplicationContext(), holder.SettingBtn);
            //Inflating the Popup using xml file
            popup.getMenuInflater()
                    .inflate(R.menu.setting_menu, popup.getMenu());

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(item -> {
                AlertDialog alertDialog = new AlertDialog.Builder(v.getRootView().getContext()).create();
                alertDialog.setTitle("Warning !");
                alertDialog.setMessage("Delete " +holder.Item_Title.getText().toString());

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "DELETE",
                        (dialog, which) -> SalesRef.child(sellerlist.get(position).getMaincategory())
                                .child(sellerlist.get(position).getDate()).removeValue().addOnCompleteListener(task -> {
                                    if (task.isSuccessful()){
                                        Toast.makeText(context.getApplicationContext(),
                                                sellerlist.get(position).getTitle() + " has been deleted successfully",
                                                Toast.LENGTH_SHORT).show();
                                        sellerlist.remove(position);
                                        sellerlist.notify();
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, getItemCount());
                                    }
                                }));
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", (dialog, which) -> dialog.dismiss());

                alertDialog.show();
                return true;
            });

            popup.show(); //showing popup menu
        }); //closing the setOnClickListener method
        holder.Card_Item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                    AlertDialog alertDialog = new AlertDialog.Builder(v.getRootView().getContext()).create();
                    alertDialog.setTitle("Warning !");
                    alertDialog.setMessage("Delete " +holder.Item_Title.getText().toString());

                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "DELETE",
                            (dialog, which) -> SalesRef.child(sellerlist.get(position).getMaincategory())
                                    .child(sellerlist.get(position).getDate()).removeValue().addOnCompleteListener(task -> {
                                        if (task.isSuccessful()){
                                            Toast.makeText(context.getApplicationContext(),
                                                    sellerlist.get(position).getTitle() + " has been deleted successfully",
                                                    Toast.LENGTH_SHORT).show();
                                            sellerlist.remove(position);
                                            notifyItemRemoved(position);
                                        }
                                    }));
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", (dialog, which) -> dialog.dismiss());

                    alertDialog.show();
                    return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (sellerlist == null) {
            return 0;
        }
        return sellerlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Item_Title, Item_Price,Item_Description,Item_Date,Item_Date_To_Show,Category;
        ImageView Item_Img; // Karim
        CardView Card_Item;
        ImageButton SettingBtn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Item_Img = itemView.findViewById(R.id.seller_item_img_row); // Karim
            Item_Title = itemView.findViewById(R.id.seller_item_title_row); // Karim
            Item_Description = itemView.findViewById(R.id.seller_item_description_row);
            Item_Date = itemView.findViewById(R.id.seller_item_date_row);
            Item_Date_To_Show = itemView.findViewById(R.id.seller_item_date_to_show_row);
            Category = itemView.findViewById(R.id.seller_item_category_row);
            Item_Price = itemView.findViewById(R.id.seller_item_price_row); // Karim
            Card_Item = itemView.findViewById(R.id.seller_card_item_row);
            SettingBtn = itemView.findViewById(R.id.setting_item_btn);
        }
    }
}