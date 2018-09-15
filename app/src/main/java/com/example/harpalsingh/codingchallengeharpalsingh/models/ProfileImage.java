package com.example.harpalsingh.codingchallengeharpalsingh.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProfileImage implements Serializable {

    @SerializedName("small")
    @Expose
    private String small;
    @SerializedName("medium")
    @Expose
    private String medium;
    @SerializedName("large")
    @Expose
    private String large;

    public String getMedium() {
        return medium;
    }

}
