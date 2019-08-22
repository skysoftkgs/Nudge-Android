package com.nudge.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ADVANTAL on 9/20/2017.
 */
public class EventNew {
    @SerializedName("custom_name")
    @Expose
    private String customName;
    @SerializedName("event_date")
    @Expose
    private String eventDate;
    @SerializedName("events")
    @Expose
    private String events;
    @SerializedName("notify")
    @Expose
    private String notify;

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEvents() {
        return events;
    }

    public void setEvents(String events) {
        this.events = events;
    }

    public String getNotify() {
        return notify;
    }

    public void setNotify(String notify) {
        this.notify = notify;
    }

}
