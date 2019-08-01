package com.elasdka2.zar3tyseller.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.core.app.NotificationCompat;

import com.elasdka2.zar3tyseller.Model.Tokens;
import com.elasdka2.zar3tyseller.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessaging extends FirebaseMessagingService {
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    String SellerName;
    NotificationManager notificationManager;
    public int NotificationID;
    DatabaseReference reference;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        reference.child("Sellers").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        SellerName = ds.child("UserName").getValue(String.class);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String CustomerID = remoteMessage.getData().get("CustomerId");

        SharedPreferences preferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
        String currentUser = preferences.getString("SellerID", "none");
        if (!currentUser.equals(CustomerID)) {
            SendNotification(remoteMessage);
        }
    }
    public void SendNotification(RemoteMessage remoteMessage){
        if(remoteMessage.getData().size() > 0 ) {
            String title = remoteMessage.getData().get("title");
            String message = remoteMessage.getData().get("body");
            String CustomerID = remoteMessage.getData().get("CustomerId");
            String SellerID = remoteMessage.getData().get("SellerId");
            String click_Action = remoteMessage.getData().get("click_action");

            Intent intent = new Intent(click_Action);
            intent.putExtra("message", message);
            intent.putExtra("from_user_id", CustomerID);
            intent.putExtra("to_user_id", SellerID);
            intent.putExtra("UniqueID", "from_Messaging");

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                String CHANNEL_ID = "my_channel_01";
                CharSequence name = "my_channel";
                String Description = "This is my channel";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                mChannel.setDescription(Description);
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.RED);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mChannel.setShowBadge(false);
                notificationManager.createNotificationChannel(mChannel);
            }else {
                if (!TextUtils.isEmpty(SellerName)){
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "01")
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setVibrate(new long[]{1000, 1000})
                            .setContentTitle(title)
                            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                            .setContentText(message)
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setWhen(0);

                    NotificationID = (int) System.currentTimeMillis();
                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    manager.notify(NotificationID, mBuilder.build());
                }else {
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "01")
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setVibrate(new long[]{1000, 1000})
                            .setContentTitle(title)
                            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                            .setContentText(message)
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setWhen(0);

                    NotificationID = (int) System.currentTimeMillis();
                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    manager.notify(NotificationID, mBuilder.build());
                }
            }

        }
    }


    private void updateToken(String refreshToken) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SellerTokens");
        Tokens token = new Tokens(refreshToken);
        reference.child(firebaseUser.getUid()).setValue(token);

    }
}
