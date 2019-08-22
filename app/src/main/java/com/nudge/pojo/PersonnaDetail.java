package com.nudge.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ADVANTAL on 7/12/2017.
 */
public class PersonnaDetail {
    @SerializedName("pers_image")
    @Expose
    private String persImage;
    @SerializedName("pers_id")
    @Expose
    private String persId;
    @SerializedName("pers_name")
    @Expose
    private String persName;

    public String getPersImage() {
        return persImage;
    }

    public void setPersImage(String persImage) {
        this.persImage = persImage;
    }

    public String getPersId() {
        return persId;
    }

    public void setPersId(String persId) {
        this.persId = persId;
    }

    public String getPersName() {
        return persName;
    }

    public void setPersName(String persName) {
        this.persName = persName;
    }
}
