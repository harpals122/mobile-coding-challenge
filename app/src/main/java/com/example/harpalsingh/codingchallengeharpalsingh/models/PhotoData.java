package com.example.harpalsingh.codingchallengeharpalsingh.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PhotoData implements Serializable {

    @SerializedName("width")
    @Expose
    private Integer width;
    @SerializedName("height")
    @Expose
    private Integer height;
    @SerializedName("urls")
    @Expose
    private Urls urls;
    @SerializedName("likes")
    @Expose
    private Integer likes;
    @SerializedName("user")
    @Expose
    private User user;

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public Urls getUrls() {
        return urls;
    }

    public Integer getLikes() {
        return likes;
    }

    public User getUser() {
        return user;
    }
}