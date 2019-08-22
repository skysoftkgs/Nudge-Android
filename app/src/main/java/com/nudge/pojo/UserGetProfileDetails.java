package com.nudge.pojo;

/**
 * Created by ADVANTAL on 7/10/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserGetProfileDetails {

    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("custom_name")
    @Expose
    private String customName;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("fb_id")
    @Expose
    private String fbId;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("contact_no")
    @Expose
    private String contactNo;
    @SerializedName("event_date")
    @Expose
    private String eventDate;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("personna_details")
    @Expose
    private List<PersonnaDetail> personnaDetails = null;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("relationship")
    @Expose
    private String relationship;
    @SerializedName("event")
    @Expose
    private List<Event> event = null;
    @SerializedName("events")
    @Expose
    private String events;
    @SerializedName("budget")
    @Expose
    private String budget;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PersonnaDetail> getPersonnaDetails() {
        return personnaDetails;
    }

    public void setPersonnaDetails(List<PersonnaDetail> personnaDetails) {
        this.personnaDetails = personnaDetails;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public List<Event> getEvent() {
        return event;
    }

    public void setEvent(List<Event> event) {
        this.event = event;
    }

    public String getEvents() {
        return events;
    }

    public void setEvents(String events) {
        this.events = events;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

}