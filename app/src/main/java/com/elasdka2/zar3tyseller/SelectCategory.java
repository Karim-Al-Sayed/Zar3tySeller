package com.elasdka2.zar3tyseller;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SelectCategory extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SelectCategory() {
        // Required empty public constructor
    }
    //--------------------------------
    Context context;

    @BindView(R.id.AgricultureCard)
    CardView AgricultureCard;
    @BindView(R.id.IrrigationCard)
    CardView IrrigationCard;
    @BindView(R.id.Industrial_AgricultureCard)
    CardView Industrial_AgricultureCard;

    //--------------------------------
    @OnClick(R.id.AgricultureCard)
    public void Agriculture(){
        AddItem fragment = new AddItem();
        Bundle args = new Bundle();
        args.putString("Category", "Agriculture");
        args.putString("UniqueID", "SelectCategory");
        fragment.setArguments(args);

        FragmentTransaction fragmentTransaction1 = getFragmentManager().beginTransaction();
        fragmentTransaction1.setCustomAnimations(R.animator.fade_in,R.animator.fade_out);
        fragmentTransaction1.replace(R.id.Frame_Content,fragment,"AddItem");
        fragmentTransaction1.commit();
    }

    @OnClick(R.id.IrrigationCard)
    public void Irrigation(){
        AddItem fragment = new AddItem();
        Bundle args = new Bundle();
        args.putString("Category", "Irrigation");
        args.putString("UniqueID", "SelectCategory");
        fragment.setArguments(args);

        FragmentTransaction fragmentTransaction1 = getFragmentManager().beginTransaction();
        fragmentTransaction1.setCustomAnimations(R.animator.fade_in,R.animator.fade_out);
        fragmentTransaction1.replace(R.id.Frame_Content,fragment,"AddItem");
        fragmentTransaction1.commit();
    }

    @OnClick(R.id.Industrial_AgricultureCard)
    public void Industrial_Agriculture(){
        AddItem fragment = new AddItem();
        Bundle args = new Bundle();
        args.putString("Category", "Industrial_Agriculture");
        args.putString("UniqueID", "SelectCategory");
        fragment.setArguments(args);

        FragmentTransaction fragmentTransaction1 = getFragmentManager().beginTransaction();
        fragmentTransaction1.setCustomAnimations(R.animator.fade_in,R.animator.fade_out);
        fragmentTransaction1.replace(R.id.Frame_Content,fragment,"AddItem");
        fragmentTransaction1.commit();
    }
    //--------------------------------


    // TODO: Rename and change types and number of parameters
    public static SelectCategory newInstance(String param1, String param2) {
        SelectCategory fragment = new SelectCategory();
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
        View v = inflater.inflate(R.layout.select_category_frag, container, false);
        context = getActivity();
        ButterKnife.bind(this, v);
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
