package com.example.user_managment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Login extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView tvRegister,forgot;
    private EditText etemail, etpassword;
    private Button btnLogin;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;


    @Override
    public void onStart() {
        super.onStart();
        connectComponents();
        //forgot
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction ft =getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frameLayout, new forgotpass());
                ft.commit();
            }
        });
        //Register
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegister(view);
            }
        });
        //Login Button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn(view);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

    }

    public void logIn(View view) {
        String email=etemail.getText().toString().trim();
        String password=etpassword.getText().toString().trim();
        Intent i = new Intent(getContext(),HomePage.class);
        if (password.isEmpty()||email.isEmpty()){
            Toast.makeText(getContext(), "Fields are empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getContext(), "Logged in successfully (Y)", Toast.LENGTH_SHORT).show();
                    startActivity(i);
                }else {
                    Toast.makeText(getContext(), "log in failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void goToRegister(View view) {
//        btnLogin.setVisibility(View.GONE);
//        etpassword.setVisibility(View.GONE);
//        etemail.setVisibility(View.GONE);
//        tvRegister.setVisibility(View.GONE);
        FragmentTransaction ft =getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, new RegisterFragment());
        ft.commit();
    }

    private void connectComponents() {
        tvRegister =getView().findViewById(R.id.tvRegister);
        etemail = getView().findViewById(R.id.etEmailLogin);
        etpassword = getView().findViewById(R.id.etPassLogin);
        btnLogin = getView().findViewById(R.id.btnLogin);
        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        forgot=getView().findViewById(R.id.forgot);
    }

    public Login() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Login.
     */
    // TODO: Rename and change types and number of parameters
    public static Login newInstance(String param1, String param2) {
        Login fragment = new Login();
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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }
}