package com.xcrino.mcqproject.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaveAccountList {
    @SerializedName("sad_id")
    @Expose
    private String sadId;
    @SerializedName("sad_user_id")
    @Expose
    private String sadUserId;
    @SerializedName("sad_account_no")
    @Expose
    private String sadAccountNo;
    @SerializedName("sad_account_holder_name")
    @Expose
    private String sadAccountHolderName;
    @SerializedName("sad_ifsc")
    @Expose
    private String sadIfsc;
    @SerializedName("sad_branch")
    @Expose
    private String sadBranch;
    @SerializedName("sad_date_ceated")
    @Expose
    private String sadDateCeated;

    public String getSadId() {
        return sadId;
    }

    public void setSadId(String sadId) {
        this.sadId = sadId;
    }

    public String getSadUserId() {
        return sadUserId;
    }

    public void setSadUserId(String sadUserId) {
        this.sadUserId = sadUserId;
    }

    public String getSadAccountNo() {
        return sadAccountNo;
    }

    public void setSadAccountNo(String sadAccountNo) {
        this.sadAccountNo = sadAccountNo;
    }

    public String getSadAccountHolderName() {
        return sadAccountHolderName;
    }

    public void setSadAccountHolderName(String sadAccountHolderName) {
        this.sadAccountHolderName = sadAccountHolderName;
    }

    public String getSadIfsc() {
        return sadIfsc;
    }

    public void setSadIfsc(String sadIfsc) {
        this.sadIfsc = sadIfsc;
    }

    public String getSadBranch() {
        return sadBranch;
    }

    public void setSadBranch(String sadBranch) {
        this.sadBranch = sadBranch;
    }

    public String getSadDateCeated() {
        return sadDateCeated;
    }

    public void setSadDateCeated(String sadDateCeated) {
        this.sadDateCeated = sadDateCeated;
    }
}
