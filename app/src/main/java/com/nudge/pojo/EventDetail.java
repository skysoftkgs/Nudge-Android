package com.nudge.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ADVANTAL on 7/20/2017.
 */
public class EventDetail {

    @SerializedName("events")
    @Expose
    private String eventId;
    @SerializedName("event_type")
    @Expose
    private String eventType;
    @SerializedName("event_date")
    @Expose
    private String eventDate;
    @SerializedName("event_name")
    @Expose
    private String eventName;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}
