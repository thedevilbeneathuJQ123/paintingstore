package com.example.user_managment;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        connectComponents();
        mAuth = FirebaseAuth.getInstance();
        btnGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft =getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frameLayout, new Login());
                ft.commit();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check();
           }
        });
    }
    


        public void Check(){
        String email = etemailRegister.getText().toString().trim();
        String password = etpasswordRegister.getText().toString().trim();
        String confirmpassword = etconfirmpasswordRegister.getText().toString().trim();
        if( email.trim().isEmpty() || password.trim().isEmpty() || confirmpassword.trim().isEmpty())
        {
            Toast.makeText(getContext(), "some feilds are missing!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isEmailvalid(email))
        {
            Toast.makeText(getContext(), "Email is incorrect!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length()<6)
        {
            Toast.makeText(getContext(), "the password should be 6 or more characters", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!password.equals(confirmpassword))
        {
            Toast.makeText(getContext(), "the confirmed password is not identical", Toast.LENGTH_SHORT).show();
            return;
        }
       
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FragmentTransaction ft =getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frameLayout, new profile());
                    ft.commit();
                    Toast.makeText(getContext(), "succeded to create an account", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "failed to create account!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_register, container, false);
        return v;
    }




    public boolean isEmailvalid(String email){
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }



    private FirebaseFirestore fstore = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;
    private Button btnRegister,btnGoToLogin;
    private EditText etemailRegister,etpasswordRegister,etconfirmpasswordRegister,etdate,etusername,etlocation;
     

    private void connectComponents() {
        etemailRegister = getView().findViewById(R.id.etEmailRegister);
        etpasswordRegister = getView().findViewById(R.id.etPasswordRegister);
        etconfirmpasswordRegister = getView().findViewById(R.id.etPasswordConfirm);
        btnRegister = getView().findViewById(R.id.btnRegister);
        btnGoToLogin = getView().findViewById(R.id.btnBackToLogin);
    }

}