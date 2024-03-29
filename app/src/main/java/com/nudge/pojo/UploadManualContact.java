package com.nudge.pojo;

/**
 * Created by ADVANTAL on 6/28/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadManualContact {

    @SerializedName("user_details")
    @Expose
    private UserDetailsManualNew userDetails;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public UserDetailsManualNew getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetailsManualNew userDetails) {
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
