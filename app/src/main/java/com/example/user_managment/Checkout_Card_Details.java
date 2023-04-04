package com.example.user_managment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.example.user_managment.eventbus.MyUpdateCartEvent;
import com.example.user_managment.model.CartModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;

//import com.braintreepayments.cardform.view.CardForm;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Checkout_Card_Details#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Checkout_Card_Details extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    CardForm cardForm;
    Button buy,backtocart;
    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public void onStart() {
        super.onStart();
        // setup the credit card form
        cardForm = getView().findViewById(R.id.card_form);
        buy = getView().findViewById(R.id.btnBuy);
        backtocart = getView().findViewById(R.id.backtocart);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("SMS is required on this number")
                .setup(getActivity());
        // make cvv crypted
        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cardForm.isValid()) {
                    // show the card details in a pop up message to confirm

                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                    alertBuilder.setTitle("Confirm before purchase");
                    alertBuilder.setMessage("Card number: " + cardForm.getCardNumber() + "\n" +
                            "Card expiry date: " + cardForm.getExpirationDateEditText().getText().toString() + "\n" +
                            "Card CVV: " + cardForm.getCvv() + "\n" +
                            "Postal code: " + cardForm.getPostalCode() + "\n" +
                            "Phone number: " + cardForm.getMobileNumber());
                    alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            Toast.makeText(getContext(), "Thank you for purchase", Toast.LENGTH_LONG).show();
                            deleteFromFirebase();
                            FragmentTransaction ft =getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.framelayout2, new paintingstore1());
                            ft.commit();
                        }
                    });
                    alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();
                }else {
                    Toast.makeText(getContext(), "Please complete the form", Toast.LENGTH_LONG).show();
                }
            }
        });
        backtocart.setOnClickListener(view -> startActivity(new Intent(getActivity(),CartActivity.class)));


    }

    private void deleteFromFirebase() {
        FirebaseDatabase.getInstance()
                .getReference("Cart")
                .child(currentFirebaseUser.getUid())
                .removeValue()
                .addOnSuccessListener(aVoid-> EventBus.getDefault().postSticky(new MyUpdateCartEvent()));
    }

    public Checkout_Card_Details() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Checkout_Card_Details.
     */
    // TODO: Rename and change types and number of parameters
    public static Checkout_Card_Details newInstance(String param1, String param2) {
        Checkout_Card_Details fragment = new Checkout_Card_Details();
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
        return inflater.inflate(R.layout.fragment_checkout__card__details, container, false);
    }
}