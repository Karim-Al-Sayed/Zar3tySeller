package com.elasdka2.zar3tyseller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class SignUp extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Animation animation;
    private static final String[] paths = {"Cairo", "Giza", "Alex"};

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference UsersRef;
    private StorageReference StorageRef;
    private static final int GALLERY_INTENT = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private Uri imgUri;
    boolean gabalsora;
    String imgURL;
    Map<String, String> UsersMap = new HashMap<>();
    ProgressDialog SignUpProgressDialog;

    @BindView(R.id.signup_profile_img)
    CircleImageView profile_img;
    @BindView(R.id.signup_username_edt)
    EditText user_name;
    @BindView(R.id.signup_email_edt)
    EditText mail;
    @BindView(R.id.signup_password_edt)
    EditText password;
    @BindView(R.id.signup_confirm_password_edt)
    EditText confirm_password;
    @BindView(R.id.signup_phone_edt)
    EditText phone;
    @BindView(R.id.signup_national_id_edt)
    EditText national_id;
    @BindView(R.id.signup_check_box)
    CheckBox agree_check;
    @BindView(R.id.signup_sign_btn)
    Button signup_btn;
    @BindView(R.id.signup_login_txt)
    TextView signin_txt;
    @BindView(R.id.signup_male_radio)
    RadioButton male_radio;
    @BindView(R.id.signup_female_radio)
    RadioButton female_radio;
    @BindView(R.id.signup_country_spinner)
    Spinner country_spinner;

    @OnClick(R.id.signup_profile_img)
    public void GetUserImg() {
        uploadProfile();
    }

    @OnClick(R.id.signup_login_txt)
    public void GoToLogin() {
        startActivity(new Intent(SignUp.this, Login.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    @OnClick(R.id.signup_sign_btn)
    public void SignUp() {
        CheckDataAndCreateNewUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_act);
        ButterKnife.bind(this);


        gabalsora = false;

        user_name.clearFocus();

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        UsersRef = database.getReference("Users");
        StorageRef = FirebaseStorage.getInstance().getReference();

        SignUpProgressDialog = new ProgressDialog(this);
        SignUpProgressDialog.setCanceledOnTouchOutside(false);
        SignUpProgressDialog.setCancelable(false);
        SignUpProgressDialog.setMessage("Creating Your Account ...");


        //------------------Animation--------------------
        animation = AnimationUtils.loadAnimation(this, R.anim.stb);

        user_name.setAlpha(0);
        mail.setAlpha(0);
        password.setAlpha(0);
        confirm_password.setAlpha(0);
        phone.setAlpha(0);
        national_id.setAlpha(0);
        country_spinner.setAlpha(0);
        agree_check.setAlpha(0);
        male_radio.setAlpha(0);
        female_radio.setAlpha(0);
        signup_btn.setAlpha(0);
        signin_txt.setAlpha(0);

        male_radio.setTranslationY(400);
        female_radio.setTranslationY(400);
        agree_check.setTranslationY(600);
        signup_btn.setTranslationY(800);
        signin_txt.setTranslationY(1000);

        user_name.setTranslationX(800);
        mail.setTranslationX(800);
        password.setTranslationX(800);
        confirm_password.setTranslationX(800);
        phone.setTranslationX(800);
        national_id.setTranslationX(800);
        country_spinner.setTranslationX(800);

        male_radio.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        female_radio.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        agree_check.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        signup_btn.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1000).start();
        signin_txt.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1000).start();

        user_name.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(200).start();
        mail.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        password.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        confirm_password.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        phone.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        national_id.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        country_spinner.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(800).start();

        profile_img.startAnimation(animation);

        //-----------------------------------------------
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignUp.this,
                android.R.layout.simple_spinner_item, paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        country_spinner.setAdapter(adapter);
        country_spinner.setOnItemSelectedListener(this);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                UsersMap.put("Country", "Cairo");
                // Whatever you want to happen when the first item gets selected
                break;
            case 1:
                UsersMap.put("Country", "Giza");
                // Whatever you want to happen when the second item gets selected
                break;
            case 2:
                UsersMap.put("Country", "Alex");
                // Whatever you want to happen when the thrid item gets selected
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void uploadProfile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select"), GALLERY_INTENT);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            imgUri = data.getData();

            Toast.makeText(this, imgUri.toString(), Toast.LENGTH_LONG).show();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                profile_img.setImageBitmap(bitmap);
            } catch (IOException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
            gabalsora = true;

        }
    }
    private void ClearTools() {
        profile_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_vector));
        gabalsora = false;
        mail.setText("");
        password.setText("");
        password.setText("");
        user_name.setText("");
        confirm_password.setText("");
        national_id.setText("");
        male_radio.setChecked(false);
        female_radio.setChecked(false);
        agree_check.setChecked(false);
        country_spinner.setSelected(false);
    }
    private void CheckDataAndCreateNewUser() {
        String str_last_name = user_name.getText().toString().trim();
        String str_mail = mail.getText().toString().trim();
        String str_pass = password.getText().toString().trim();
        String str_confirm_pass = confirm_password.getText().toString().trim();
        String str_phone = phone.getText().toString().trim();
        String str_nationalID = national_id.getText().toString().trim();
        if (!gabalsora){
            Toast.makeText(getApplicationContext(),"Select You Profile Pic Please",Toast.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(),"Select You Profile Pic Please",Toast.LENGTH_LONG).show();
            return;
        }

        if (str_last_name.isEmpty()){
            user_name.setError("Insert Your Name Please");
            user_name.requestFocus();
            return;
        }
        if (str_mail.isEmpty()){
            mail.setError("Insert Your Mail Please");
            mail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(str_mail).matches()){
            mail.setError("Invalid Email !");
            mail.requestFocus();
            return;
        }

        if (str_pass.isEmpty()){
            password.setError("Insert Your Password Please");
            password.requestFocus();
            return;
        }
        if (str_pass.length() < 6){
            password.setError("Required 6 char at least");
            password.requestFocus();
            return;
        }
        if (!(str_confirm_pass.equals(str_pass))){
            confirm_password.setError("Invalid Password");
            confirm_password.requestFocus();
            return;
        }
        if (str_phone.isEmpty()){
            phone.setError("Insert Your Mobile Phone Please");
            phone.requestFocus();
            return;
        }
        if (!str_phone.startsWith("01")){
            phone.setError("Mobile Phone Must Start With 01 ");
            phone.requestFocus();
            return;
        }
        if (str_phone.length() < 11 ){
            phone.setError("Required 11 Num at least");
            phone.requestFocus();
            return;
        }
        if (str_phone.length() > 11 ){
            phone.setError("Invalid Mobile Number");
            phone.requestFocus();
            return;
        }
        if(str_nationalID.isEmpty()){
            national_id.setError("Please Insert Your National Number");
            national_id.requestFocus();
            return;
        }
        if (str_nationalID.length() < 14){
            national_id.setError("Required 14 Num at least");
            national_id.requestFocus();
            return;
        }
        if (str_nationalID.length() > 14){
            national_id.setError("Invalid National Number");
            national_id.requestFocus();
            return;
        }
        if (!male_radio.isChecked() && !female_radio.isChecked()){
            Toast.makeText(getApplicationContext(),"Select Your Identifier Please",Toast.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(),"Select Your Identifier Please",Toast.LENGTH_LONG).show();
            return;
        }
        if (male_radio.isChecked()){
            UsersMap.put("Gender","Male");
        }
        if (female_radio.isChecked()){
            UsersMap.put("Gender","Female");
        }
        if (!agree_check.isChecked()){
            Toast.makeText(getApplicationContext(),"You Should Agree Terms and Conditions",Toast.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(),"You Should Agree Terms and Conditions",Toast.LENGTH_LONG).show();
            return;
        }
        CreateNewUser(mail,password);

    }
    private void CreateNewUser(EditText mail, EditText password) {
        SignUpProgressDialog.show();

        mAuth.createUserWithEmailAndPassword(mail.getText().toString(), password.getText().toString())
                .addOnCompleteListener(SignUp.this, task -> {

                    if (!task.isSuccessful()) {
                        if(task.getException() instanceof FirebaseAuthUserCollisionException){
                            Toast.makeText(getApplicationContext(),"This Email already exists",Toast.LENGTH_SHORT).show();
                            SignUpProgressDialog.dismiss();
                            return;
                        }
                    } else {

                        FirebaseUser user = mAuth.getCurrentUser();
                        String token_id =  FirebaseInstanceId.getInstance().getToken();

                        //*************************************************************

                            StorageRef.child("ProfileImages/Sellers/" + user.getUid())
                                    .putFile(imgUri).addOnSuccessListener(taskSnapshot -> {
                                Toast.makeText(SignUp.this,"Profile Img Uploaded Successfully",Toast.LENGTH_LONG).show();
                                //Toast.makeText(SignUp.this, "Profile Img Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                UsersMap.put("UserName", user_name.getText().toString());
                                UsersMap.put("Mail", mail.getText().toString());
                                UsersMap.put("Phone", phone.getText().toString());
                                UsersMap.put("National_ID", national_id.getText().toString());
                                UsersMap.put("User_ID", user.getUid());
                                UsersMap.put("token_id", token_id);
                                UsersMap.put("User_State", "Enabled");
                                UsersMap.put("ImgUri", taskSnapshot.getDownloadUrl().toString());
                                UsersRef.child("Sellers").child(user.getUid()).setValue(UsersMap).addOnCompleteListener(task1 -> {

                                    Toast.makeText(SignUp.this,"Account Created",Toast.LENGTH_LONG).show();
                                    //Toast.makeText(SignUp.this, "Account Created", Toast.LENGTH_SHORT).show();
                                    SignUpProgressDialog.dismiss();
                                    Intent intent = new Intent(SignUp.this, Login.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    finish();

                                    ClearTools();


                                }).addOnFailureListener(e -> {
                                    SignUpProgressDialog.dismiss();

                                    Toast.makeText(SignUp.this,"exception is " + e.toString(),Toast.LENGTH_LONG).show();
                                    //Toast.makeText(SignUp.this, "exception is " + e.toString(), Toast.LENGTH_SHORT).show();
                                });
                            });
                        }

                });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }
}
