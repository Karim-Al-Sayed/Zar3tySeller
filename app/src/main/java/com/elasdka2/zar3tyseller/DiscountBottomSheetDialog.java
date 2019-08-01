package com.elasdka2.zar3tyseller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class DiscountBottomSheetDialog extends BottomSheetDialogFragment {
    private BottomSheetListener myListener;

    private FirebaseAuth mAuth;
    private DatabaseReference OrdersRef,PendingRef;
    String CurrentUserID;
    Button done_btn, skip_btn;
    EditText discount;
    TextView new_price_tag,new_price_val;
    CheckBox add_discount;
    TextWatcher txtListener;
    Integer quantity,price,TotalPrice;
    Map<String, String> OrdersMap;
    private void CheckDataAndSendAccept() {
        if (add_discount.isChecked()) {
            String str_complaint = discount.getText().toString();
            if (TextUtils.isEmpty(str_complaint)) {
                Toast.makeText(getActivity(), "Enter Discount value !", Toast.LENGTH_LONG).show();
                return;
            }
            if (discount.getText().toString().startsWith("0")){
                Toast.makeText(getActivity(), "Can't Start with 0 ( zero )", Toast.LENGTH_LONG).show();
                return;
            }
                SendOrderToFirebase();

        }else Toast.makeText(getActivity(),"Add Discount or Click On Skip Button",Toast.LENGTH_LONG).show();
    }
    private void SendOrderToFirebase() {
        if (getArguments() != null) {

            OrdersMap = new HashMap<>();
            OrdersMap.put("CustomerID", Objects.requireNonNull(getArguments().getString("CustomerID")));
            OrdersMap.put("ItemImg", Objects.requireNonNull(getArguments().getString("ItemImg")));
            OrdersMap.put("ItemTitle", Objects.requireNonNull(getArguments().getString("ItemTitle")));
            OrdersMap.put("ItemPrice",new_price_val.getText().toString());
            OrdersMap.put("ItemQuantity", Objects.requireNonNull(getArguments().getString("ItemQuantity")));
            OrdersMap.put("RequestDate", Objects.requireNonNull(getArguments().getString("RequestDate")));
            OrdersMap.put("SellerID", CurrentUserID);
            OrdersMap.put("State", "Accepted");

            OrdersRef.push().setValue(OrdersMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {


                    Toast.makeText(getActivity(), "Done", Toast.LENGTH_LONG).show();
                    dismiss();
                } else
                    Toast.makeText(getActivity(),
                            Objects.requireNonNull(task.getException()).getMessage(),
                            Toast.LENGTH_LONG).show();
            });
        }

    }
    private void SendOrderToFirebaseSkip() {
        if (getArguments() != null) {

            Map<String,String> OrdersMap = new HashMap<>();
            OrdersMap.put("CustomerID", Objects.requireNonNull(getArguments().getString("CustomerID")));
            OrdersMap.put("ItemImg", Objects.requireNonNull(getArguments().getString("ItemImg")));
            OrdersMap.put("ItemTitle", Objects.requireNonNull(getArguments().getString("ItemTitle")));
            OrdersMap.put("ItemPrice", getArguments().getString("ItemPrice") + " EGP");
            OrdersMap.put("ItemQuantity", Objects.requireNonNull(getArguments().getString("ItemQuantity")));
            OrdersMap.put("RequestDate", Objects.requireNonNull(getArguments().getString("RequestDate")));
            OrdersMap.put("SellerID", CurrentUserID);
            OrdersMap.put("State", "Accepted");

            OrdersRef.push().setValue(OrdersMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    PendingRef.child(Objects.requireNonNull(getArguments().getString("ItemKey"))).removeValue()
                            .addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()){
                                    Toast.makeText(getActivity(), "Done", Toast.LENGTH_LONG).show();
                                    dismiss();
                                }else Toast.makeText(getActivity(),
                                        Objects.requireNonNull(task1.getException()).getMessage(),
                                        Toast.LENGTH_LONG).show();

                            });

                } else
                    Toast.makeText(getActivity(),
                            Objects.requireNonNull(task.getException()).getMessage(),
                            Toast.LENGTH_LONG).show();
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
        PendingRef = FirebaseDatabase.getInstance().getReference("Pending Requests");
        CurrentUserID = mAuth.getCurrentUser().getUid();
        OrdersMap = new HashMap<>();

        done_btn = v.findViewById(R.id.done_discount_btn);
        skip_btn = v.findViewById(R.id.skip_discount_btn);
        discount = v.findViewById(R.id.discount_edit_text);
        add_discount = v.findViewById(R.id.discount_check_box);
        new_price_tag = v.findViewById(R.id.new_price_tag);
        new_price_val = v.findViewById(R.id.new_price_value);

        new_price_val.setText(getArguments().getString("ItemPrice" + " EGP"));
        discount.setText("0");
        add_discount.setChecked(false);

        add_discount.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                discount.setVisibility(View.VISIBLE);
                new_price_tag.setVisibility(View.VISIBLE);
                new_price_val.setVisibility(View.VISIBLE);
            }else {
                discount.setVisibility(View.GONE);
                new_price_tag.setVisibility(View.GONE);
                new_price_val.setVisibility(View.GONE);
            }
        });


        done_btn.setOnClickListener(v1 -> {
            //  Toast.makeText(getActivity(),"Send Clicked",Toast.LENGTH_SHORT).show();
            CheckDataAndSendAccept();

        });
        skip_btn.setOnClickListener(v12 -> {
            SendOrderToFirebaseSkip();
        });

        if (getArguments() != null){
            price = Integer.parseInt(getArguments().getString("ItemPrice"));
        }
    //------------------------------------------------------
        txtListener = new TextWatcher() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                if (discount.length() == 0){
                    new_price_val.setText(price + " EGP");

                }else {
                    new_price_val.setText(CalcSalary(price) + " EGP");

                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                new_price_val.setText(price + " EGP");

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                if (discount.length() == 0){
                    new_price_val.setText(price + " EGP");
                }else {
                    new_price_val.setText(CalcSalary(price) + " EGP");
                }

            }
        };
        discount.addTextChangedListener(txtListener);

        //------------------------------------------------------
        return v;
    }
    private Integer CalcSalary(Integer p) {
       p= price ;
        quantity = Integer.parseInt(discount.getText().toString());
        TotalPrice = p - quantity;
        return TotalPrice;
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
