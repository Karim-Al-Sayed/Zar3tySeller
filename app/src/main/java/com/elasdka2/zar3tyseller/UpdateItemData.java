package com.elasdka2.zar3tyseller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UpdateItemData.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UpdateItemData#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateItemData extends Fragment implements AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    //-----------------------------------------------
    Context context;
    private static final String[] agriculture_paths = {"نخل", "شجر", "شجيرات", "مغطيات ترب", "مغذيات و وقاية"};
    private static final String[] irrigation_paths = {"لوح كنترول", "مواسير", "خراطيم", "رشاشات", "محابس", "وصلات", "مضخات"};
    private static final String[] industrial_agriculture_paths = {"نخل", "شجر", "شجيرات", "مغطيات ترب"};
    private static final int GALLERY_INTENT = 1;
    private Uri ImgUri;
    boolean gabalsora;
    ProgressDialog UpdateProgressDialog;
    FirebaseAuth mAuth;
    DatabaseReference SalesRef;
    StorageReference mStorage;
    boolean IsPhotoExist;
    String CurrentUserID, MainCategory,SubCategory,UploadDate;
    //-----------------------------------------------
    @BindView(R.id.item_update_item_img)
    ImageView item_img;
    @BindView(R.id.item_update_item_title)
    EditText item_title;
    @BindView(R.id.item_update_item_description)
    EditText item_description;
    @BindView(R.id.item_update_item_price)
    EditText item_price;
    @BindView(R.id.item_update_agriculture_category_spinner)
    Spinner agriculture_category;
    @BindView(R.id.item_update_irrigation_category_spinner)
    Spinner irrigation_category;
    @BindView(R.id.item_update_industrial_agriculture_category_spinner)
    Spinner industrial_agriculture_category;
    @BindView(R.id.item_update_item_data_btn)
    Button update_btn;
    //-----------------------------------------------
    @OnClick(R.id.item_update_item_img)
    public void UpdatePic(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select"), GALLERY_INTENT);
    }
    @OnClick(R.id.item_update_item_data_btn)
    public void Update(){
        CheckDataAndUpdate();
    }
    //-----------------------------------------------
    public UpdateItemData() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateItemData.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateItemData newInstance(String param1, String param2) {
        UpdateItemData fragment = new UpdateItemData();
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
        View v = inflater.inflate(R.layout.update_item_data_frag, container, false);
        context = getActivity();
        ButterKnife.bind(this, v);

        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference("Sales").child("Items_Images");
        SalesRef = FirebaseDatabase.getInstance().getReference("Sales");
        CurrentUserID = mAuth.getCurrentUser().getUid();
        IsPhotoExist = false;

        UpdateProgressDialog = new ProgressDialog(context);
        UpdateProgressDialog.setCanceledOnTouchOutside(false);
        UpdateProgressDialog.setCancelable(false);
        UpdateProgressDialog.setMessage("Updating Your Data ...");

        if (getArguments() != null) {

            item_title.setText(getArguments().getString("ItemTitle"));
            item_description.setText(getArguments().getString("ItemDescription"));
            item_price.setText(getArguments().getString("ItemPrice"));
            Glide.with(context).load(getArguments().getString("ItemImg")).into(item_img);
            UploadDate = getArguments().getString("ItemUploadDate");

            MainCategory = getArguments().getString("ItemMainCategory");
            if (MainCategory.equals("Agriculture")){
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                        android.R.layout.simple_spinner_item, agriculture_paths);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                agriculture_category.setAdapter(adapter);
                agriculture_category.setOnItemSelectedListener(UpdateItemData.this);
                MainCategory = "Agriculture";
                irrigation_category.setVisibility(View.GONE);
                industrial_agriculture_category.setVisibility(View.GONE);
                agriculture_category.setVisibility(View.VISIBLE);

            }else if (MainCategory.equals("Irrigation")){
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                        android.R.layout.simple_spinner_item, irrigation_paths);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                irrigation_category.setAdapter(adapter);
                irrigation_category.setOnItemSelectedListener(UpdateItemData.this);
                MainCategory = "Irrigation";
                agriculture_category.setVisibility(View.GONE);
                industrial_agriculture_category.setVisibility(View.GONE);
                irrigation_category.setVisibility(View.VISIBLE);

            }else if (MainCategory.equals("Industrial_Agriculture")){
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                        android.R.layout.simple_spinner_item, industrial_agriculture_paths);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                industrial_agriculture_category.setAdapter(adapter);
                industrial_agriculture_category.setOnItemSelectedListener(UpdateItemData.this);
                MainCategory = "Industrial_Agriculture";
                irrigation_category.setVisibility(View.GONE);
                agriculture_category.setVisibility(View.GONE);
                industrial_agriculture_category.setVisibility(View.VISIBLE);

            }
        }

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    private void CheckDataAndUpdate() {
        String str_item_title = item_title.getText().toString().trim();
        String str_item_description = item_description.getText().toString().trim();
        String str_item_price = item_price.getText().toString().trim();

        if (TextUtils.isEmpty(str_item_title)){
            item_title.setError("Insert Item Title Please");
            item_title.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(str_item_description)){
            item_description.setError("Insert Item Description Please");
            item_description.requestFocus();
            return;
        }

        if (str_item_price.equals("0") ){
            item_price.setError("Invalid Price");
            item_price.requestFocus();
            return;
        }
        if (str_item_price.startsWith("0") ){
            item_price.setError("Invalid Price");
            item_price.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(str_item_price)){
            item_price.setError("Insert Item Price Please");
            item_price.requestFocus();
            return;
        }
        UpdateData();
    }

    private void UpdateData() {
        UpdateProgressDialog.show();
        if (IsPhotoExist){
            mStorage.child(CurrentUserID).child(getArguments().getString("ItemDescription")).delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    mStorage.child(CurrentUserID).putFile(ImgUri).addOnSuccessListener(taskSnapshot -> {
                        SalesRef.child(CurrentUserID).child(MainCategory).child(UploadDate).child("Item_Title").setValue(item_title.getText().toString());
                        SalesRef.child(CurrentUserID).child(MainCategory).child(UploadDate).child("Item_Description").setValue(item_description.getText().toString());
                        SalesRef.child(CurrentUserID).child(MainCategory).child(UploadDate).child("Item_Price").setValue(item_price.getText().toString() + " EGP");
                        SalesRef.child(CurrentUserID).child(MainCategory).child(UploadDate).child("Img_Uri").setValue(taskSnapshot.getDownloadUrl().toString());
                        SalesRef.child(CurrentUserID).child(MainCategory).child(UploadDate).child("Category").setValue(SubCategory)
                                .addOnCompleteListener(task1 -> {
                                    UpdateProgressDialog.dismiss();
                                    Toast.makeText(context.getApplicationContext(),"Updated",Toast.LENGTH_LONG).show();
                                    //Toast.makeText(context.getApplicationContext(),"Updated Successfully",Toast.LENGTH_LONG).show();
                                }).addOnFailureListener(e -> {
                            UpdateProgressDialog.dismiss();
                            Toast.makeText(context.getApplicationContext(),"Update Failed Try again in 1 minute",Toast.LENGTH_LONG).show();
                            //Toast.makeText(context.getApplicationContext(),"Update Failed Try again in 1 minute",Toast.LENGTH_LONG).show();
                        });

                    }).addOnFailureListener(e -> {
                        Toast.makeText(context.getApplicationContext(),"Failed Upload New Img",Toast.LENGTH_LONG).show();
                        //Toast.makeText(context,"Failed Upload New Img " + e.getMessage(),Toast.LENGTH_LONG).show();
                    });
                }else if (!task.isSuccessful()){
                    Toast.makeText(context.getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    //Toast.makeText(context,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            });

        }else {
            SalesRef.child(CurrentUserID).child(MainCategory).child(UploadDate).child("Item_Title").setValue(item_title.getText().toString());
            SalesRef.child(CurrentUserID).child(MainCategory).child(UploadDate).child("Item_Description").setValue(item_description.getText().toString());
            SalesRef.child(CurrentUserID).child(MainCategory).child(UploadDate).child("Item_Price").setValue(item_price.getText().toString() + " EGP");
            SalesRef.child(CurrentUserID).child(MainCategory).child(UploadDate).child("Category").setValue(SubCategory)
                    .addOnCompleteListener(task -> {
                        UpdateProgressDialog.dismiss();
                        Toast.makeText(context.getApplicationContext(),"Updated",Toast.LENGTH_LONG).show();
                        //Toast.makeText(context.getApplicationContext(),"Updated Successfully",Toast.LENGTH_LONG).show();
                    }).addOnFailureListener(e -> {
                UpdateProgressDialog.dismiss();
                Toast.makeText(context.getApplicationContext(),"Update Failed Try again in 1 minute",Toast.LENGTH_LONG).show();
                //Toast.makeText(context.getApplicationContext(),"Update Failed Try again in 1 minute",Toast.LENGTH_LONG).show();
            });
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.item_update_agriculture_category_spinner) {
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
        }else if (parent.getId() == R.id.item_update_irrigation_category_spinner){
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
        }else if (parent.getId() == R.id.item_update_industrial_agriculture_category_spinner){
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
        if (parent.getId() == R.id.item_update_agriculture_category_spinner){
            SubCategory = "نخل";
        }else if (parent.getId() == R.id.item_update_irrigation_category_spinner){
            SubCategory = "لوح كنترول";
        }else if (parent.getId() == R.id.item_update_industrial_agriculture_category_spinner)
            SubCategory = "نخل";
    }
    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {

            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                ItemInfo fragment = new ItemInfo();
                Bundle args = new Bundle();
                args.putString("UniqueID","from_UpdateItemData");
                args.putString("ItemTitle",item_title.getText().toString());
                args.putString("ItemDescription",item_description.getText().toString());
                args.putString("ItemPrice",item_price.getText().toString() + " EGP");
                args.putString("ItemDate",getArguments().getString("ItemDateToShow"));
                args.putString("ItemCategory",SubCategory);
                args.putString("ItemMainCategory",MainCategory);
                args.putString("UploadDate",UploadDate);
                if (IsPhotoExist){
                    args.putString("ItemImg",ImgUri.toString());
                }else {
                    args.putString("ItemImg",getArguments().getString("ItemImg"));
                }
                fragment.setArguments(args);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction1 = fragmentManager.beginTransaction();
                fragmentTransaction1.replace(R.id.Frame_Content, fragment);
                fragmentTransaction1.commit();

                return true;

            }

            return false;
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            ImgUri = data.getData();

            Toast.makeText(context, ImgUri.toString(), Toast.LENGTH_LONG).show();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), ImgUri);
                item_img.setImageBitmap(bitmap);
            } catch (IOException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
            IsPhotoExist = true;
        }
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
