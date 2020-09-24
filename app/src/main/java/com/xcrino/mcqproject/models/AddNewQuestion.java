package com.xcrino.mcqproject.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddNewQuestion {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("NewQuestions")
    @Expose
    private NewQuestions newQuestions;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public NewQuestions getNewQuestions() {
        return newQuestions;
    }

    public void NewQuestions(NewQuestions newQuestions) {
        this.newQuestions = newQuestions;
    }
}
