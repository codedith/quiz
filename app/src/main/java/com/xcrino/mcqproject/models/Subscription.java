package com.xcrino.mcqproject.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Subscription {
    @SerializedName("result")
    @Expose
    private Boolean result;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<SubcriptionPlan> data = null;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<SubcriptionPlan> getData() {
        return data;
    }

    public void setData(List<SubcriptionPlan> data) {
        this.data = data;
    }
}
