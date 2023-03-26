package com.example.user_managment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.example.user_managment.adapter.MyPaintingAdapter;
import com.example.user_managment.eventbus.MyUpdateCartEvent;
import com.example.user_managment.listener.ICartLoadListener;
import com.example.user_managment.listener.IPaintingLoadListener;
import com.example.user_managment.model.CartModel;
import com.example.user_managment.model.PaintingModel;
import com.example.user_managment.utils.SpaceItemDecoration;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SnapshotMetadata;
import com.nex3z.notificationbadge.NotificationBadge;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link paintingstore1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class paintingstore1 extends Fragment implements IPaintingLoadListener, ICartLoadListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    
    
    

    

    public paintingstore1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment paintingstore1.
     */
    // TODO: Rename and change types and number of parameters
    public static paintingstore1 newInstance(String param1, String param2) {
        paintingstore1 fragment = new paintingstore1();
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
        View v= inflater.inflate(R.layout.fragment_paintingstore1, container, false);
        //setHasOptionsMenu(true);
        init(v);
        loadPaintingFromFirebase();
        countCartItem();
        return v;
    }
    @BindView(R.id.recycler_painting)
    RecyclerView recyclerPainting;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R.id.badge)
    NotificationBadge badge;
    @BindView(R.id.btnCart)
    FrameLayout btnCart;
    @BindView(R.id.btnBack)
    ImageView btnBack;
    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

    IPaintingLoadListener paintingLoadListener;
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
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onUpdatecart(MyUpdateCartEvent event)
    {
        countCartItem();
    }

    private void loadPaintingFromFirebase() {
        List<PaintingModel> paintingModels = new ArrayList<>();
        FirebaseDatabase.getInstance()
                .getReference("Painting")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            for (DataSnapshot paintingSnapshot:snapshot.getChildren())
                            {
                                PaintingModel paintingModel = paintingSnapshot.getValue(PaintingModel.class);
                                paintingModel.setKey(paintingSnapshot.getKey());
                                paintingModels.add(paintingModel);
                            }
                            paintingLoadListener.onPaintingLoadSuccess(paintingModels);
                        }
                        else
                            paintingLoadListener.onPaintingLoadFailed("Can't find painting");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        paintingLoadListener.onPaintingLoadFailed(error.getMessage());
                    }
                });
    }
    private void init(View v) {
        ButterKnife.bind(this,v);
        paintingLoadListener = this;
        cartLoadListener = this;

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerPainting.setLayoutManager(gridLayoutManager);
        recyclerPainting.addItemDecoration(new SpaceItemDecoration());
        
        btnCart.setOnClickListener(view -> startActivity(new Intent(getActivity(),CartActivity.class)));
        btnBack.setOnClickListener(view -> startActivity(new Intent(getActivity(),HomePage.class)));
    }

    @Override
    public void onPaintingLoadSuccess(List<PaintingModel> paintingModelList) {
        MyPaintingAdapter adapter = new MyPaintingAdapter(getContext(),paintingModelList,cartLoadListener);
        recyclerPainting.setAdapter(adapter);
    }

    @Override
    public void onPaintingLoadFailed(String message) {
        Snackbar.make(frameLayout,message,Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onCartLoadSuccess(List<CartModel> cartModelList) {
        
        int cartsum = 0;
        for (CartModel cartModel: cartModelList)
            cartsum += cartModel.getQuantity();
            badge.setNumber(cartsum);
    }

    @Override
    public void onCartLoadFailed(String message) {
        
        Snackbar.make(frameLayout,message,Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        countCartItem();
    }

    private void countCartItem() {
        List<CartModel> cartModels = new ArrayList<>();
        FirebaseDatabase
                .getInstance().getReference("Cart")
                .child(currentFirebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot cartSnapshot: snapshot.getChildren())
                        {
                            CartModel cartModel = cartSnapshot.getValue(CartModel.class);
                            cartModel.setKey(cartSnapshot.getKey());
                            cartModels.add(cartModel);
                        }
                        cartLoadListener.onCartLoadSuccess(cartModels);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        cartLoadListener.onCartLoadFailed(error.getMessage());
                    }
                });
    }
}