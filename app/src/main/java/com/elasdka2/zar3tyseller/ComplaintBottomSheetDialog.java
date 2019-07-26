package com.elasdka2.zar3tyseller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ComplaintBottomSheetDialog extends BottomSheetDialogFragment {
    private BottomSheetListener myListener;

    private FirebaseAuth mAuth;
    private DatabaseReference UserRef,ComplaintRef;
    String CurrentUserID;
    Button send_btn,cancel_btn;
    EditText complaint;
    /*@BindView(R.id.send_complaint_btn)
    Button send;
    @BindView(R.id.cancel_complaint_btn)
    Button cancel;
    @BindView(R.id.complaint_edit_text)
    EditText complaint;
    @OnClick(R.id.cancel_complaint_btn)
    public void Cancel(){
        dismiss();
    }*/
   /* @OnClick(R.id.send_complaint_btn)
    public void SendComplaint(){
        CheckDataAndSend();
    }*/

    private void CheckDataAndSend() {
        String str_complaint = complaint.getText().toString();
        if (TextUtils.isEmpty(str_complaint)){
            Toast.makeText(getActivity(),"Type Your Complaint Please !",Toast.LENGTH_LONG).show();
        }else {
            SendComplaintToFirebase();
        }

    }

    private void SendComplaintToFirebase() {
        String currentDate= DateFormat.getDateInstance(DateFormat.FULL).format(Calendar.getInstance().getTime());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy,MM,dd,HH,mm,ss,SSS");
        Date now = new Date();
        String strDate = sdf.format(now);

        Map<String,String> ComplaintMap = new HashMap<>();
        ComplaintMap.put("Complaint",complaint.getText().toString());
        ComplaintMap.put("Date",currentDate);
        ComplaintRef.child("Sellers").child(CurrentUserID).child(strDate.trim()).setValue(ComplaintMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getActivity(),"Complaint Sent Thank you .",Toast.LENGTH_SHORT).show();
                    dismiss();
                }else Toast.makeText(getActivity(),"Failed sending complaint try again in 1 minute please !",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.complaint_bottom_sheet, container, false);
      //  ButterKnife.bind(this,v);

        mAuth = FirebaseAuth.getInstance();
        UserRef = FirebaseDatabase.getInstance().getReference("Users").child("Sellers");
        ComplaintRef = FirebaseDatabase.getInstance().getReference("Complaints");
        CurrentUserID = mAuth.getCurrentUser().getUid();

         send_btn = v.findViewById(R.id.send_complaint_btn);
         cancel_btn = v.findViewById(R.id.cancel_complaint_btn);
         complaint = v.findViewById(R.id.complaint_edit_text);

        send_btn.setOnClickListener(v1 -> {
          //  Toast.makeText(getActivity(),"Send Clicked",Toast.LENGTH_SHORT).show();
            myListener.onButtonClicked("Button 1 clicked");

            CheckDataAndSend();

        });
        cancel_btn.setOnClickListener(v12 -> {
            //Toast.makeText(getActivity(),"Cancel Clicked",Toast.LENGTH_SHORT).show();
            myListener.onButtonClicked("Button 2 clicked");
            dismiss();
        });

        return v;
    }

    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            myListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }
}
