package com.example.myalbums;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotosViewHolder> {
    List<String> Photos;
    public PhotosAdapter(List<String> Photos){
        this.Photos=Photos;
    }

    @NonNull
    @Override
    public PhotosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView= LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item, parent,false);
        RecyclerView.LayoutParams lp=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutView.setLayoutParams(lp);

        PhotosViewHolder rcv=new PhotosViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull final PhotosViewHolder holder, final int position) {
        String path=Photos.get(position);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(path,bmOptions);
        holder.mPhoto.setImageBitmap(bitmap);
        holder.mPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @Override

    public int getItemCount() {
        return Photos.size();
    }

    public class PhotosViewHolder extends RecyclerView.ViewHolder{
        public ImageView mPhoto;
        public PhotosViewHolder(View view){
            super(view);
            mPhoto=view.findViewById(R.id.photo);
        }
    }
}

