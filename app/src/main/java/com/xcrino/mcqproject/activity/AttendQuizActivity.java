package com.xcrino.mcqproject.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.xcrino.mcqproject.R;
import com.xcrino.mcqproject.appInterface.APIInterface;
import com.xcrino.mcqproject.models.Data;
import com.xcrino.mcqproject.models.QuizAnswerBase;
import com.xcrino.mcqproject.models.QuizAnswerModel;
import com.xcrino.mcqproject.models.QuizQuestionBase;
import com.xcrino.mcqproject.utils.APIClient;
import com.xcrino.mcqproject.utils.AppPreferences;
import com.xcrino.mcqproject.utils.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendQuizActivity extends AppCompatActivity {

    private TextView view_question, description, tv_timer;
    private RadioButton radio_button1, radio_button2, radio_button3, radio_button4;
    private Button button_skip, button_next;
    private String mQuestion, optionA, optionB, optionC, optionD, nextPage = "0", mdescription;

    private ImageView quiz_back_key;

    private String userId, mQuestioId, mChooseOptions;

    private ArrayList<QuizAnswerBase> data = new ArrayList<>();
    private QuizAnswerBase answerQuestions;
    private QuizQuestionBase questionAnswers;
    private Data data1;

    private int pageNumber = 0;
    List<Integer> list = new ArrayList<Integer>();
    private String mAnswer;
    private int mScore = 0;
    private int mQuestionNumber = 0;

    private ColorStateList textColorDefaultRb;
    private ColorStateList textColorDefaultCd;
    private Boolean finish;
    Context mContext;
    private static final String AD_UNIT_ID = "ca-app-pub-3548280363774169~5766912321";
    //    private RewardedVideoAd mRewardedVideoAd;
//    private AdView mAdView;
    private static final long START_TIME_IN_MILLIS = 30000;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private AppPreferences appPreferences;
    private int progress = 0;

    private ProgressBar progress_timer;
    Long secondsLeft = 0L;
    String timerState = "Stopped";
    Long timerLengthSeconds = 0L;

    TextView mToolbar_title;
    MediaPlayer right_answer_sound, wrong_answer_sound;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend_quiz);

//        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
//            UserLogin user = SharedPrefManager.getInstance(this).getUser();
//            userId = user.getUserId();
//            Bundle b = getIntent().getExtras();
//            String userName;
//            userName = userId;
//            if (b != null) {
//                userName = b.getString("userId");
//            }
//        }
        mContext = this;
        appPreferences = new AppPreferences(mContext);

        MobileAds.initialize(mContext, AD_UNIT_ID);
//        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(mContext);
//        mRewardedVideoAd.setRewardedVideoAdListener(this);

        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");   //Sample ID
//        mInterstitialAd.setAdUnitId("ca-app-pub-3548280363774169/8898591678");
        mInterstitialAd.setAdUnitId("ca-app-pub-3548280363774169/8037355006");

        loadRewardedVideoAd();

//        AdView adView = new AdView(this);
//        adView.setAdSize(AdSize.BANNER);
//        adView.setAdUnitId("ca-app-pub-3548280363774169/8048712302");

//        mAdView = findViewById(R.id.adView);
        mToolbar_title = findViewById(R.id.toolbar_title);
        mToolbar_title.setText("QwizApp");

//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
//
//        mAdView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                // Code to be executed when an ad finishes loading.
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                // Code to be executed when an ad request fails.
//            }
//
//            @Override
//            public void onAdOpened() {
//                // Code to be executed when an ad opens an overlay that
//                // covers the screen.
//            }
//
//            @Override
//            public void onAdClicked() {
//                // Code to be executed when the user clicks on an ad.
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                // Code to be executed when the user has left the app.
//            }
//
//            @Override
//            public void onAdClosed() {
//                // Code to be executed when the user is about to return
//                // to the app after tapping on an ad.
//            }
//        });

        list.add(1);

        getUIInit();
//        changeNotificationcolor();

//        resetTimer();
        if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
            attendQuizProcess();
        } else {
            Toast.makeText(this, "Check Internet Connection !", Toast.LENGTH_SHORT).show();
        }

        quiz_back_key = (ImageView) findViewById(R.id.back_arrow);

        quiz_back_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();

            }

        });

        radioHandling();

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountDownTimer.cancel();
                onTimerFinished();
