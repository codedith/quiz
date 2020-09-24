package com.xcrino.mcqproject.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("package_id")
    @Expose
    private String packageId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("subscribe_date")
    @Expose
    private String subscribeDate;
    @SerializedName("payment_id")
    @Expose
    private String paymentId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("amount_inr")
    @Expose
    private String amountInr;
    @SerializedName("amount_usd")
    @Expose
    private String amountUsd;
    @SerializedName("validity")
    @Expose
    private String validity;
    @SerializedName("user_response")
    @Expose
    private String userResponse;
    @SerializedName("earning")
    @Expose
    private String earning;
    @SerializedName("add_date")
    @Expose
    private Object addDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSubscribeDate() {
        return subscribeDate;
    }

    public void setSubscribeDate(String subscribeDate) {
        this.subscribeDate = subscribeDate;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAmountInr() {
        return amountInr;
    }

    public void setAmountInr(String amountInr) {
        this.amountInr = amountInr;
    }

    public String getAmountUsd() {
        return amountUsd;
    }

    public void setAmountUsd(String amountUsd) {
        this.amountUsd = amountUsd;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getUserResponse() {
        return userResponse;
    }

    public void setUserResponse(String userResponse) {
        this.userResponse = userResponse;
    }

    public String getEarning() {
        return earning;
    }

    public void setEarning(String earning) {
        this.earning = earning;
    }

    public Object getAddDate() {
        return addDate;
    }

    public void setAddDate(Object addDate) {
        this.addDate = addDate;
    }
}
