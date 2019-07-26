package com.elasdka2.zar3tyseller;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ItemInfo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ItemInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemInfo extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    //----------------------------------------------
    private FirebaseAuth mAuth;
    private DatabaseReference MyRef;
    private Uri uri;
    String MainCategory, UploadDate, DateToShow, intent_from, part1, part2;
    Context context;
    int str_total_price;

    @BindView(R.id.sales_item_info_img)
    ImageView item_info_img;
    @BindView(R.id.sales_item_info_title_value)
    TextView item_info_title;
    @BindView(R.id.sales_item_info_description_value)
    TextView item_info_description;
    @BindView(R.id.sales_item_info_price_value)
    TextView item_info_price;
    @BindView(R.id.sales_item_info_category_value)
    TextView item_info_category;
    @BindView(R.id.sales_item_info_quantity_value)
    TextView item_info_quantity;
    @BindView(R.id.sales_item_info_date_value)
    TextView item_info_date;
    @BindView(R.id.sales_item_info_update)
    Button item_info_update;
    //----------------------------------------------
    @OnClick(R.id.sales_item_info_update)
    public void UpdateItem(){
        //Toast.makeText(context,getIntent().getExtras().getString("ItemKey"),Toast.LENGTH_SHORT).show();
        UpdateItemData fragment = new UpdateItemData();
        Bundle args = new Bundle();
        args.putString("ItemImg", getArguments().getString("ItemImg"));
        args.putString("ItemTitle", item_info_title.getText().toString());
        args.putString("ItemDescription", item_info_description.getText().toString());
        args.putString("ItemPrice", part1);
        args.putString("ItemMainCategory", MainCategory);
        args.putString("ItemUploadDate", UploadDate);
        args.putString("ItemDateToShow", DateToShow);
        fragment.setArguments(args);

        FragmentTransaction fragmentTransaction1 = getFragmentManager().beginTransaction();
        fragmentTransaction1.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        fragmentTransaction1.replace(R.id.Frame_Content, fragment, "UpdateItemData");
        fragmentTransaction1.commit();
        /*Intent intent = new Intent(context,SellerItemUpdate.class);
        intent.putExtra("UserID",UserID);
        intent.putExtra("ItemKey",ItemKey);
        intent.putExtra("ItemTitle",seller_item_info_title.getText().toString());
        intent.putExtra("ItemDescription",seller_item_info_description.getText().toString());
        String string = seller_item_info_price.getText().toString();
        String[] parts = string.split(" ");
        String part1 = parts[0]; // Money
        String part2 = parts[1]; // LE
        intent.putExtra("ItemPrice",part1);
        intent.putExtra("ItemDate",seller_item_info_date.getText().toString());
        intent.putExtra("ItemImg",getIntent().getExtras().getString("ItemImg"));
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        finish();*/
    }
    //----------------------------------------------
    public ItemInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ItemInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemInfo newInstance(String param1, String param2) {
        ItemInfo fragment = new ItemInfo();
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
        View v = inflater.inflate(R.layout.item_info_frag, container, false);
        context = getActivity();
        ButterKnife.bind(this, v);

        mAuth = FirebaseAuth.getInstance();
        MyRef = FirebaseDatabase.getInstance().getReference("Sales");

        intent_from = getArguments().getString("UniqueID");
        if (getArguments() != null){
            if (!TextUtils.isEmpty(intent_from)){
                if (intent_from.equals("from_ItemsAdapter")){
                    item_info_title.setText(getArguments().getString("ItemTitle"));
                    item_info_description.setText(getArguments().getString("ItemDescription"));
                    item_info_price.setText(getArguments().getString("ItemPrice"));

                    String string = item_info_price.getText().toString();
                    String[] parts = string.split(" ");
                    part1 = parts[0]; // Money
                    part2 = parts[1]; // LE

                    item_info_category.setText(getArguments().getString("ItemCategory"));
                    item_info_date.setText(getArguments().getString("ItemDate"));
                    MainCategory = getArguments().getString("ItemMainCategory");
                    UploadDate = getArguments().getString("UploadDate");
                    DateToShow = getArguments().getString("ItemDate");
                    Glide.with(context.getApplicationContext()).load(getArguments().getString("ItemImg")).into(item_info_img);
                }else if (intent_from.equals("from_UpdateItemData")){
                    item_info_title.setText(getArguments().getString("ItemTitle"));
                    item_info_description.setText(getArguments().getString("ItemDescription"));
                    item_info_price.setText(getArguments().getString("ItemPrice"));
                    String string = item_info_price.getText().toString();
                    String[] parts = string.split(" ");
                    part1 = parts[0]; // Money
                    part2 = parts[1]; // LE
                    item_info_category.setText(getArguments().getString("ItemCategory"));
                    item_info_date.setText(getArguments().getString("ItemDate"));
                    MainCategory = getArguments().getString("ItemMainCategory");
                    UploadDate = getArguments().getString("UploadDate");
                    DateToShow = getArguments().getString("ItemDate");
                    Glide.with(context.getApplicationContext()).load(getArguments().getString("ItemImg")).into(item_info_img);
                }
            }
        }


        return v;
    }
    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {

            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                MyItems fragment = new MyItems();
                FragmentTransaction fragmentTransaction1 = getFragmentManager().beginTransaction();
                fragmentTransaction1.replace(R.id.Frame_Content, fragment, "MyItems");
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
