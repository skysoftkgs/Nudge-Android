package com.nudge.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ADVANTAL on 9/20/2017.
 */
public class UserDetailsManualNew {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("event")
    @Expose
    private List<EventNew> event = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<EventNew> getEvent() {
        return event;
    }

    public void setEvent(List<EventNew> event) {
        this.event = event;
    }
}