//                pauseTimer();
//                resetTimer();
                pageNumber++;
                nextPage = Integer.toString(pageNumber);
                addMobDisplay();
                if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
                    attendQuizProcess();
                } else {
                    Toast.makeText(getApplicationContext(), "Check Internet Connection !", Toast.LENGTH_SHORT).show();
                }
                radio_button1.setClickable(true);
                radio_button2.setClickable(true);
                radio_button3.setClickable(true);
                radio_button4.setClickable(true);
                description.setText("");
                radioHandling();
            }
        });

        button_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountDownTimer.cancel();
                onTimerFinished();
//                pauseTimer();
//                resetTimer();
                pageNumber++;
                nextPage = Integer.toString(pageNumber);
                addMobDisplay();
                if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
                    attendQuizProcess();
                } else {
                    Toast.makeText(getApplicationContext(), "Check Internet Connection !", Toast.LENGTH_SHORT).show();
                }
                radio_button1.setClickable(true);
                radio_button2.setClickable(true);
                radio_button3.setClickable(true);
                radio_button4.setClickable(true);
                description.setText("");
                radioHandling();
            }
        });


        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                loadRewardedVideoAd();
//                startTimer();
            }
        });

//        updateCountDownText();
//        addMobDisplay();
    }

    @Override
    protected void onResume() {
        super.onResume();

        initTimer();

        //TODO: Remove background timer, hide notifications
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (timerState.equals("Running")) {
            mCountDownTimer.cancel();

            //TODO: start background timer, show notifications
        } else if (timerState.equals("Paused")) {
            //TODO: show notification
        }

        appPreferences.setPreviousTimerLengthSeconds(timerLengthSeconds);
        appPreferences.setSecondsRemaining(secondsLeft);
        appPreferences.setTimerState(timerState);
    }

    private void initTimer() {
        timerState = appPreferences.getTimerState();

        if (timerState.equals("Stopped")) {
            setNewTimerLength();
        } else {
            setPreviousTimerLength();
        }

        if (timerState.equals("Running") || timerState.equals("Paused")) {
            secondsLeft = appPreferences.getSecondsRemaining();
        } else {
            secondsLeft = timerLengthSeconds;
        }

        //TODO: change secondsLeft According to where the background timer stopped

        //resume where we left off
        if (timerState.equals("Running")) {
            startTimer();
        }

        updateCountDownUI();
    }

    private void loadRewardedVideoAd() {
//        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
//                new AdRequest.Builder().build());             // Sample Id
//        mRewardedVideoAd.loadAd("ca-app-pub-3548280363774169/1444523939",
//                new AdRequest.Builder().build());

        mInterstitialAd.loadAd(new AdRequest.Builder().build());

    }

    private void updateCountDownText() {
        String seconds = String.valueOf(mTimeLeftInMillis / 1000);
        if (seconds.length() == 2) {
            tv_timer.setText("00:" + mTimeLeftInMillis / 1000);
        } else {
            tv_timer.setText("00:0" + mTimeLeftInMillis / 1000);
        }
    }

    private void updateCountDownUI() {
        Long minutesUntilFinished = secondsLeft / 60;
        Long secondsInMinutesUntilFinished = secondsLeft - minutesUntilFinished * 60;
        String secondStr = secondsInMinutesUntilFinished.toString();
        tv_timer.setText(secondStr + "s");
        progress_timer.setProgress((int) (timerLengthSeconds - secondsLeft));
    }

    private void onTimerFinished() {
        timerState = "Stopped";

        setNewTimerLength();

        progress_timer.setProgress(0);

        appPreferences.setSecondsRemaining(timerLengthSeconds);
        secondsLeft = timerLengthSeconds;

        updateCountDownUI();
    }

    private void setNewTimerLength() {
        timerLengthSeconds = (1 * 20L);
        progress_timer.setMax(timerLengthSeconds.intValue());
    }

    private void setPreviousTimerLength() {
        timerLengthSeconds = appPreferences.getPreviousTimerLengthSeconds();
        progress_timer.setMax(timerLengthSeconds.intValue());
    }

    private void startTimer() {
//        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                mTimeLeftInMillis = millisUntilFinished;
//                updateCountDownText();
//            }
//
//            @Override
//            public void onFinish() {
//                pageNumber++;
//                nextPage = Integer.toString(pageNumber);
//                if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
//                    attendQuizProcess();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Check Internet Connection !", Toast.LENGTH_SHORT).show();
//                }
//                radio_button1.setClickable(true);
//                radio_button2.setClickable(true);
//                radio_button3.setClickable(true);
//                radio_button4.setClickable(true);
//                description.setText("");
//                radioHandling();
//                addMobDisplay();
//                startTimer();
//            }
//        }.start();
//
//        mTimerRunning = true;

        timerState = "Running";

        mCountDownTimer = new CountDownTimer(secondsLeft * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                secondsLeft = millisUntilFinished / 1000;
                updateCountDownUI();
            }

            @Override
            public void onFinish() {
                onTimerFinished();
                pageNumber++;
                nextPage = Integer.toString(pageNumber);
                addMobDisplay();
                if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
                    attendQuizProcess();
                } else {
                    Toast.makeText(getApplicationContext(), "Check Internet Connection !", Toast.LENGTH_SHORT).show();
                }
                radio_button1.setClickable(true);
                radio_button2.setClickable(true);
                radio_button3.setClickable(true);
                radio_button4.setClickable(true);
                description.setText("");
                radioHandling();
            }
        }.start();

