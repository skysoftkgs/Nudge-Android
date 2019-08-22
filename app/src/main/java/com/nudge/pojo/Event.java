package com.nudge.pojo;

/**
 * Created by ADVANTAL on 7/10/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Event {

    @SerializedName("event_date_id")
    @Expose
    private String eventDateId;
    @SerializedName("custom_name")
    @Expose
    private String customName;
    @SerializedName("event_type")
    @Expose
    private String eventType;
    @SerializedName("event_date")
    @Expose
    private String eventDate;
    @SerializedName("notify_days")
    @Expose
    private String notifyDays;
    @SerializedName("repeat")
    @Expose
    private String repeat;
    @SerializedName("events")
    @Expose
    private String events;
    @SerializedName("notify")
    @Expose
    private String notify;
    @SerializedName("budget")
    @Expose
    private String budget;

    public String getEventDateId() {
        return eventDateId;
    }

    public void setEventDateId(String eventDateId) {
        this.eventDateId = eventDateId;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
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

    public String getNotifyDays() {
        return notifyDays;
    }

    public void setNotifyDays(String notifyDays) {
        this.notifyDays = notifyDays;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
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

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }
}
