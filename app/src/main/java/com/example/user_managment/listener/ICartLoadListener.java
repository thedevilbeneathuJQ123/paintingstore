package com.example.user_managment.listener;

import com.example.user_managment.model.CartModel;
import com.example.user_managment.model.PaintingModel;

import java.util.List;

public interface ICartLoadListener {
    void onCartLoadSuccess(List<CartModel> cartModelList);
    void onCartLoadFailed(String message);
}
