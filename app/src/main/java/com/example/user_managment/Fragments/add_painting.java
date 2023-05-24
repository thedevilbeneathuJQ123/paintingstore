package com.example.user_managment.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.user_managment.Activities.HomePage;
import com.example.user_managment.R;
import com.example.user_managment.utils.Utilities;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link add_painting#newInstance} factory method to
 * create an instance of this fragment.
 */
public class add_painting extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public add_painting() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment add_painting.
     */
    // TODO: Rename and change types and number of parameters
    public static add_painting newInstance(String param1, String param2) {
        add_painting fragment = new add_painting();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    ImageButton goback;
    Button save;
    EditText price,name;
    ImageView img;
    Uri imagepath;

    @Override
    public void onStart() {
        super.onStart();
        goback = getView().findViewById(R.id.back);
        save = getView().findViewById(R.id.savepainting);
        img = getView().findViewById(R.id.painting);
        price = getView().findViewById(R.id.price);
        name = getView().findViewById(R.id.nameofpainting);
        if(imagepath == null){
            Glide.with(getContext()).load(R.drawable.baseline_account_circle_24).into(img);}
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imagepath != null && !price.getText().toString().trim().isEmpty() && !name.getText().toString().trim().isEmpty())uploadimage();
                else Toast.makeText(getContext(), "some feilds are missing!", Toast.LENGTH_SHORT).show();
            }
        });
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), HomePage.class);
                startActivity(i);
            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                photoIntent.setType("image/*");
                someActivityResultLauncher.launch(photoIntent);
            }
        });
    }

    private void uploadimage() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        img.setDrawingCacheEnabled(true);
        img.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        String path = "images/" + UUID.randomUUID() + ".jpg"; // generate unique filename
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(path);

        UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Get the URL of the uploaded image
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Get a reference to the database
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Painting");
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                long numOFelem = dataSnapshot.getChildrenCount();
                                String key = String.valueOf(++numOFelem);
                                Map<String, String> painting = new HashMap<>();
                                painting.put("name", name.getText().toString().trim());
                                painting.put("price", price.getText().toString().trim());
                                painting.put("image", uri.toString());
                                databaseReference.child(key).setValue(painting);
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(getContext(), "Did Not Add Painting", Toast.LENGTH_SHORT).show();
                            }
                        });
                       
                    }
                });
            }
        });

    }
    

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK ) {
                        // There are no request codes
                        Intent data = result.getData();
                        imagepath = data.getData();
                        getimageInimageview();
                    }
                }
            });

    private void getimageInimageview() {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(),imagepath);
        } catch (IOException e){
            e.printStackTrace();
        }
        Glide.with(getContext()).clear(img);
        img.setImageBitmap(bitmap);
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
        return inflater.inflate(R.layout.fragment_add_painting, container, false);
    }
}