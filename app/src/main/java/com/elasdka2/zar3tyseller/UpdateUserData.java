package com.elasdka2.zar3tyseller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
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
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UpdateUserData.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UpdateUserData#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateUserData extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //-------------------------------------
    Context context;
    private static final int GALLERY_INTENT = 1;
    private Uri ImgUri;
    boolean gabalsora;
    ProgressDialog UpdateProgressDialog;
    FirebaseAuth mAuth;
    DatabaseReference UsersRef;
    StorageReference mStorage;
    boolean IsPhotoExist;
    String CurrentUserID;

    @BindView(R.id.seller_update_profile_img)
    ImageView img_update;
    @BindView(R.id.seller_update_user_name)
    EditText user_name_update;
    @BindView(R.id.seller_update_user_phone)
    EditText phone_update;
    @BindView(R.id.seller_update_user_country)
    EditText country_update;
    @BindView(R.id.seller_update_user_data_btn)
    Button update_btn;
    //-------------------------------------
    @OnClick(R.id.seller_update_profile_img)
    public void UpdatePic(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select"), GALLERY_INTENT);
    }

    @OnClick(R.id.seller_update_user_data_btn)
    public void Update(){
        CheckDataAndUpdate();
    }

    private void CheckDataAndUpdate() {
        String str_user_name = user_name_update.getText().toString().trim();
        String str_country = country_update.getText().toString().trim();
        String str_phone = phone_update.getText().toString().trim();

        if (TextUtils.isEmpty(str_user_name)){
            user_name_update.setError("Insert Your Name Please");
            user_name_update.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(str_phone)){
            phone_update.setError("Insert Your Phone Number Please");
            phone_update.requestFocus();
            return;
        }
        if (!str_phone.startsWith("01")){
            phone_update.setError("Mobile Phone Must Start With 01 ");
            phone_update.requestFocus();
            return;
        }
        if (str_phone.length() < 11 ){
            phone_update.setError("Invalid Mobile Number");
            phone_update.requestFocus();
            return;
        }
        if (str_phone.length() > 11 ){
            phone_update.setError("Invalid Mobile Number");
            phone_update.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(str_country)){
            country_update.setError("Insert Your Country Please");
            country_update.requestFocus();
            return;
        }
        UpdateData();
    }
    private void UpdateData() {
        UpdateProgressDialog.show();
        if (IsPhotoExist){
            mStorage.child(CurrentUserID).delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    mStorage.child(CurrentUserID).putFile(ImgUri).addOnSuccessListener(taskSnapshot -> {
                        UsersRef.child(CurrentUserID).child("UserName").setValue(user_name_update.getText().toString());
                        UsersRef.child(CurrentUserID).child("Phone").setValue(phone_update.getText().toString());
                        UsersRef.child(CurrentUserID).child("Country").setValue(country_update.getText().toString());
                        UsersRef.child(CurrentUserID).child("ImgUri").setValue(taskSnapshot.getDownloadUrl().toString())
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
            UsersRef.child(CurrentUserID).child("UserName").setValue(user_name_update.getText().toString());
            UsersRef.child(CurrentUserID).child("Phone").setValue(phone_update.getText().toString());
            UsersRef.child(CurrentUserID).child("Country").setValue(country_update.getText().toString())
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
    //-------------------------------------


    public UpdateUserData() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateData.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateUserData newInstance(String param1, String param2) {
        UpdateUserData fragment = new UpdateUserData();
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
        View v = inflater.inflate(R.layout.update_user_data_frag, container, false);
        context = getActivity();
        ButterKnife.bind(this, v);
        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference("ProfileImages").child("Sellers");
        UsersRef = FirebaseDatabase.getInstance().getReference("Users").child("Sellers");
        CurrentUserID = mAuth.getCurrentUser().getUid();
        IsPhotoExist = false;

        UpdateProgressDialog = new ProgressDialog(context);
        UpdateProgressDialog.setCanceledOnTouchOutside(false);
        UpdateProgressDialog.setCancelable(false);
        UpdateProgressDialog.setMessage("Updating Your Data ...");

        user_name_update.setText(getArguments().getString("UserName"));
        phone_update.setText(getArguments().getString("UserPhone"));
        country_update.setText(getArguments().getString("UserCountry"));
        Glide.with(context.getApplicationContext()).load(getArguments().getString("UserImg")).into(img_update);

        SharedPreferences.Editor editor = context.getSharedPreferences("CurrentUser", MODE_PRIVATE).edit();
        editor.putString("UserName", user_name_update.getText().toString());
        editor.putString("UserMail", getArguments().getString("UserMail"));
        editor.apply();

        return v;
    }

    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {

            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                Bundle args = new Bundle();
                args.putString("UserName",user_name_update.getText().toString());
                args.putString("UserName",getArguments().getString("UserMail"));
                args.putString("UniqueID","from_UpdateUserData");

                Personal fragment = new Personal();
                fragment.setArguments(args);
                FragmentTransaction fragmentTransaction1 = getFragmentManager().beginTransaction();
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
                img_update.setImageBitmap(bitmap);
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
