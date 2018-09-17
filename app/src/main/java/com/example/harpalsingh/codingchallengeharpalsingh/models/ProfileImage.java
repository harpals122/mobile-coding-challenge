package com.example.harpalsingh.codingchallengeharpalsingh.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProfileImage implements Serializable {

    @SerializedName("medium")
    @Expose
    private String medium;

    public String getMedium() {
        return medium;
    }
}
