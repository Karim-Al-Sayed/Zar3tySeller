package com.elasdka2.zar3tyseller;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class DiscountBottomSheetDialog extends BottomSheetDialogFragment {
    private BottomSheetListener myListener;

    private FirebaseAuth mAuth;
    private DatabaseReference OrdersRef;
    String CurrentUserID;
    Button done_btn, skip_btn;
    EditText discount;
    CheckBox add_discount;

    private void CheckDataAndSend() {
        if (add_discount.isChecked()) {
            discount.setEnabled(true);
            String str_complaint = discount.getText().toString();
            if (TextUtils.isEmpty(str_complaint)) {
                Toast.makeText(getActivity(), "Enter new price !", Toast.LENGTH_LONG).show();
            } else {
                SendDiscountOrderToFirebase();
            }
        }
    }

    private void SendDiscountOrderToFirebase() {
        if (getArguments() != null) {
            Map<String, String> OrdersMap = new HashMap<>();
            OrdersMap.put("CustomerID", Objects.requireNonNull(getArguments().getString("CustomerID")));
            OrdersMap.put("ItemImg", Objects.requireNonNull(getArguments().getString("ItemImg")));
            OrdersMap.put("ItemTitle", Objects.requireNonNull(getArguments().getString("ItemTitle")));
            OrdersMap.put("ItemPrice", Objects.requireNonNull(getArguments().getString("ItemPrice")));
            OrdersMap.put("ItemQuantity", Objects.requireNonNull(getArguments().getString("ItemQuantity")));
            OrdersMap.put("RequestDate", Objects.requireNonNull(getArguments().getString("RequestDate")));
            OrdersMap.put("SellerID", CurrentUserID);
            OrdersMap.put("State", "Accepted");

            OrdersRef.push().setValue(OrdersMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Done", Toast.LENGTH_LONG).show();
                    dismiss();
                } else
                    Toast.makeText(getActivity(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
            });
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.discount_bottom_sheet, container, false);
        //  ButterKnife.bind(this,v);

        mAuth = FirebaseAuth.getInstance();
        OrdersRef = FirebaseDatabase.getInstance().getReference("Orders");
        CurrentUserID = mAuth.getCurrentUser().getUid();

        discount = v.findViewById(R.id.done_discount_btn);
        skip_btn = v.findViewById(R.id.skip_discount_btn);
        discount = v.findViewById(R.id.discount_edit_text);
        add_discount = v.findViewById(R.id.discount_check_box);

        add_discount.setChecked(false);
        discount.setEnabled(false);

        discount.setOnClickListener(v1 -> {
            //  Toast.makeText(getActivity(),"Send Clicked",Toast.LENGTH_SHORT).show();
            CheckDataAndSend();

        });
        skip_btn.setOnClickListener(v12 -> {
            if (getArguments() != null) {
                Map<String, String> OrdersMap = new HashMap<>();
                OrdersMap.put("CustomerID", Objects.requireNonNull(getArguments().getString("CustomerID")));
                OrdersMap.put("ItemImg", Objects.requireNonNull(getArguments().getString("ItemImg")));
                OrdersMap.put("ItemTitle", Objects.requireNonNull(getArguments().getString("ItemTitle")));
                OrdersMap.put("ItemPrice", Objects.requireNonNull(getArguments().getString("ItemPrice")));
                OrdersMap.put("ItemQuantity", Objects.requireNonNull(getArguments().getString("ItemQuantity")));
                OrdersMap.put("RequestDate", Objects.requireNonNull(getArguments().getString("RequestDate")));
                OrdersMap.put("SellerID", CurrentUserID);
                OrdersMap.put("State", "Accepted");

                OrdersRef.push().setValue(OrdersMap).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Done", Toast.LENGTH_LONG).show();
                        dismiss();
                    } else
                        Toast.makeText(getActivity(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                });
            }
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
