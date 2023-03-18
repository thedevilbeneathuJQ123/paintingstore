package com.example.user_managment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class HomePage extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    TextView tx1;
    Button delete,signout,seedetails,editprof,gotostore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        auth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        user= auth.getCurrentUser();
        delete=findViewById(R.id.Delete);
        tx1=findViewById(R.id.textView);
        seedetails=findViewById(R.id.seedetails);
        signout=findViewById(R.id.signout);
        editprof=findViewById(R.id.Edit);
        gotostore=findViewById(R.id.paintingstore);
    }

    public void signout(View view) {
        auth.signOut();
        Intent i=new Intent(HomePage.this,MainActivity.class);
        startActivity(i);
    }

    public void Deleteprofile(View view) {Showdialog();}
    private void Showdialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
        builder.setTitle("Delete");
        builder.setMessage("are you sure you want to delete this profile");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(HomePage.this, "profile deleted", Toast.LENGTH_SHORT).show();
                        Deleteuserfromfirestore();
                        Intent i=new Intent(HomePage.this,MainActivity.class);
                        startActivity(i);
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(HomePage.this, "profile unsuccessful to delete", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        
    }

    public void Toeditprofile(View view) {
        FragmentTransaction ft =getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framelayout, new Editprofile());
        ft.commit();
        delete.setVisibility(View.GONE);
        tx1.setVisibility(View.GONE);
        editprof.setVisibility(View.GONE);
        signout.setVisibility(View.GONE);
        seedetails.setVisibility(View.GONE);
        gotostore.setVisibility(View.GONE);


    }

    public void seedetails(View view) {
       /* FragmentTransaction ft =getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framelayout, new seedetails());
        ft.commit();*/
        delete.setVisibility(View.GONE);
        tx1.setVisibility(View.GONE);
        editprof.setVisibility(View.GONE);
        signout.setVisibility(View.GONE);
        seedetails.setVisibility(View.GONE);
        gotostore.setVisibility(View.GONE);
        Intent i = getIntent();
        seedetails seedetails = new seedetails();
        String data = i.getStringExtra("username");
        if (data != null) {
            Utilities u = Utilities.getInstance();
            u.AddSeeDetailsBundlestring("username", data);
            data = i.getStringExtra("location");
            u.AddSeeDetailsBundlestring("location", data);
            data = i.getStringExtra("birthday");
            u.AddSeeDetailsBundlestring("birthday", data);
        } else {
            Utilities u = Utilities.getInstance();
            data = u.getStringSeeDetailsBundle("username");
            u.AddSeeDetailsBundlestring("username", data);
            data = u.getStringSeeDetailsBundle("location");
            u.AddSeeDetailsBundlestring("location", data);
            data = u.getStringSeeDetailsBundle("birthday");
            u.AddSeeDetailsBundlestring("birthday", data);
        }
        /*Bundle bundle = new Bundle();
        bundle.putString("username",data);
        data = i.getStringExtra("location");
        bundle.putString("location",data);
        data = i.getStringExtra("birthday");
        bundle.putString("birthday",data);
        seedetails.setArguments(bundle);*/
        getSupportFragmentManager().beginTransaction().add(R.id.framelayout, seedetails).commit();


    }
    public void Deleteuserfromfirestore(){
        db.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() && !task.getResult().isEmpty()) {
                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                    String documentID = documentSnapshot.getId();
                    db.collection("Users").document(documentID).delete();
                }
            }
        });
    }

    public void gotostore(View view) {
        FragmentTransaction ft =getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framelayout, new paintingstore1());
        ft.commit();
        delete.setVisibility(View.GONE);
        tx1.setVisibility(View.GONE);
        editprof.setVisibility(View.GONE);
        signout.setVisibility(View.GONE);
        seedetails.setVisibility(View.GONE);
        gotostore.setVisibility(View.GONE);
    }
}