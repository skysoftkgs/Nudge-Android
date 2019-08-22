package com.nudge.pojo;

/**
 * Created by ADVANTAL on 7/10/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetProfileResponse {

    @SerializedName("user_details")
    @Expose
    private List<UserGetProfileDetails> userDetails = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public List<UserGetProfileDetails> getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(List<UserGetProfileDetails> userDetails) {
        this.userDetails = userDetails;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
