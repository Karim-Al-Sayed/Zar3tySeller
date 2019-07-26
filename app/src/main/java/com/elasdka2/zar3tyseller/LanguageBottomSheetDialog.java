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

public class LanguageBottomSheetDialog extends BottomSheetDialogFragment {
    private BottomSheetListener myListener;

    private FirebaseAuth mAuth;
    private DatabaseReference UserRef,ComplaintRef;
    String CurrentUserID;
    Button arabic_btn,english_btn;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.language_bottom_sheet, container, false);
      //  ButterKnife.bind(this,v);

        mAuth = FirebaseAuth.getInstance();
        UserRef = FirebaseDatabase.getInstance().getReference("Users").child("Sellers");
        ComplaintRef = FirebaseDatabase.getInstance().getReference("Complaints");
        CurrentUserID = mAuth.getCurrentUser().getUid();

         arabic_btn = v.findViewById(R.id.arabic_language_btn);
         english_btn = v.findViewById(R.id.english_language_btn);

        arabic_btn.setOnClickListener(v1 -> {
            Toast.makeText(getActivity(),"Arabic Clicked",Toast.LENGTH_SHORT).show();
            myListener.onButtonClicked("Arabic Clicked");
            dismiss();

        });
        english_btn.setOnClickListener(v12 -> {
            Toast.makeText(getActivity(),"English Clicked",Toast.LENGTH_SHORT).show();
            myListener.onButtonClicked("English Clicked");
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
