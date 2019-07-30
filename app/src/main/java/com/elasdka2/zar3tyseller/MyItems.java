package com.elasdka2.zar3tyseller;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elasdka2.zar3tyseller.Adapters.ItemsAdapter;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyItems.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyItems#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyItems extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    //---------------------------------------------
    Context context;
    String CurrentUserID;

    private DatabaseReference Sales_Ref;
    private FirebaseAuth SalesAuth;
    ArrayList<SellerItems> sellerlist;
    @BindView(R.id.seller_recycler_frag)
    RecyclerView seller_recycler;
    @BindView(R.id.no_item_img)
    ImageView no_item_img;
    @BindView(R.id.no_item_txt)
    TextView no_item_text;
    //---------------------------------------------
    final int ITEM_LOAD_COUNT = 2;
    int total_item = 0, last_visible_item;
    boolean isLoading = false, isMaxData = false;
    String last_node = "", last_key = "";
    ItemsAdapter adapter;

    //---------------------------------------------
    public MyItems() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyItems.
     */
    // TODO: Rename and change types and number of parameters
    public static MyItems newInstance(String param1, String param2) {
        MyItems fragment = new MyItems();
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
        View view = inflater.inflate(R.layout.my_items_frag, container, false);
        context = view.getContext();
        ButterKnife.bind(this, view);
        sellerlist = new ArrayList<>();
        SalesAuth = FirebaseAuth.getInstance();
        Sales_Ref = FirebaseDatabase.getInstance().getReference("Sales");
        CurrentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        GridLayoutManager linearLayoutManager = new GridLayoutManager(context,2);
        seller_recycler.setLayoutManager(linearLayoutManager);

        Sales_Ref.child("Agriculture").orderByChild("User_ID").equalTo(CurrentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String item_title = dataSnapshot1.child("Item_Title").getValue(String.class);
                            String item_price = dataSnapshot1.child("Item_Price").getValue(String.class);
                            String item_description = dataSnapshot1.child("Item_Description").getValue(String.class);
                            String category = dataSnapshot1.child("Category").getValue(String.class);
                            // String item_quantity = dataSnapshot1.child("Item_Quantity").getValue().toString();
                            String id = dataSnapshot1.child("User_ID").getValue(String.class);
                            String upload_date = dataSnapshot1.child("Upload_Date").getValue(String.class);
                            String upload_date_show = dataSnapshot1.child("Upload_Date_To_Show").getValue(String.class);
                            String img_uri = dataSnapshot1.child("Img_Uri").getValue(String.class);

                            SellerItems items = new SellerItems();
                            items.setTitle(item_title);
                            items.setPrice(item_price);
                            items.setDescription(item_description);
                            items.setCategory(category);
                            items.setMaincategory("Agriculture");
                            items.setId(id);
                            items.setDate(upload_date);
                            items.setDate_to_show(upload_date_show);
                            items.setImg_uri(img_uri);
                            sellerlist.add(items);

                        }
                        ItemsAdapter adapter = new ItemsAdapter(sellerlist,getActivity());
                        seller_recycler.setAdapter(adapter);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Sales_Ref.child("Irrigation").orderByChild("User_ID").equalTo(CurrentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String item_title = dataSnapshot1.child("Item_Title").getValue(String.class);
                            String item_price = dataSnapshot1.child("Item_Price").getValue(String.class);
                            String item_description = dataSnapshot1.child("Item_Description").getValue(String.class);
                            String category = dataSnapshot1.child("Category").getValue(String.class);
                            // String item_quantity = dataSnapshot1.child("Item_Quantity").getValue().toString();
                            String id = dataSnapshot1.child("User_ID").getValue(String.class);
                            String upload_date = dataSnapshot1.child("Upload_Date").getValue(String.class);
                            String upload_date_show = dataSnapshot1.child("Upload_Date_To_Show").getValue(String.class);
                            String img_uri = dataSnapshot1.child("Img_Uri").getValue(String.class);

                            SellerItems items = new SellerItems();
                            items.setTitle(item_title);
                            items.setPrice(item_price);
                            items.setDescription(item_description);
                            items.setCategory(category);
                            items.setMaincategory("Irrigation");
                            items.setId(id);
                            items.setDate(upload_date);
                            items.setDate_to_show(upload_date_show);
                            items.setImg_uri(img_uri);
                            sellerlist.add(items);
                        }
                        ItemsAdapter adapter = new ItemsAdapter(sellerlist,getActivity());
                        seller_recycler.setAdapter(adapter);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Sales_Ref.child("Industrial_Agriculture").orderByChild("User_ID").equalTo(CurrentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String item_title = dataSnapshot1.child("Item_Title").getValue(String.class);
                            String item_price = dataSnapshot1.child("Item_Price").getValue(String.class);
                            String item_description = dataSnapshot1.child("Item_Description").getValue(String.class);
                            String category = dataSnapshot1.child("Category").getValue(String.class);
                            // String item_quantity = dataSnapshot1.child("Item_Quantity").getValue().toString();
                            String id = dataSnapshot1.child("User_ID").getValue(String.class);
                            String upload_date = dataSnapshot1.child("Upload_Date").getValue(String.class);
                            String upload_date_show = dataSnapshot1.child("Upload_Date_To_Show").getValue(String.class);
                            String img_uri = dataSnapshot1.child("Img_Uri").getValue(String.class);

                            SellerItems items = new SellerItems();
                            items.setTitle(item_title);
                            items.setPrice(item_price);
                            items.setDescription(item_description);
                            items.setCategory(category);
                            items.setMaincategory("Industrial_Agriculture");
                            items.setId(id);
                            items.setDate(upload_date);
                            items.setDate_to_show(upload_date_show);
                            items.setImg_uri(img_uri);
                            sellerlist.add(items);
                        }
                        ItemsAdapter adapter = new ItemsAdapter(sellerlist,getActivity());
                        seller_recycler.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                /*Thread thread = new Thread(() -> {
                    if (sellerlist.size() == 0){
                        seller_recycler.setVisibility(View.GONE);
                        no_item_img.setVisibility(View.VISIBLE);
                        no_item_text.setVisibility(View.VISIBLE);
                    }else {
                        seller_recycler.setVisibility(View.VISIBLE);
                        no_item_img.setVisibility(View.GONE);
                        no_item_text.setVisibility(View.GONE);
                    }
                });
                thread.start();*/

        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }



    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {

            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                Personal fragment = new Personal();
                FragmentTransaction fragmentTransaction1 = getFragmentManager().beginTransaction();
                fragmentTransaction1.replace(R.id.Frame_Content, fragment, "Personal");
                fragmentTransaction1.commit();

                return true;

            }

            return false;
        });
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
