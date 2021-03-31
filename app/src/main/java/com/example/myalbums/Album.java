package com.example.myalbums;

import java.util.List;

public class Album {
    String title;
    List<String> photoList;
    public Album(String title,List<String> photoList){
        this.photoList=photoList;
        this.title=title;
    }

    public List<String> getPhotoList() {
        return photoList;
    }

    public String getTitle() {
        return title;
    }
}
