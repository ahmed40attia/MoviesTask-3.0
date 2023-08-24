package com.example.movies_task30.ui.adapter;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.example.movies_task30.util.Constant;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class BindingViewAdapter {
    @BindingAdapter("imageURL")
    public static void setImageURL (ImageView imageView,String URL){
        String BASE_IMAGE_URL = Constant.BASE_IMAGE_URL;
        String imagePath = BASE_IMAGE_URL + URL;
            try{
                imageView.setAlpha(0f);
                Picasso.get().load(imagePath).noFade().into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        imageView.animate().setDuration(300).alpha(1f).start();
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }catch (Exception ignore){

            }
    }
}
