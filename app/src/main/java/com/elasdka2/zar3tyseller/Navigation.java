package com.elasdka2.zar3tyseller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Navigation extends AppCompatActivity implements ComplaintBottomSheetDialog.BottomSheetListener,
                            LanguageBottomSheetDialog.BottomSheetListener{
    @BindView(R.id.bottom_nav)
    BottomNavigationView navigation;

    private FirebaseAuth mAuth;
    private DatabaseReference UserRef;
    String CurrentUserID, welcome_mail, welcome_name;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.Home :
                    SelectCategory fragment1 = new SelectCategory();
                    FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction1.replace(R.id.Frame_Content,fragment1);
                    fragmentTransaction1.commit();
                    return true;

                case R.id.Personal :
                    Bundle args = new Bundle();
                    args.putString("CurrentUserName",welcome_name);
                    args.putString("CurrentUserMail",welcome_mail);
                    Personal fragment2 = new Personal();
                    fragment2.setArguments(args);
                    FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.Frame_Content,fragment2);
                    fragmentTransaction2.commit();
                    return true;

                case R.id.Setting :
                    Setting fragment3 = new Setting();
                    FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction3.replace(R.id.Frame_Content,fragment3);
                    fragmentTransaction3.commit();
                    return true;
                case R.id.Test :
                    Message fragment4 = new Message();
                    FragmentTransaction fragmentTransaction4 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction4.replace(R.id.Frame_Content,fragment4);
                    fragmentTransaction4.commit();
                    return true;
            }

            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_act);
        ButterKnife.bind(this);

        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        SelectCategory fragment1 = new SelectCategory();
        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction1.replace(R.id.Frame_Content,fragment1,"SelectCategory");
        fragmentTransaction1.commit();

        ComplaintBottomSheetDialog bottomSheetDialog = new ComplaintBottomSheetDialog();
        bottomSheetDialog.setCancelable(false);

        LanguageBottomSheetDialog languageBottomSheetDialog = new LanguageBottomSheetDialog();
        languageBottomSheetDialog.setCancelable(false);

        mAuth = FirebaseAuth.getInstance();
        UserRef = FirebaseDatabase.getInstance().getReference("Users").child("Sellers");
        CurrentUserID = mAuth.getCurrentUser().getUid();

        UserRef.child(CurrentUserID).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot UsersSnap) {

                String user_name = UsersSnap.child("UserName").getValue(String.class);
                String user_mail = UsersSnap.child("Mail").getValue(String.class);


                welcome_mail = user_mail;
                welcome_name = user_name;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onButtonClicked(String text) {

    }
}
