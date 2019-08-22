package com.nudge.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UpdateRegistration implements Serializable
{

@SerializedName("user_details")
@Expose
private UserDetail_UpdateRegistration userDetails;
@SerializedName("message")
@Expose
private String message;
@SerializedName("status")
@Expose
private String status;
private final static long serialVersionUID = 2364254201979487854L;

public UserDetail_UpdateRegistration getUserDetails() {
return userDetails;
}

public void setUserDetails(UserDetail_UpdateRegistration userDetails) {
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