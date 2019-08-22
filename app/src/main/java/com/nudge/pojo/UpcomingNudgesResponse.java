package com.nudge.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ADVANTAL on 7/21/2017.
 */
public class UpcomingNudgesResponse {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("nudges_details")
    @Expose
    private List<NudgesDetail> nudgesDetails = null;
    @SerializedName("status")
    @Expose
    private String status;

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
