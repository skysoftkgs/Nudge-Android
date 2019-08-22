package com.nudge.model;

import java.io.Serializable;

/**
 * Created by harshita on 6/19/2017.
 */
public class ForgotPassword implements Serializable {

    private String message;
    private String status;

    public ForgotPassword() {
    }

    public ForgotPassword(String message, String status) {
        this.message = message;
        this.status = status;
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
