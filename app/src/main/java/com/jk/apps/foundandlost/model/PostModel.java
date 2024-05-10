package com.jk.apps.foundandlost.model;

public class PostModel {

    public int postId;
    public String postName, postPhone, postInfo;
    public long postDate;
    public String postLocation;
    public double latitude, longitude;
    public int postType;

    public PostModel(int postId, String postName, String postPhone, String postInfo, long postDate, String postLocation, double latitude, double longitude, int postType) {
        this.postId = postId;
        this.postName = postName;
        this.postPhone = postPhone;
        this.postInfo = postInfo;
        this.postDate = postDate;
        this.postLocation = postLocation;
        this.latitude = latitude;
        this.longitude = longitude;
        this.postType = postType;
    }
}
