package com.nudge.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ADVANTAL on 11/20/2017.
 */
public class GetFavouritesResponse {
    @SerializedName("favorite_details")
    @Expose
    private List<FavoriteDetail> favoriteDetails = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public List<FavoriteDetail> getFavoriteDetails() {
        return favoriteDetails;
    }

    public void setFavoriteDetails(List<FavoriteDetail> favoriteDetails) {
        this.favoriteDetails = favoriteDetails;
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
