package com.xcrino.mcqproject.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wallet {
    @SerializedName("rightPoint")
    @Expose
    private Integer rightPoint;
    @SerializedName("wrongPoint")
    @Expose
    private Double wrongPoint;
    @SerializedName("uploadedPoint")
    @Expose
    private Integer uploadedPoint;
    @SerializedName("attendedPoint")
    @Expose
    private Integer attendedPoint;
    @SerializedName("watchedPoint")
    @Expose
    private Integer watchedPoint;
    @SerializedName("reffer_point")
    @Expose
    private Integer refferPoint;
    @SerializedName("total")
    @Expose
    private Double total;

    public Integer getRightPoint() {
        return rightPoint;
    }

    public void setRightPoint(Integer rightPoint) {
        this.rightPoint = rightPoint;
    }

    public Double getWrongPoint() {
        return wrongPoint;
    }

    public void setWrongPoint(Double wrongPoint) {
        this.wrongPoint = wrongPoint;
    }

    public Integer getUploadedPoint() {
        return uploadedPoint;
    }

    public void setUploadedPoint(Integer uploadedPoint) {
        this.uploadedPoint = uploadedPoint;
    }

    public Integer getAttendedPoint() {
        return attendedPoint;
    }

    public void setAttendedPoint(Integer attendedPoint) {
        this.attendedPoint = attendedPoint;
    }

    public Integer getWatchedPoint() {
        return watchedPoint;
    }

    public void setWatchedPoint(Integer watchedPoint) {
        this.watchedPoint = watchedPoint;
    }

    public Integer getRefferPoint() {
        return refferPoint;
    }

    public void setRefferPoint(Integer refferPoint) {
        this.refferPoint = refferPoint;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

}