//        addMobDisplay();

        // ringpreogressbar.
      /*  new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++){
                    try {
                        Thread.sleep(300);
                        handler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();*/
    }

    /*   Handler handler = new Handler(){
           @Override
           public void handleMessage(Message msg) {
               if (msg.what == 0){
                   if (progress < 100){
                       progress++;
                       progress_bar_1.setProgress(progress);
                   }
               }
           }
       };
   */
    private void pauseTimer() {
        mCountDownTimer.cancel();
//        mTimerRunning = false;
    }

    private void resetTimer() {
//        mTimeLeftInMillis = START_TIME_IN_MILLIS;
//        updateCountDownText();
        setNewTimerLength();
        startTimer();
    }

    private void addMobDisplay() {
//        if (pageNumber>6 && pageNumber % 6 == 0) {
//            if (mRewardedVideoAd.isLoaded()) {
//                mRewardedVideoAd.show();
//                mCountDownTimer.cancel();
//                onTimerFinished();
//            }
//        }
        Log.d("pageNumber", "" + pageNumber);
        if (pageNumber > 1 && pageNumber % 3 == 0) {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
                mCountDownTimer.cancel();
                onTimerFinished();
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }
        }

    }

    private void sound(boolean play) {
        right_answer_sound = MediaPlayer.create(this, R.raw.right_answer);
        wrong_answer_sound = MediaPlayer.create(this, R.raw.wrong_answer);
        if (play) {
            right_answer_sound.start();
            wrong_answer_sound.stop();
        } else {
            right_answer_sound.stop();
            wrong_answer_sound.start();
        }
    }

    private void radioHandling() {

        radio_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                selectCorrectAnswers();
                if (!mChooseOptions.equals("1")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        radio_button1.setBackground(getResources().getDrawable(R.drawable.bg_incorrect_ans));
                    }
                    sound(false);
                } else {
                    sound(true);
                }
                checkAnswer();
                description.setText(mdescription);
                postQuizAnswers("1");
            }
        });

        radio_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mChooseOptions.equals("2")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        radio_button2.setBackground(getResources().getDrawable(R.drawable.bg_incorrect_ans));
                    }
                    sound(false);
                } else {
                    sound(true);
                }
                checkAnswer();
                description.setText(mdescription);
                postQuizAnswers("2");
            }
        });

        radio_button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mChooseOptions.equals("3")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        radio_button3.setBackground(getResources().getDrawable(R.drawable.bg_incorrect_ans));
                    }
                    sound(false);
                } else {
                    sound(true);
                }
                checkAnswer();
                description.setText(mdescription);
                postQuizAnswers("3");
            }
        });

        radio_button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mChooseOptions.equals("4")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        radio_button4.setBackground(getResources().getDrawable(R.drawable.bg_incorrect_ans));
                    }
                    sound(false);
                } else {
                    sound(true);
                }
                checkAnswer();
                description.setText(mdescription);
                postQuizAnswers("4");
            }
        });
    }

    private void checkAnswer() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

            switch (mChooseOptions) {
                case "1":
                    radio_button1.setBackground(getResources().getDrawable(R.drawable.bg_correct_ans));
                    break;
                case "2":
                    radio_button2.setBackground(getResources().getDrawable(R.drawable.bg_correct_ans));
                    break;
                case "3":
                    radio_button3.setBackground(getResources().getDrawable(R.drawable.bg_correct_ans));
                    break;
                case "4":
                    radio_button4.setBackground(getResources().getDrawable(R.drawable.bg_correct_ans));
                    break;
            }
            radio_button1.setClickable(false);
            radio_button2.setClickable(false);
            radio_button3.setClickable(false);
            radio_button4.setClickable(false);
        }
    }

    private void getUIInit() {
//        progress_bar_1 = (RingProgressBar) findViewById(R.id.progress_bar_1);
        button_skip = (Button) findViewById(R.id.skip_button);
        button_next = (Button) findViewById(R.id.next_button);
        view_question = (TextView) findViewById(R.id.question_text);
        radio_button1 = (RadioButton) findViewById(R.id.option_a);
        radio_button2 = (RadioButton) findViewById(R.id.option_b);
        radio_button3 = (RadioButton) findViewById(R.id.option_c);
        radio_button4 = (RadioButton) findViewById(R.id.option_d);
        description = (TextView) findViewById(R.id.description);
        tv_timer = (TextView) findViewById(R.id.count_down_timer_tv);
        progress_timer = findViewById(R.id.progress_timer);
    }

    private void attendQuizProcess() {
        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Please wait...");
        if (!((Activity) mContext).isFinishing()) {
            progressDialog.show();
        }


        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<QuizQuestionBase> call = apiInterface.getQuestionListForTest(appPreferences.getUserId(), nextPage, appPreferences.getCategoryId());
        call.enqueue(new Callback<QuizQuestionBase>() {
            @Override
            public void onResponse(Call<QuizQuestionBase> call, Response<QuizQuestionBase> response) {
                if (response.isSuccessful()) {
                    if (progressDialog.isShowing()) {
                        progressDialog.cancel();
                    }
                    if (response.body() != null) {
                        if (response.body().getData() != null) {
                            mQuestioId = response.body().getData().get(0).getId();
                            mChooseOptions = response.body().getData().get(0).getAnswers();

                            view_question.setText(response.body().getData().get(0).getQuestion());
                            radio_button1.setText(response.body().getData().get(0).getOption1());
                            radio_button2.setText(response.body().getData().get(0).getOption2());
                            radio_button3.setText(response.body().getData().get(0).getOption3());
                            radio_button4.setText(response.body().getData().get(0).getOption4());
                            mdescription = response.body().getData().get(0).getDescript();

                            radio_button1.setClickable(true);
                            radio_button2.setClickable(true);
                            radio_button3.setClickable(true);
                            radio_button4.setClickable(true);
                            radio_button1.setBackground(getResources().getDrawable(R.drawable.option_bg));
                            radio_button2.setBackground(getResources().getDrawable(R.drawable.option_bg));
                            radio_button3.setBackground(getResources().getDrawable(R.drawable.option_bg));
                            radio_button4.setBackground(getResources().getDrawable(R.drawable.option_bg));
                            startTimer();
                        } else {
                            Toast.makeText(AttendQuizActivity.this, "No more quiz", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(mContext, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            finish();
                        }

                    } else {
                        Toast.makeText(mContext, "No more quiz", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(mContext, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        finish();
                    }
                } else {
                    if (progressDialog.isShowing()) {
                        progressDialog.cancel();
                        Toast.makeText(mContext, "No more quiz", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(mContext, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<QuizQuestionBase> call, Throwable t) {
                if (progressDialog.isShowing()) {
                    progressDialog.cancel();
                    Toast.makeText(mContext, "No more quiz", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(mContext, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    finish();
//                    Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void postQuizAnswers(String selectedOption) {

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<QuizAnswerModel> call = apiInterface.postQuizAnswers(appPreferences.getUserId(), mQuestioId, selectedOption);
        call.enqueue(new Callback<QuizAnswerModel>() {
            @Override
            public void onResponse(Call<QuizAnswerModel> call, Response<QuizAnswerModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                    } else {

                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<QuizAnswerModel> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    //    @Override
//    public void onRewardedVideoAdLoaded() {
//
//    }
//
//    @Override
//    public void onRewardedVideoAdOpened() {
//
//    }
//
//    @Override
//    public void onRewardedVideoStarted() {
//
//    }
//
//    @Override
//    public void onRewardedVideoAdClosed() {
//        loadRewardedVideoAd();
////        mCountDownTimer.cancel();
////        onTimerFinished();
//        startTimer();
////        visibleTimer();
//    }
//
//    @Override
//    public void onRewarded(RewardItem rewardItem) {
//
//    }
//
//    @Override
//    public void onRewardedVideoAdLeftApplication() {
//
//    }
//
//    @Override
//    public void onRewardedVideoAdFailedToLoad(int i) {
//
//    }
//
//    @Override
//    public void onRewardedVideoCompleted() {
//
//    }
}
