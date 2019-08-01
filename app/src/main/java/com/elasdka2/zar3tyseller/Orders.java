package com.elasdka2.zar3tyseller;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.elasdka2.zar3tyseller.Adapters.OrdersAdapter;
import com.elasdka2.zar3tyseller.Helper.ChatsSellerRecylcerItemTouchHelper;
import com.elasdka2.zar3tyseller.Helper.OrdersRecylcerItemTouchHelper;
import com.elasdka2.zar3tyseller.Helper.RecyclerItemTouchHelperListener;
import com.elasdka2.zar3tyseller.Model.OrderItems;
import com.elasdka2.zar3tyseller.Model.SellerItems;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Orders extends Fragment implements DiscountBottomSheetDialog.BottomSheetListener,
                                                RecyclerItemTouchHelperListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Orders() {
        // Required empty public constructor
    }
    //--------------------------------
    Context context;
    DatabaseReference PendingRef;
    FirebaseAuth mAuth;
    ArrayList<OrderItems> orderList;
    OrdersAdapter ordersAdapter;
    @BindView(R.id.Orders_Recycler)
    RecyclerView recyclerView;

    // TODO: Rename and change types and number of parameters
    public static Orders newInstance(String param1, String param2) {
        Orders fragment = new Orders();
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
        View v = inflater.inflate(R.layout.orders_frag, container, false);
        context = getActivity();
        ButterKnife.bind(this, v);
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
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback
                = new OrdersRecylcerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback2
                = new OrdersRecylcerItemTouchHelper(0,ItemTouchHelper.RIGHT,this);
        new ItemTouchHelper(itemTouchHelperCallback2).attachToRecyclerView(recyclerView);

        return v;
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

    @Override
    public void onButtonClicked(String text) {

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (direction == ItemTouchHelper.RIGHT){

        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
