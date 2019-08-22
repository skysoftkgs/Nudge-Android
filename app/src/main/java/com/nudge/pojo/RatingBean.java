package com.nudge.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RatingBean implements Serializable
{

@SerializedName("message")
@Expose
private String message;
@SerializedName("status")
@Expose
private String status;
private final static long serialVersionUID = -250588692034655051L;

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