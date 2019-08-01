package com.elasdka2.zar3tyseller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Splash extends AppCompatActivity {
    private static int splash_time_out = 2000;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference SellerReference;

    @BindView(R.id.splash_app_txt)
    TextView app_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);


        Animation myanim = AnimationUtils.loadAnimation(Splash.this, R.anim.stb2);
        app_txt.setAnimation(myanim);

        firebaseAuth = FirebaseAuth.getInstance();
        SellerReference = FirebaseDatabase.getInstance().getReference("Users").child("Sellers");
        firebaseUser = firebaseAuth.getCurrentUser();

        new Handler().postDelayed(() -> {
            if (firebaseUser == null) {
                Intent intent = new Intent(Splash.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();

            } else {

                String CurrentUserID = firebaseUser.getUid();
                Query query = SellerReference.orderByChild("User_ID").equalTo(CurrentUserID);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                String user_name = ds.child("UserName").getValue(String.class);
                                String user_mail = ds.child("Mail").getValue(String.class);

                                SharedPreferences.Editor editor = getSharedPreferences("CurrentUser", MODE_PRIVATE).edit();
                                editor.putString("UserName", user_name);
                                editor.putString("UserMail", user_mail);
                                editor.apply();

                                Intent intent = new Intent(Splash.this, Navigation.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK & Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                startActivity(intent);

                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                finish();

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }, splash_time_out);
    }
}
