package com.xcrino.mcqproject.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class UploadData implements Comparable<UploadData> {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("option1")
    @Expose
    private String option1;
    @SerializedName("option2")
    @Expose
    private String option2;
    @SerializedName("option3")
    @Expose
    private String option3;
    @SerializedName("option4")
    @Expose
    private String option4;
    @SerializedName("answers")
    @Expose
    private String answers;
    @SerializedName("descript")
    @Expose
    private String descript;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("add_date")
    @Expose
    private String addDate;
    @SerializedName("userid")
    @Expose
    private String userid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddDate() {
        return addDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Override
    public int compareTo(UploadData uploadData) {
        return new Date(this.getAddDate()).compareTo(new Date(uploadData.addDate));
    }
}
