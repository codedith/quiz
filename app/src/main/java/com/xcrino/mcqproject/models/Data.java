package com.xcrino.mcqproject.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("rightPoint")
    @Expose
    private Integer rightPoint;
    @SerializedName("wrongPoint")
    @Expose
    private Integer wrongPoint;
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
    private Integer total;

    public Integer getRightPoint() {
        return rightPoint;
    }

    public void setRightPoint(Integer rightPoint) {
        this.rightPoint = rightPoint;
    }

    public Integer getWrongPoint() {
        return wrongPoint;
    }

    public void setWrongPoint(Integer wrongPoint) {
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

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
