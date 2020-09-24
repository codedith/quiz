package com.xcrino.mcqproject.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String APP_SHARED_PREFS;

    public AppPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        APP_SHARED_PREFS = "qwizapp.com";
    }

    public void ClearPreferences() {
        editor.clear().commit();
    }

    public void savePhone(String phone) {
        editor.putString("phone", phone);
        editor.apply();
    }

    public String getPhone() {
        return sharedPreferences.getString("phone", "");
    }

    public void saveEmail(String email) {
        editor.putString("email", email);
        editor.apply();

    }

    public void saveUserId(String user_id) {
        editor.putString("user_id", user_id);
        editor.apply();
    }

    public String getEmail() {
        return sharedPreferences.getString("email", "");
    }


    public String getSubscriptionId() {
        return sharedPreferences.getString("subscription_id", "");
    }

    public void saveCountry(String country) {
        editor.putString("country", country);
        editor.apply();
    }

    public void saveSubStatus(Boolean subs_status) {
        editor.putBoolean("subs_status", subs_status);
        editor.apply();
    }

    public Boolean getSubStatus() {
        return sharedPreferences.getBoolean("subs_status", false);
    }

    public void saveCurrencyCode(String currency_code) {
        editor.putString("currency_code", currency_code);
        editor.apply();
    }

    public void saveAmount(String amount) {
        editor.putString("amount", amount);
        editor.apply();
    }

    public String getAmount() {
        return sharedPreferences.getString("amount", "");
    }

    public void saveSubscriptionId(String subscription_id) {
        editor.putString("subscription_id", subscription_id);
        editor.apply();
    }

    public String getCurrencyCode() {
        return sharedPreferences.getString("currency_code", "");
    }

    public String getCountry() {
        return sharedPreferences.getString("country", "India");
    }

    public void setIsFirstTimeLaunch(Boolean isFirstTimeLaunch) {
        editor.putBoolean("isFirstTimeLaunch", isFirstTimeLaunch);
        editor.commit();
    }

    public Boolean getIsFirstTimeLaunch() {
        return sharedPreferences.getBoolean("isFirstTimeLaunch", true);
    }

    public void setAccessTokenString(String mAccessTokenString) {
        editor.putString("mAccessTokenString", mAccessTokenString);
        editor.commit();
    }

    public String getAccessTokenString() {
        return sharedPreferences.getString("mAccessTokenString", "");
    }

    public void setUserId(String userId) {
        editor.putString("userId", userId);
        editor.commit();
    }

    public String getUserId() {
        return sharedPreferences.getString("userId", "");
    }

    public void setReferCode(String refer_code){
        editor.putString("refer_code", refer_code);
        editor.commit();
    }

    public String getReferCode(){
        return sharedPreferences.getString("refer_code", "");
    }

    public void setPreviousTimerLengthSeconds(Long PreviousTimerLengthSeconds) {
        editor.putLong("PreviousTimerLengthSeconds", PreviousTimerLengthSeconds);
        editor.apply();
    }

    public Long getPreviousTimerLengthSeconds() {
        return sharedPreferences.getLong("PreviousTimerLengthSeconds", 0);
    }

    public void setTimerState(String TimerState) {
        editor.putString("TimerState", TimerState);
        editor.apply();
    }

    public String getTimerState() {
        return sharedPreferences.getString("TimerState", "Stopped");
    }

    public void setSecondsRemaining(Long SecondsRemaining) {
        editor.putLong("SecondsRemaining", SecondsRemaining);
        editor.apply();
    }

    public Long getSecondsRemaining() {
        return sharedPreferences.getLong("SecondsRemaining", 0);
    }

    public void setCategoryId(String cateId){
        editor.putString("categoryId", cateId);
        editor.apply();
    }

    public String getCategoryId(){
        return sharedPreferences.getString("categoryId", "0");
    }

    public void setAvailableBalance(Float availableBalance){
        editor.putFloat("availableBalance", availableBalance);
        editor.apply();
    }

    public Float getAvailableBalance(){
        return sharedPreferences.getFloat("availableBalance", 0.0F);
    }

    public void setTransactionStatus(String transactionStatus){
        editor.putString("transactionStatus", transactionStatus);
        editor.apply();
    }

    public String getTransactionStatus(){
        return sharedPreferences.getString("transactionStatus", "0");
    }
}