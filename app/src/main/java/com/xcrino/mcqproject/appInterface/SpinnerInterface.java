package com.xcrino.mcqproject.appInterface;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SpinnerInterface {
    String JSONURL = "http://theacademiz.com/learn&earn/";

    @GET("api_quiz/get_subject")
    Call<String> getSubjectList();
}
