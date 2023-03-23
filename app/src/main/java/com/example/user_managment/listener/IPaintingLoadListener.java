package com.example.user_managment.listener;

import com.example.user_managment.model.PaintingModel;

import java.util.List;

public interface IPaintingLoadListener {
    void onPaintingLoadSuccess(List<PaintingModel> paintingModelList);
    void onPaintingLoadFailed(String message);
}
