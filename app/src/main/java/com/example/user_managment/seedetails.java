package com.example.user_managment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link seedetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class seedetails extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button goback;
    private TextView username,birthday,location;
    FirebaseFirestore fstore;
    FirebaseAuth fauth;
    String userid;

    @Override
    public void onStart() {
        super.onStart();

        goback = getView().findViewById(R.id.gobaCK);
   

       /* username = getView().findViewById(R.id.username);
        birthday = getView().findViewById(R.id.birthday);
        location = getView().findViewById(R.id.location);
       // this way cant work because of the emulators i use because of a google service error 
      fauth = FirebaseAuth.getInstance();  
        fstore = FirebaseFirestore.getInstance();
        userid = fauth.getCurrentUser().getUid();
        
      DocumentReference documentReference = fstore.collection("Users").document(userid);
       documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
           @Override
           public void onSuccess(DocumentSnapshot documentSnapshot) {
               if(documentSnapshot.exists())
               {
                   username.setText(documentSnapshot.getString("username"));
                   birthday.setText(documentSnapshot.getString("birthday"));
                   location.setText(documentSnapshot.getString("location"));
               }
               else Toast.makeText(getContext(), "data not found", Toast.LENGTH_SHORT).show();

           }
       })
               .addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(getContext(), "failed to fetch data", Toast.LENGTH_SHORT).show();
                   }
               });*/
               /*
      documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                username.setText(documentSnapshot.getString("username"));
                birthday.setText(documentSnapshot.getString("birthday"));
                location.setText(documentSnapshot.getString("location"));
            }
        });*/

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),HomePage.class);
                startActivity(i);
            }
        });
        Bundle bundle = this.getArguments();
        username = getView().findViewById(R.id.username);
        birthday = getView().findViewById(R.id.birthday);
        location = getView().findViewById(R.id.location);
        try {
            String data = bundle.getString("username");
            username.setText(data);
            data = bundle.getString("location");
            location.setText(data);
            data = bundle.getString("birthday");
            birthday.setText(data);
        }
        catch (NullPointerException e) {
            username.setText("nullpointerexception");
        }
   }

    public seedetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment seedetails.
     */
    // TODO: Rename and change types and number of parameters
    public static seedetails newInstance(String param1, String param2) {
        seedetails fragment = new seedetails();
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
        View v = inflater.inflate(R.layout.fragment_seedetails, container, false);
        return v;
    }
}