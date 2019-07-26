package com.elasdka2.zar3tyseller;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Setting.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Setting#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Setting extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    //------------------------------------------------
    Context context;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef;
    String CurrentUserID;
    private BottomSheetBehavior bottomSheetBehavior;
    //------------------------------------------------
    @BindView(R.id.AboutZar3tyCard)
    CardView AboutCard;
    @BindView(R.id.TermsConditionsCard)
    CardView TermsCard;
    @BindView(R.id.RateUsCard)
    CardView RateCard;
    @BindView(R.id.HelpCard)
    CardView HelpCard;
    @BindView(R.id.ComplaintCard)
    CardView ComplaintCard;
    @BindView(R.id.LanguageCard)
    CardView LanguageCard;
    //------------------------------------------------
    @OnClick(R.id.AboutZar3tyCard)
    public void GoToAbout(){
        Toast.makeText(context.getApplicationContext(),"it will done soon isa ...",Toast.LENGTH_SHORT).show();
    }
    @OnClick(R.id.TermsConditionsCard)
    public void GoToTerms(){
        Toast.makeText(context.getApplicationContext(),"it will done soon isa ...",Toast.LENGTH_SHORT).show();
    }
    @OnClick(R.id.RateUsCard)
    public void GoToRate(){
        Toast.makeText(context.getApplicationContext(),"it will done soon isa ...",Toast.LENGTH_SHORT).show();
    }
    @OnClick(R.id.HelpCard)
    public void GoToHelp(){
        Toast.makeText(context.getApplicationContext(),"it will done soon isa ...",Toast.LENGTH_SHORT).show();
    }
    @OnClick(R.id.ComplaintCard)
    public void GoToComplaint(){
        /*Toast.makeText(context.getApplicationContext(),"it will done soon isa ...",Toast.LENGTH_SHORT).show();*/
        SendComplaint();
    }
    @OnClick(R.id.LanguageCard)
    public void GoToLanguage(){
        //Toast.makeText(context.getApplicationContext(),"it will done soon isa ...",Toast.LENGTH_SHORT).show();
        LanguageBottomSheetDialog bottomSheetDialog = new LanguageBottomSheetDialog();
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show(getChildFragmentManager(),"ExampleBottomSheet");
    }

    private void SendComplaint() {
        ComplaintBottomSheetDialog bottomSheetDialog = new ComplaintBottomSheetDialog();
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show(getChildFragmentManager(),"ExampleBottomSheet");
    }

    //------------------------------------------------
    public Setting() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Setting.
     */
    // TODO: Rename and change types and number of parameters
    public static Setting newInstance(String param1, String param2) {
        Setting fragment = new Setting();
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
        View v = inflater.inflate(R.layout.setting_frag, container, false);
        context = getActivity();
        ButterKnife.bind(this,v);


        mAuth = FirebaseAuth.getInstance();
        UserRef = FirebaseDatabase.getInstance().getReference("Users").child("Sellers");
        CurrentUserID = mAuth.getCurrentUser().getUid();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = ((FragmentActivity) context).getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }

        return v;
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
