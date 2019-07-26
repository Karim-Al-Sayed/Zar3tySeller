package com.elasdka2.zar3tyseller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddItem.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddItem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddItem extends Fragment implements AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //---------------------------------
    @BindView(R.id.sales_item_img)
    ImageView Item_Img;
    @BindView(R.id.sales_item_title)
    EditText Item_Title;
    @BindView(R.id.sales_item_description)
    EditText Item_Description;
    @BindView(R.id.sales_item_price)
    EditText Item_Price;
    @BindView(R.id.sales_item_category_agriculture_spinner)
    Spinner Agriculture_Category;
    @BindView(R.id.sales_item_category_irrigation_spinner)
    Spinner Irrigation_Category;
    @BindView(R.id.sales_item_category_industrial_agriculture_spinner)
    Spinner Industrial_Agriculture_Category;
    @BindView(R.id.sales_upload_btn)
    Button Upload;
    //---------------------------------
    //---------------------------------
    Context context;

    private static final int GALLERY_INTENT = 1;
    private Uri ImgUri;
    private FirebaseAuth SalesAuth;
    private DatabaseReference Sales_Database_Ref,Users_Ref;
    private StorageReference Sales_Storage_Ref;

    private static final String[] agriculture_paths = {"نخل", "شجر", "شجيرات", "مغطيات ترب", "مغذيات و وقاية"};
    private static final String[] irrigation_paths = {"لوح كنترول", "مواسير", "خراطيم", "رشاشات", "محابس", "وصلات", "مضخات"};
    private static final String[] industrial_agriculture_paths = {"نخل", "شجر", "شجيرات", "مغطيات ترب"};

    boolean IsPhotoExist;
    boolean doubleBackToExitPressedOnce = false;
    String Category, IntentFrom, MainCategory, SubCategory;
    Calendar calendar = Calendar.getInstance();
    ProgressDialog SalesProgressDialog;
    //---------------------------------

    public AddItem() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddItem.
     */
    // TODO: Rename and change types and number of parameters
    public static AddItem newInstance(String param1, String param2) {
        AddItem fragment = new AddItem();
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
        View v = inflater.inflate(R.layout.add_item_frag, container, false);
        context = getActivity();
        ButterKnife.bind(this, v);

        if (getArguments() != null) {
            IntentFrom = getArguments().getString("UniqueID");
            if (IntentFrom != null && IntentFrom.equals("SelectCategory")) {
                Category = getArguments().getString("Category") ;
            }


        }
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        IsPhotoExist = false;

        SalesProgressDialog = new ProgressDialog(context);
        SalesProgressDialog.setCanceledOnTouchOutside(false);
        SalesProgressDialog.setCancelable(false);
        SalesProgressDialog.setMessage("Uploading Your Item ...");

        if (Category.equals("Agriculture")){
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                    android.R.layout.simple_spinner_item, agriculture_paths);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Agriculture_Category.setAdapter(adapter);
            Agriculture_Category.setOnItemSelectedListener(AddItem.this);
            MainCategory = "Agriculture";
            Irrigation_Category.setVisibility(View.GONE);
            Industrial_Agriculture_Category.setVisibility(View.GONE);
            Agriculture_Category.setVisibility(View.VISIBLE);

        }else if (Category.equals("Irrigation")){
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                    android.R.layout.simple_spinner_item, irrigation_paths);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Irrigation_Category.setAdapter(adapter);
            Irrigation_Category.setOnItemSelectedListener(AddItem.this);
            MainCategory = "Irrigation";
            Agriculture_Category.setVisibility(View.GONE);
            Industrial_Agriculture_Category.setVisibility(View.GONE);
            Irrigation_Category.setVisibility(View.VISIBLE);

        }else if (Category.equals("Industrial_Agriculture")){
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                    android.R.layout.simple_spinner_item, industrial_agriculture_paths);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Industrial_Agriculture_Category.setAdapter(adapter);
            Industrial_Agriculture_Category.setOnItemSelectedListener(AddItem.this);
            MainCategory = "Industrial_Agriculture";
            Irrigation_Category.setVisibility(View.GONE);
            Agriculture_Category.setVisibility(View.GONE);
            Industrial_Agriculture_Category.setVisibility(View.VISIBLE);

        }

        SalesAuth = FirebaseAuth.getInstance();
        Sales_Database_Ref = FirebaseDatabase.getInstance().getReference("Sales");
        Users_Ref = FirebaseDatabase.getInstance().getReference("Users");
        Sales_Storage_Ref = FirebaseStorage.getInstance().getReference("Sales");

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.sales_item_category_agriculture_spinner) {
            switch (position) {
                case 0:
                    SubCategory = "نخل";
                    break;
                case 1:
                    SubCategory = "شجر";
                    break;
                case 2:
                    SubCategory = "شجيرات";
                    break;
                case 3:
                    SubCategory = "مغطيات ترب";
                    break;
                case 4:
                    SubCategory = "مغذيات و وقايه";
                    break;

            }
        }else if (parent.getId() == R.id.sales_item_category_irrigation_spinner){
            switch (position) {
                case 0:
                    SubCategory = "لوح كنترول";
                    break;
                case 1:
                    SubCategory = "مواسير";
                    break;
                case 2:
                    SubCategory = "خراطيم";
                    break;
                case 3:
                    SubCategory = "رشاشات";
                    break;
                case 4:
                    SubCategory = "محابس";
                    break;
                case 5:
                    SubCategory = "وصلات";
                    break;
                case 6:
                    SubCategory = "مضخات";
                    break;
            }
        }else if (parent.getId() == R.id.sales_item_category_industrial_agriculture_spinner){
            switch (position) {
                case 0:
                    SubCategory = "نخل";
                    break;
                case 1:
                    SubCategory = "شجر";
                    break;
                case 2:
                    SubCategory = "شجيرات";
                    break;
                case 3:
                    SubCategory = "مغطيات ترب";
                    break;

            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @OnClick(R.id.sales_item_img)
    public void UploadItemPhoto(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select"), GALLERY_INTENT);
    }

    @OnClick(R.id.sales_upload_btn)
    public void UploadItem(){
        CheckDataAndUpload();
    }

    private void CheckDataAndUpload() {
        String str_title = Item_Title.getText().toString().trim();
        String str_description = Item_Description.getText().toString().trim();
        String str_price = Item_Price.getText().toString().trim();
        // String str_quantity = Item_Quantity.getText().toString().trim();

        if (!IsPhotoExist){
            Toast.makeText(context,"Add Item Img Please !",Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(str_title)){
            Toast.makeText(context,"Add Item Title Please !",Toast.LENGTH_LONG).show();
            Item_Title.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(str_description)){
            Toast.makeText(context,"Add Item Description Please !",Toast.LENGTH_LONG).show();
            Item_Description.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(str_price)){
            Toast.makeText(context,"Add Item Price Please !",Toast.LENGTH_LONG).show();
            Item_Price.requestFocus();
            return;
        }
      /*  if (TextUtils.isEmpty(str_quantity)){
            Toast.makeText(SalesContext,"Add Item Quantity Please !",Toast.LENGTH_LONG).show();
            Item_Quantity.requestFocus();
            return;
        }*/
        UploadDataToFirebase();
    }

    private void UploadDataToFirebase() {
        SalesProgressDialog.show();
        String id = SalesAuth.getCurrentUser().getUid();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy,MM,dd,HH,mm,ss,SSS");
        Date now = new Date();
        String strDate = sdf.format(now);

        String currentDate= DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        Sales_Storage_Ref.child("Items_Images/" + id + "/" + Item_Description.getText().toString().trim())
                .putFile(ImgUri).addOnSuccessListener(taskSnapshot -> {
            //  Toast.makeText(SalesContext, "uploaded", Toast.LENGTH_SHORT).show();
            Map<String, String> SalesMap = new HashMap<>();
            SalesMap.put("Item_Title", Item_Title.getText().toString());
            SalesMap.put("Item_Description", Item_Description.getText().toString());
            SalesMap.put("Item_Price", Item_Price.getText().toString()+" EGP");
            //  SalesMap.put("Item_Quantity", Item_Quantity.getText().toString());
            SalesMap.put("Upload_Date", strDate.trim());
            SalesMap.put("Upload_Date_To_Show", currentDate);
            SalesMap.put("Img_Uri", taskSnapshot.getDownloadUrl().toString());
            SalesMap.put("User_ID", id);
            SalesMap.put("Category", SubCategory);

            Sales_Database_Ref.child(MainCategory).child(strDate.trim()).setValue(SalesMap).addOnCompleteListener(task -> {
                SalesProgressDialog.dismiss();
                Toast.makeText(context, "Your Item is published", Toast.LENGTH_SHORT).show();

                ClearTools();

            }).addOnFailureListener(e -> {
                SalesProgressDialog.dismiss();
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });

    }
    private void ClearTools() {
        Item_Img.setImageDrawable(getResources().getDrawable(R.drawable.ic_upload_cloud));
        IsPhotoExist = false;
        Item_Title.setText("");
        Item_Description.setText("");
        Item_Price.setText("");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            ImgUri = data.getData();

            Toast.makeText(context, ImgUri.toString(), Toast.LENGTH_LONG).show();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), ImgUri);
                Item_Img.setImageBitmap(bitmap);
            } catch (IOException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
            IsPhotoExist = true;

        }
    }
    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {

            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                SelectCategory fragment = new SelectCategory();
                FragmentTransaction fragmentTransaction1 = getFragmentManager().beginTransaction();
                fragmentTransaction1.replace(R.id.Frame_Content, fragment);
                fragmentTransaction1.setCustomAnimations(R.animator.fade_in,R.animator.fade_out);
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
