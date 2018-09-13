package com.example.harpalsingh.codingchallengeharpalsingh.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Urls implements Serializable {

    @SerializedName("raw")
    @Expose
    private String raw;
    @SerializedName("full")
    @Expose
    private String full;
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
