package com.nudge.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ADVANTAL on 11/24/2017.
 */

public class BudgetDetail {
    @SerializedName("budget_id")
    @Expose
    private String budgetId;
    @SerializedName("range")
    @Expose
    private String range;
    @SerializedName("currency")
    @Expose
    private String currency;

    public String getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(String budgetId) {
        this.budgetId = budgetId;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
