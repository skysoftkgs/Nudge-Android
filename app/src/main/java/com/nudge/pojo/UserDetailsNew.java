package com.nudge.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ADVANTAL on 9/20/2017.
 */
public class UserDetailsNew {
    @SerializedName("event")
    @Expose
    private List<EventNew> event = null;

    public List<EventNew> getEvent() {
        return event;
    }

    public void setEvent(List<EventNew> event) {
        this.event = event;
    }
}
