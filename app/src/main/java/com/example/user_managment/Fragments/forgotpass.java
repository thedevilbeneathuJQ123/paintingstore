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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link forgotpass#newInstance} factory method to
 * create an instance of this fragment.
 */
public class forgotpass extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button btnGoToLogin,send;
    private EditText etemail;
    private FirebaseAuth auth;

    public forgotpass() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        // back to login
        btnGoToLogin=getView().findViewById(R.id.btnBackToLogin2);
        btnGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft =getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frameLayout, new Login());
                ft.commit();
            }
        });
        auth=FirebaseAuth.getInstance();
        etemail=getView().findViewById(R.id.etemail3);
        send=getView().findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email;
                email=etemail.getText().toString().trim();
                if(!isEmailvalid(email)){
                    etemail.setError("this is not a valid email");
                    etemail.requestFocus();
                }
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                   @Override
                                                   public void onComplete(@NonNull Task<Void> task) {
                                                       if(task.isSuccessful()){
                                                           Toast.makeText(getContext(), "Check Your Email Box", Toast.LENGTH_SHORT).show();
                                                       }else{
                                                           Toast.makeText(getContext(), "link not sent", Toast.LENGTH_SHORT).show();
                                                       }
                                                   }});

            }
        });
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment frogotpass.
     */
    // TODO: Rename and change types and number of parameters
    public static forgotpass newInstance(String param1, String param2) {
        forgotpass fragment = new forgotpass();
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
    public boolean isEmailvalid(String email){
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgotpass, container, false);
    }
}