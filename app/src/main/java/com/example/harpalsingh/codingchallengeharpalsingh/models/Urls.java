package com.example.harpalsingh.codingchallengeharpalsingh.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Urls implements Serializable {

    @SerializedName("regular")
    @Expose
    private String regular;
    @SerializedName("small")
    @Expose
    private String small;
    @SerializedName("thumb")
    @Expose
    private String thumb;

    public String getRegular() {
        return regular;
    }

    public String getThumb() {
        return thumb;
    }
}
