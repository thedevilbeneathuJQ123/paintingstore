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
import com.example.user_managment.model.PaintingModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyPaintingAdapter extends RecyclerView.Adapter<MyPaintingAdapter.MyPaintingViewHolder> {
    
    private Context context;
    private List<PaintingModel> paintingModelList;

    public MyPaintingAdapter(Context context, List<PaintingModel> paintingModelList) {
        this.context = context;
        this.paintingModelList = paintingModelList;
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
    }

    @Override
    public int getItemCount() {
        return paintingModelList.size();
    }

    public class MyPaintingViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.txtName)
        TextView txtName;
        @BindView(R.id.txtprice)
        TextView txtPrice;
        
        private Unbinder unbinder;
        public MyPaintingViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
        }
    }
}
