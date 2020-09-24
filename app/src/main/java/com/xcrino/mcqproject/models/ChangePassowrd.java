package com.xcrino.mcqproject.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangePassowrd {
    @SerializedName("result")
    @Expose
    private Boolean result;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("ChangeUserPassword")
    @Expose
    private ChangeUserPassword changeUserPassword;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ChangeUserPassword getChangeUserPassword() {
        return changeUserPassword;
    }
}
