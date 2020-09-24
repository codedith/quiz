package com.xcrino.mcqproject.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserTransaction {
    @SerializedName("rt_id")
    @Expose
    private String rtId;
    @SerializedName("rt_user_id")
    @Expose
    private String rtUserId;
    @SerializedName("rt_account_no")
    @Expose
    private String rtAccountNo;
    @SerializedName("rt_account_holder_name")
    @Expose
    private String rtAccountHolderName;
    @SerializedName("rt_ifsc")
    @Expose
    private String rtIfsc;
    @SerializedName("rt_branch")
    @Expose
    private String rtBranch;
    @SerializedName("rt_amt")
    @Expose
    private String rtAmt;
    @SerializedName("rt_date_created")
    @Expose
    private String rtDateCreated;
    @SerializedName("rt_status")
    @Expose
    private String rtStatus;
    @SerializedName("rt_pay_date")
    @Expose
    private Object rtPayDate;

    public String getRtId() {
        return rtId;
    }

    public void setRtId(String rtId) {
        this.rtId = rtId;
    }

    public String getRtUserId() {
        return rtUserId;
    }

    public void setRtUserId(String rtUserId) {
        this.rtUserId = rtUserId;
    }

    public String getRtAccountNo() {
        return rtAccountNo;
    }

    public void setRtAccountNo(String rtAccountNo) {
        this.rtAccountNo = rtAccountNo;
    }

    public String getRtAccountHolderName() {
        return rtAccountHolderName;
    }

    public void setRtAccountHolderName(String rtAccountHolderName) {
        this.rtAccountHolderName = rtAccountHolderName;
    }

    public String getRtIfsc() {
        return rtIfsc;
    }

    public void setRtIfsc(String rtIfsc) {
        this.rtIfsc = rtIfsc;
    }

    public String getRtBranch() {
        return rtBranch;
    }

    public void setRtBranch(String rtBranch) {
        this.rtBranch = rtBranch;
    }

    public String getRtAmt() {
        return rtAmt;
    }

    public void setRtAmt(String rtAmt) {
        this.rtAmt = rtAmt;
    }

    public String getRtDateCreated() {
        return rtDateCreated;
    }

    public void setRtDateCreated(String rtDateCreated) {
        this.rtDateCreated = rtDateCreated;
    }

    public String getRtStatus() {
        return rtStatus;
    }

    public void setRtStatus(String rtStatus) {
        this.rtStatus = rtStatus;
    }

    public Object getRtPayDate() {
        return rtPayDate;
    }

    public void setRtPayDate(Object rtPayDate) {
        this.rtPayDate = rtPayDate;
    }
}
