package com.nudge.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ADVANTAL on 3/22/2018.
 */
public class AgeDetail {
    @SerializedName("age_name")
    @Expose
    private String ageName;
    @SerializedName("age_range")
    @Expose
    private String ageRange;
    @SerializedName("age_id")
    @Expose
    private String ageId;

    public String getAgeName() {
        return ageName;
    }

    public void setAgeName(String ageName) {
        this.ageName = ageName;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    public String getAgeId() {
        return ageId;
    }

    public void setAgeId(String ageId) {
        this.ageId = ageId;
    }

}
