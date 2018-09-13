package com.example.harpalsingh.codingchallengeharpalsingh.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Links_ implements Serializable {

    @SerializedName("self")
    @Expose
    private String self;
    @SerializedName("html")
    @Expose
    private String html;
    @SerializedName("photos")
    @Expose
    private String photos;
    @SerializedName("likes")
    @Expose
    private String likes;
    @SerializedName("portfolio")
    @Expose
    private String portfolio;
    @SerializedName("following")
    @Expose
    private String following;
    @SerializedName("followers")
    @Expose
    private String followers;

}
