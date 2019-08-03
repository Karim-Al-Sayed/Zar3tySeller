package com.elasdka2.zar3tyseller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.elasdka2.zar3tyseller.Adapters.OrdersAdapter;
import com.elasdka2.zar3tyseller.Helper.OrdersRecylcerItemTouchHelper;
import com.elasdka2.zar3tyseller.Model.OrderItems;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class myOrders extends AppCompatActivity {
    Context context;
    DatabaseReference PendingRef;
    FirebaseAuth mAuth;
    ArrayList<OrderItems> orderList;
    OrdersAdapter ordersAdapter;
    @BindView(R.id.Orders_Recycler)
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        context = myOrders.this;
        ButterKnife.bind(this);
        orderList = new ArrayList<>();
        GridLayoutManager linearLayoutManager = new GridLayoutManager(context,1);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAuth = FirebaseAuth.getInstance();
        PendingRef = FirebaseDatabase.getInstance().getReference("Pending Requests");
        PendingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    String title = ds.child("ItemTitle").getValue(String.class);
                    String price = ds.child("ItemPrice").getValue(String.class);
                    String quantity = ds.child("ItemQuantity").getValue(String.class);
                    String request_date = ds.child("RequestDate").getValue(String.class);
                    String customerID = ds.child("CustomerID").getValue(String.class);
                    String customerName = ds.child("CustomerName").getValue(String.class);
                    String customerImg = ds.child("CustomerImg").getValue(String.class);
                    String itemImg = ds.child("ItemIMG").getValue(String.class);

                    OrderItems orderItems = new OrderItems();
                    orderItems.setCustomerid(customerID);
                    orderItems.setDate(request_date);
                    orderItems.setSellerid(mAuth.getCurrentUser().getUid());
                    orderItems.setQuantity(quantity);
                    orderItems.setPrice(price);
                    orderItems.setKey(ds.getKey());
                    orderItems.setCustomername(customerName);
                    orderItems.setCustomerimg(customerImg);
                    orderItems.setTitle(title);
                    orderItems.setItemimg(itemImg);
                    orderList.add(orderItems);
                }
                ordersAdapter = new OrdersAdapter(orderList,context);
                recyclerView.setAdapter(ordersAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
       /* ItemTouchHelper.SimpleCallback itemTouchHelperCallback
                = new OrdersRecylcerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback2
                = new OrdersRecylcerItemTouchHelper(0,ItemTouchHelper.RIGHT,this);
        new ItemTouchHelper(itemTouchHelperCallback2).attachToRecyclerView(recyclerView);
*/

    }

  /*  @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (direction == ItemTouchHelper.RIGHT){

        }
    }*/

}
