package com.example.harpalsingh.codingchallengeharpalsingh.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("portfolio_url")
    @Expose
    private String portfolioUrl;
    @SerializedName("bio")
    @Expose
    private String bio;
    @SerializedName("profile_image")
    @Expose
    private ProfileImage profileImage;

    public String getFirstName() {
        return firstName;
    }

    public String getPortfolioUrl() {
        return portfolioUrl;
    }

    public String getBio() {
        return bio;
    }

    public ProfileImage getProfileImage() {
        return profileImage;
    }

}
