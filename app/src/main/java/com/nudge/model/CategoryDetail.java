package com.nudge.model;

/**
 * Created by ADVANTAL on 7/7/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryDetail {

    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("pid")
    @Expose
    private String pid;
    @SerializedName("category")
    @Expose
    private String category;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
