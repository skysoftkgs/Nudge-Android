package com.nudge.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ADVANTAL on 11/20/2017.
 */
public class FavoriteDetail {

    @SerializedName("prod_image")
    @Expose
    private String prodImage;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("prod_id")
    @Expose
    private String prodId;
    @SerializedName("prod_name")
    @Expose
    private String prodName;

    public String getProdImage() {
        return prodImage;
    }

    public void setProdImage(String prodImage) {
        this.prodImage = prodImage;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }
}
