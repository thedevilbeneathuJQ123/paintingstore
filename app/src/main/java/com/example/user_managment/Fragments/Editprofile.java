package com.example.user_managment.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.user_managment.Activities.HomePage;
import com.example.user_managment.R;
import com.example.user_managment.utils.Utilities;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Editprofile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Editprofile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Editprofile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Editprofile.
     */
    // TODO: Rename and change types and number of parameters
    public static Editprofile newInstance(String param1, String param2) {
        Editprofile fragment = new Editprofile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        connectComponents();
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), HomePage.class);
                startActivity(i);
            }
        });
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=newname.getText().toString();
                String date=newdate.getText().toString();
                String location=newlocation.getText().toString();
                Updatedata(name,date,location);
            }
        });
    }

    private void Updatedata(String name,String date, String location){

        if( name.trim().isEmpty() || date.trim().isEmpty() || location.trim().isEmpty())
        {
            Toast.makeText(getContext(), "some feilds are missing!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        mDatabase.child(mUser.getUid()).child("username").setValue(name);
        mDatabase.child(mUser.getUid()).child("birthday").setValue(date);
        mDatabase.child(mUser.getUid()).child("location").setValue(location);
        Utilities u = Utilities.getInstance();
        u.AddSeeDetailsBundlestring("username", name);
        u.AddSeeDetailsBundlestring("location", date);
        u.AddSeeDetailsBundlestring("birthday", location);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private void connectComponents() {
        newname =getView().findViewById(R.id.etname2);
        newdate = getView().findViewById(R.id.dateofbirth2);
        newlocation = getView().findViewById(R.id.location2);
        updatebtn = getView().findViewById(R.id.updatebtn);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        mUser = mAuth.getCurrentUser();
        goback = getView().findViewById(R.id.goback);
    }

    Button updatebtn,goback;
    EditText newname,newdate,newlocation;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_editprofile, container, false);
    }
}