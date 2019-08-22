package com.nudge.model;

/**
 * Created by ADVANTAL on 7/7/2017.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nudge.pojo.AgeDetail;
import com.nudge.pojo.BudgetDetail;
import com.nudge.pojo.EventDetail;
import com.nudge.pojo.ProductDetail;


public class GetConfigResponse {

    @SerializedName("category_details")
    @Expose
    private List<CategoryDetail> categoryDetails = null;
    @SerializedName("event_details")
    @Expose
    private List<EventDetail> eventDetails = null;
    @SerializedName("product_details")
    @Expose
    private List<ProductDetail> productDetails = null;
    @SerializedName("budget_details")
    @Expose
    private List<BudgetDetail> budgetDetails = null;
    @SerializedName("age_details")
    @Expose
    private List<AgeDetail> ageDetails = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("relation_details")
    @Expose
    private List<RelationDetail> relationDetails = null;

    public List<CategoryDetail> getCategoryDetails() {
        return categoryDetails;
    }

    public void setCategoryDetails(List<CategoryDetail> categoryDetails) {
        this.categoryDetails = categoryDetails;
    }

    public List<EventDetail> getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(List<EventDetail> eventDetails) {
        this.eventDetails = eventDetails;
    }

    public List<ProductDetail> getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(List<ProductDetail> productDetails) {
        this.productDetails = productDetails;
    }

    public List<BudgetDetail> getBudgetDetails() {
        return budgetDetails;
    }

    public void setBudgetDetails(List<BudgetDetail> budgetDetails) {
        this.budgetDetails = budgetDetails;
    }

    public List<AgeDetail> getAgeDetails() {
        return ageDetails;
    }

    public void setAgeDetails(List<AgeDetail> ageDetails) {
        this.ageDetails = ageDetails;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<RelationDetail> getRelationDetails() {
        return relationDetails;
    }

    public void setRelationDetails(List<RelationDetail> relationDetails) {
        this.relationDetails = relationDetails;
    }

}
