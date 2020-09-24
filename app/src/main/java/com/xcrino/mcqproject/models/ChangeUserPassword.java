package com.xcrino.mcqproject.models;

public class ChangeUserPassword {
    private String userId;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

    public ChangeUserPassword(String userId, String oldPassword, String newPassword, String confirmPassword) {
        this.userId = userId;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public String getUserId() {
        return userId;
    }
}
