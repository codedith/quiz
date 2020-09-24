package com.xcrino.mcqproject.appInterface;

import com.xcrino.mcqproject.models.AccountTransaction;
import com.xcrino.mcqproject.models.AddNewQuestion;
import com.xcrino.mcqproject.models.AmountWithdral;
import com.xcrino.mcqproject.models.ChangePassowrd;
import com.xcrino.mcqproject.models.ForgotPassword;
import com.xcrino.mcqproject.models.LogInSignUpBase;
import com.xcrino.mcqproject.models.PaymentModel;
import com.xcrino.mcqproject.models.QuizAnswerModel;
import com.xcrino.mcqproject.models.QuizQuestionBase;
import com.xcrino.mcqproject.models.SubjectList;
import com.xcrino.mcqproject.models.Subscription;
import com.xcrino.mcqproject.models.TransactionList;
import com.xcrino.mcqproject.models.UploadQuestion;
import com.xcrino.mcqproject.models.UserProfile;
import com.xcrino.mcqproject.models.UserSubscription;
import com.xcrino.mcqproject.models.WalletData;
import com.xcrino.mcqproject.models.WatchVideo;

import javax.security.auth.Subject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIInterface {

    @FormUrlEncoded
    @POST("api_user/login")
    Call<LogInSignUpBase> loginUser(@Field("identity") String email, @Field("password") String password, @Field("token_id")String token_id);

    @FormUrlEncoded
    @POST("api_user/create_user")
    Call<LogInSignUpBase> createUser(@Field("first_name") String firstName, @Field("phone") String phone,
                                     @Field("country") String country, @Field("email") String email,
                                  @Field("password") String password, @Field("password_confirm") String passwordConfirm,
                                     @Field("refferal_check")Boolean refferal_check, @Field("refferal_code")String refferal_code,
                                     @Field("token_id")String token_id);

    @FormUrlEncoded
    @POST("api_quiz/get_question_for_test")
    Call<QuizQuestionBase> getQuestionListForTest(@Field("user_id") String userId, @Field("page") String page, @Field("category_id") String categoryId);

    @FormUrlEncoded
    @POST("api_quiz/attend_questions")
    Call<QuizAnswerModel> postQuizAnswers(@Field("userid") String userId, @Field("question_id") String questionId, @Field("choosed") String choosedQuestion);

    @GET("api_quiz/getSubscriptions")
    Call<Subscription> getSubscriptionValue();

    @FormUrlEncoded
    @POST("api_quiz/get_user_subscription")
    Call<UserSubscription> get_user_subscription(@Field("usersid") String user_id);

    @FormUrlEncoded
    @POST("api_quiz/subscribeNow")
    Call<PaymentModel> postPayment(@Field("user_id") String user_id,
                                   @Field("package_id")String package_id,
                                   @Field("payment_id")String payment_id,
                                   @Field("amount")String amount,
                                   @Field("currency_code")String currency_code);

    @FormUrlEncoded
    @POST("api_quiz/updateSubscription")
    Call<UserSubscription> updateSubscription(@Field("user_id") String user_id,
                                              @Field("package_id")String package_id,
                                              @Field("payment_id")String payment_id,
                                              @Field("amount")String amount,
                                              @Field("currency_code")String currency_code);

    @FormUrlEncoded
    @POST("api_quiz/getWalletData")
    Call<WalletData> getWalletDataDetails(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("api_quiz/addNewQuestion")
    Call<AddNewQuestion> addNewQuestionByUser(@Field("usersid") String userId, @Field("question") String questions, @Field("option1") String optionFirst,
                                              @Field("option2") String optionSecond, @Field("option3") String optionThird, @Field("option4") String optionFourth,
                                              @Field("answer") String answer, @Field("description") String descriptions, @Field("subject") String subject);


    @FormUrlEncoded
    @POST("api_quiz/watched_video")
    Call<WatchVideo> postWatchedVideo(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("api_user/change_password")
    Call<ChangePassowrd> changePassword(@Field("user_id") String userId, @Field("old") String oldPassword, @Field("new") String newPassword, @Field("new_confirm") String newConfirmPassword);

    @FormUrlEncoded
    @POST("api_quiz/get_user_profile")
    Call<UserProfile> getDetailsUseProfile(@Field("user_id") String userId);

    @Multipart
    @POST("api_quiz/change_profile")
    Call<ResponseBody> changeUserProfile(@Part MultipartBody.Part imageFile, @Part("user_id") RequestBody userId);

    @FormUrlEncoded
    @POST("api_quiz/getUploadedQuestions")
    Call<UploadQuestion> getUploadedQuestions(@Field("user_id") String userId);

    @GET("api_quiz/get_subject")
    Call<SubjectList> getSubjectList();

    @FormUrlEncoded
    @POST("api_user/forgot_password")
    Call<ForgotPassword> forgotPassword(@Field("email") String email);

    @FormUrlEncoded
    @POST("api_quiz/get_question_for_test_by_cate")
    Call<Response> getQuestionForTestbyCategory(@Field("user_id") String userId, @Field("page") String page, @Field("category_id") String categoryId);

    @FormUrlEncoded
    @POST("api_user/create_social_user")
    Call<LogInSignUpBase> socialLogin(@Field("user_name") String user_name,
                                      @Field("token_id") String token_id,
                                      @Field("user_unique_id") String user_unique_id,
                                      @Field("user_login_type") String user_login_type,
                                      @Field("profile_image") String profile_image,
                                      @Field("email") String email);

    @FormUrlEncoded
    @POST("api_quiz/save_account_details")
    Call<AmountWithdral> getSaveAccountWithdrawal(@Field("user_id") String userId, @Field("account_no") String accountNo,
                                                  @Field("account_holdername") String accountHolderName,
                                                  @Field("ifsc") String ifsCode, @Field("branch") String branchAddress,
                                                  @Field("amt") String amount);

    @FormUrlEncoded
    @POST("api_quiz/get_user_transactions")
    Call<AccountTransaction> getUserTransaction(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("api_quiz/get_saved_account_list")
    Call<TransactionList> getSaveAccountList(@Field("user_id") String userId);

}
