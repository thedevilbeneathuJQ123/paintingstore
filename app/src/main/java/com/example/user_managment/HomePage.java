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

import com.example.user_managment.eventbus.MyUpdateCartEvent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import org.greenrobot.eventbus.EventBus;

public class HomePage extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private DatabaseReference databaseReference;
    TextView tx1;
    Button delete,signout,seedetails,editprof,gotostore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        auth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        user= auth.getCurrentUser();
        delete=findViewById(R.id.Delete);
        tx1=findViewById(R.id.textView);
        seedetails=findViewById(R.id.seedetails);
        signout=findViewById(R.id.signout);
        editprof=findViewById(R.id.Edit);
        gotostore=findViewById(R.id.paintingstore);
        if (user.getUid()!=null)
        {
            getUserdata();
        }
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
                        DeleteuserfromRealtimedatabase();
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
        seedetails seedetails = new seedetails();
        getSupportFragmentManager().beginTransaction().add(R.id.framelayout, seedetails).commit();
    }

    private void getUserdata() {
        databaseReference.child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful())
                {
                  if (task.getResult().exists()){
                    //  Toast.makeText(HomePage.this, "Successfully read", Toast.LENGTH_SHORT).show();
                      DataSnapshot dataSnapshot = task.getResult();
                      Utilities u = Utilities.getInstance();
                      u.AddSeeDetailsBundlestring("username", String.valueOf(dataSnapshot.child("username").getValue()));
                      u.AddSeeDetailsBundlestring("location", String.valueOf(dataSnapshot.child("location").getValue()));
                      u.AddSeeDetailsBundlestring("birthday", String.valueOf(dataSnapshot.child("birthday").getValue()));
                      
                  }else {
                     // Toast.makeText(HomePage.this, "User does not exist", Toast.LENGTH_SHORT).show();
                  }
                }
                else {
                    //Toast.makeText(HomePage.this, "failed to get data", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
    }

    public void DeleteuserfromRealtimedatabase(){
        if (user.getUid()!=null)
        {
            FirebaseDatabase.getInstance()
                .getReference("Cart")
                .child(user.getUid())
                .removeValue()
                .addOnSuccessListener(aVoid-> EventBus.getDefault().postSticky(new MyUpdateCartEvent()));
            databaseReference.child(user.getUid()).removeValue();
        }
    }

    public void gotostore(View view) {
        FragmentTransaction ft =getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framelayout, new paintingstore());
        ft.commit();
        delete.setVisibility(View.GONE);
        tx1.setVisibility(View.GONE);
        editprof.setVisibility(View.GONE);
        signout.setVisibility(View.GONE);
        seedetails.setVisibility(View.GONE);
        gotostore.setVisibility(View.GONE);
    }
}