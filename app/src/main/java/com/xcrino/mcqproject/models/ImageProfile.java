package com.xcrino.mcqproject.models;

public class ImageProfile {
    private String profile;
    private String userId;

    public ImageProfile(String userId, String profile) {
        this.userId = userId;
        this.profile = profile;

    }

    public String getUserId() {
        return userId;
    }

    public String getProfile() {
        return profile;
    }

}
