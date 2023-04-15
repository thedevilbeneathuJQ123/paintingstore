package com.example.user_managment.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user_managment.Fragments.Checkout_Card_Details;
import com.example.user_managment.Fragments.paintingstore;
import com.example.user_managment.R;
import com.example.user_managment.adapter.MyCartAdapter;
import com.example.user_managment.eventbus.MyUpdateCartEvent;
import com.example.user_managment.listener.ICartLoadListener;
import com.example.user_managment.model.CartModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartActivity extends AppCompatActivity implements ICartLoadListener {

    @BindView(R.id.recycler_cart)
    RecyclerView recyclerCart;
    @BindView(R.id.Mainlayout)
    RelativeLayout MainLayout;
    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.txtTotal)
    TextView txtTotal;
    Button checkout;
    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    ICartLoadListener cartLoadListener;


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        if (EventBus.getDefault().hasSubscriberForEvent(MyUpdateCartEvent.class))
            EventBus.getDefault().removeStickyEvent(MyUpdateCartEvent.class);
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    //updatecart
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onUpdatecart(MyUpdateCartEvent event)
    {
        loadCartFromFirebase();
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        
        Init();
        loadCartFromFirebase();
        checkout = findViewById(R.id.checkout);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<CartModel> cartModels = new ArrayList<>();
                FirebaseDatabase.getInstance()
                        .getReference("Cart")
                        .child(currentFirebaseUser.getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    for (DataSnapshot cartsnapshot:snapshot.getChildren())
                                    {
                                        CartModel cartModel = cartsnapshot.getValue(CartModel.class);
                                        cartModel.setKey(cartsnapshot.getKey());
                                        cartModels.add(cartModel);
                                        recyclerCart.setVisibility(View.GONE);
                                        btnBack.setVisibility(View.GONE);
                                        txtTotal.setVisibility(View.GONE);
                                        checkout.setVisibility(View.GONE);
                                        FragmentTransaction ft =getSupportFragmentManager().beginTransaction();
                                        ft.replace(R.id.Mainlayout, new Checkout_Card_Details());
                                        ft.commit();
                                       
                                    }
                                    cartLoadListener.onCartLoadSuccess(cartModels);
                                }
                                else
                                    cartLoadListener.onCartLoadFailed("cart empty");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                cartLoadListener.onCartLoadFailed(error.getMessage());
                            }
                        });
            }
        });
    }

    private void loadCartFromFirebase() {
        List<CartModel> cartModels = new ArrayList<>();
        FirebaseDatabase.getInstance()
                .getReference("Cart")
                .child(currentFirebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot cartsnapshot:snapshot.getChildren())
                            {
                                CartModel cartModel = cartsnapshot.getValue(CartModel.class);
                                cartModel.setKey(cartsnapshot.getKey());
                                cartModels.add(cartModel);
                            }
                            cartLoadListener.onCartLoadSuccess(cartModels);
                        }
                        else {
                            txtTotal.setText("");
                            cartLoadListener.onCartLoadFailed("cart empty");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                       cartLoadListener.onCartLoadFailed(error.getMessage());
                    }
                });
    }

    private void Init() {
        ButterKnife.bind(this);
        
        cartLoadListener = this;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerCart.setLayoutManager(layoutManager);
        recyclerCart.addItemDecoration(new DividerItemDecoration(this,layoutManager.getOrientation()));
        
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft =getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.Mainlayout, new paintingstore());
                ft.commit();
            }
        });
    }

    @Override
    public void onCartLoadSuccess(List<CartModel> cartModelList) {
        double sum = 0;
        for (CartModel cartModel : cartModelList)
        {
            sum+=cartModel.getTotalprice();
        }
        txtTotal.setText(new StringBuilder("$").append(sum));
        MyCartAdapter adapter = new MyCartAdapter(this,cartModelList);
        recyclerCart.setAdapter(adapter);
    }

    @Override
    public void onCartLoadFailed(String message) {
        Snackbar.make(MainLayout,message,Snackbar.LENGTH_LONG).show();
    }
}