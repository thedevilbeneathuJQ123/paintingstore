package com.example.user_managment.adapter;

import android.content.Context;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.user_managment.BuildConfig;
import com.example.user_managment.R;
import com.example.user_managment.eventbus.MyUpdateCartEvent;
import com.example.user_managment.eventbus.MyUpdateCartEvent;
import com.example.user_managment.listener.ICartLoadListener;
import com.example.user_managment.listener.IRecyclerViewClickListener;
import com.example.user_managment.model.CartModel;
import com.example.user_managment.model.PaintingModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyPaintingAdapter extends RecyclerView.Adapter<MyPaintingAdapter.MyPaintingViewHolder> {
    
    private Context context;
    private List<PaintingModel> paintingModelList;
    private ICartLoadListener iCartLoadListener;

    public MyPaintingAdapter(Context context, List<PaintingModel> paintingModelList, ICartLoadListener iCartLoadListener) {
        this.context = context;
        this.paintingModelList = paintingModelList;
        this.iCartLoadListener = iCartLoadListener;
    }

    @NonNull
    @Override
    public MyPaintingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyPaintingViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_painting_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyPaintingViewHolder holder, int position) {
        Glide.with(context)
                .load(paintingModelList.get(position).getImage())
                .into(holder.imageView);
        holder.txtPrice.setText(new StringBuilder("$").append(paintingModelList.get(position).getPrice()));
        holder.txtName.setText(new StringBuilder().append(paintingModelList.get(position).getName()));
        
        holder.setListener((view, adapterPosition) -> {
            addtocart(paintingModelList.get(position));
        });
    }

    private void addtocart(PaintingModel paintingModel) {
        DatabaseReference userCart = FirebaseDatabase
                .getInstance()
                .getReference("Cart")
                .child("UNIQUE_USER_ID");
        userCart.child(paintingModel.getKey())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            CartModel cartModel = snapshot.getValue(CartModel.class);
                            cartModel.setQuantity(cartModel.getQuantity()+1);
                            Map<String,Object> updatedata = new HashMap<>();
                            updatedata.put("quantity", cartModel.getQuantity());
                            updatedata.put("totalprice",cartModel.getQuantity()*Float.parseFloat(cartModel.getPrice()));
                            
                            userCart.child(paintingModel.getKey())
                                    .updateChildren(updatedata)
                                    .addOnSuccessListener(unused -> {
                                        iCartLoadListener.onCartLoadFailed("Add to cart successfully");                                 
                                    })
                                    .addOnFailureListener(e -> iCartLoadListener.onCartLoadFailed(e.getMessage()));
                        }
                        else{
                            CartModel cartModel = new CartModel();
                            cartModel.setName(paintingModel.getName());
                            cartModel.setQuantity(1);
                            cartModel.setImage(paintingModel.getImage());
                            cartModel.setKey(paintingModel.getKey());
                            cartModel.setPrice(paintingModel.getPrice());
                            cartModel.setTotalprice(Float.parseFloat(paintingModel.getPrice()));
                            
                            userCart.child(paintingModel.getKey())
                                    .setValue(cartModel)
                                    .addOnSuccessListener(unused -> {
                                        iCartLoadListener.onCartLoadFailed("Add to cart successfully");
                                    })
                                    .addOnFailureListener(e -> iCartLoadListener.onCartLoadFailed(e.getMessage()));
                        }
                        EventBus.getDefault().postSticky(new MyUpdateCartEvent());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iCartLoadListener.onCartLoadFailed(error.getMessage());
                    }
                });
    }

    @Override
    public int getItemCount() {
        return paintingModelList.size();
    }

    public class MyPaintingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.txtName)
        TextView txtName;
        @BindView(R.id.txtprice)
        TextView txtPrice;

        IRecyclerViewClickListener listener;
        

        public void setListener(IRecyclerViewClickListener listener) {
            this.listener = listener;
        }

        private Unbinder unbinder;
        public MyPaintingViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onRecyclerClick(view,getAdapterPosition());
        }
    }
}
