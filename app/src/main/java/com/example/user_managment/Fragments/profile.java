package com.example.user_managment.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.user_managment.R;
import com.example.user_managment.utils.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profile.
     */
    // TODO: Rename and change types and number of parameters
    public static profile newInstance(String param1, String param2) {
        profile fragment = new profile();
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
    public void onStart() {
        super.onStart();
        btnadd = getView().findViewById(R.id.adduser);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddusertoRealtimedatabase();
                FragmentTransaction ft =getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.framelayout2, new paintingstore());
                ft.commit();
            }
        });
    }

    public void AddusertoRealtimedatabase(){
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        mUser = mAuth.getCurrentUser();
        etdate = getView().findViewById(R.id.ETdate);
        etusername = getView().findViewById(R.id.etusername);
        etlocation = getView().findViewById(R.id.etlocation);
        if(etusername.getText().toString().trim().isEmpty() || etlocation.getText().toString().trim().isEmpty() || etdate.getText().toString().trim().isEmpty())
        {
            Toast.makeText(getContext(), "some feilds are missing!", Toast.LENGTH_SHORT).show();
            return;
        }
        User user = new User(etusername.getText().toString().trim(),etlocation.getText().toString().trim(),etdate.getText().toString().trim());
        if(mUser.getUid()!=null)
        {
            mDatabase.child(mUser.getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getContext(), "User has been added", Toast.LENGTH_SHORT).show();
                    return;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "User failed to get added", Toast.LENGTH_SHORT).show();
                    return;
                }
            });
        }
    }

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;
    private EditText etdate,etusername,etlocation;
    private Button btnadd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}