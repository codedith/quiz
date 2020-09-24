package com.xcrino.mcqproject.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserProfile {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<UserData> data = null;
    @SerializedName("wallet")
    @Expose
    private List<Wallet> wallet = null;
    @SerializedName("total")
    @Expose
    private Double total;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<UserData> getData() {
        return data;
    }

    public void setData(List<UserData> data) {
        this.data = data;
    }

    public List<Wallet> getWallet() {
        return wallet;
    }

    public void setWallet(List<Wallet> wallet) {
        this.wallet = wallet;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
