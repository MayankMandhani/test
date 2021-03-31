package com.example.myalbums;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumListViewHolder> {
    private Context context;
    private List<Album> albumList;
    private RecyclerView.Adapter adapter;
    private List<String> photoList=new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    public AlbumAdapter(Context context, List<Album>albumList){
        this.context=context;
        this.albumList=albumList;
    }

    @NonNull
    @Override
    public AlbumListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView= LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item, parent,false);
        RecyclerView.LayoutParams lp=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        AlbumListViewHolder rcv=new AlbumListViewHolder(layoutView);
        return rcv;
    }

    private void initialiseRecyclerView(RecyclerView recyclerView,final int position) {
        photoList=albumList.get(position).getPhotoList();
        recyclerView.setHasFixedSize(false);
        layoutManager=new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new PhotosAdapter(photoList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumListViewHolder holder, final int position) {
        holder.mTitle.setText(albumList.get(position).getTitle());
        holder.mGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.currentAlbum=position;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                ((Activity) context).startActivityForResult(Intent.createChooser(intent,"Select Photos"), 1);
                adapter.notifyDataSetChanged();
            }
        });
        initialiseRecyclerView(holder.mPhotos,position);
    }


    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public class AlbumListViewHolder extends RecyclerView.ViewHolder{
        public TextView mTitle;
        public RecyclerView mPhotos;
        public ImageView mGallery;
        public AlbumListViewHolder(View view){
            super(view);
            mTitle=view.findViewById(R.id.title);
            mGallery=view.findViewById(R.id.gallery);
            mPhotos=view.findViewById(R.id.photos);
        }
    }
}

