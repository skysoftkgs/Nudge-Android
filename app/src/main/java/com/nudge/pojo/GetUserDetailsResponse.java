package com.nudge.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ADVANTAL on 8/24/2017.
 */

public class GetUserDetailsResponse {

    @SerializedName("user_details")
    @Expose
    private GetUserDetails userDetails;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public GetUserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(GetUserDetails userDetails) {
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
