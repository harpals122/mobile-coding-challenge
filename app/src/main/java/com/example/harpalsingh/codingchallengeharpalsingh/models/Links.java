package com.example.harpalsingh.codingchallengeharpalsingh.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Links implements Serializable {

    @SerializedName("self")
    @Expose
    private String self;
    @SerializedName("html")
    @Expose
    private String html;
    @SerializedName("download")
    @Expose
    private String download;
    @SerializedName("download_location")
    @Expose
    private String downloadLocation;


    public String getHtml() {
        return html;
    }

    public String getDownload() {
        return download;
    }

    public String getDownloadLocation() {
        return downloadLocation;
    }

    public String getSelf() {
        return self;
    }
}
