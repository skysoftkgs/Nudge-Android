package com.nudge.pojo;

/**
 * Created by ADVANTAL on 7/20/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UpdateProfileManualResponse {

    @SerializedName("user_details")
    @Expose
    private UserDetailsNew userDetails;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("nudges_details")
    @Expose
    private List<NudgesDetail> nudgesDetails = null;
    @SerializedName("status")
    @Expose
    private String status;

    public UserDetailsNew getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetailsNew userDetails) {
        this.userDetails = userDetails;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<NudgesDetail> getNudgesDetails() {
        return nudgesDetails;
    }

    public void setNudgesDetails(List<NudgesDetail> nudgesDetails) {
        this.nudgesDetails = nudgesDetails;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
