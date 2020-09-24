package com.xcrino.mcqproject.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;
import com.xcrino.mcqproject.BuildConfig;
import com.xcrino.mcqproject.R;
import com.xcrino.mcqproject.adapter.CategoryAdapter;
import com.xcrino.mcqproject.appInterface.APIInterface;
import com.xcrino.mcqproject.models.ImageProfile;
import com.xcrino.mcqproject.models.SubjectList;
import com.xcrino.mcqproject.models.SubjectPosition;
import com.xcrino.mcqproject.models.UserData;
import com.xcrino.mcqproject.models.UserProfile;
import com.xcrino.mcqproject.models.Wallet;
import com.xcrino.mcqproject.models.WatchVideo;
import com.xcrino.mcqproject.utils.APIClient;
import com.xcrino.mcqproject.utils.AppPreferences;
import com.xcrino.mcqproject.utils.NetworkUtil;
import com.xcrino.mcqproject.utils.PermissionsChecker;

import java.io.File;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, RewardedVideoAdListener/*, GoogleApiClient.OnConnectionFailedListener*/ {

    private DrawerLayout drawer_layout;
    private NavigationView design_navigation_view;
    private ImageView navigation, support, notification;
    private ViewFlipper viewFlipper;
    private RecyclerView recycler_View;

    private LinearLayout attend_ques, publish_ques, watch_video, refer_earn, entertainment, technology, news, business, sports,
            general_knowledge, computer_science, finance, communication, education, history, more;

    private FloatingActionButton floatingActionButton;
    private ImageView img_profile;
    private TextView tv_user_Name, tv_email_Id, question_uploaded, watched_videos, attend_quizes, toolbar_title;
    private String profileImage, userEmailId, userName, uploadQuestion, videoWatched, questionsAttend;
    private String imagePath;
    private RewardedVideoAd mRewardedVideoAd;
    private static final String AD_UNIT_ID = "ca-app-pub-3548280363774169~5766912321";
    AppPreferences mAppPreferences;

    PermissionsChecker checker;
    private static final String[] PERMISSIONS_READ_STORAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    private Uri contentUris;
    private String postPath;
    private ImageProfile imageProfile;
    private Bitmap.CompressFormat compressedImage;

    private String mCountry;
    private List<UserData> data = null;
    private List<Wallet> wallet = null;
    private List<SubjectPosition> data1 = null;

    private ImageView profile_Image;
    private TextView profile_user_Name, available_balance, current_coin_balance, refer_earn_desc;
    private String totalCoins;
    private float amount;

    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;

    private LinearLayout coins_available, balance_available;
    //    private InterstitialAd mInterstitialAd;
    private AdLoader adLoader;
    private NativeAd nativeAd;
    Animation animation;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getUiInit();
        toolbar_title.setText("QwizApp");

        support.setOnClickListener(this);
        notification.setOnClickListener(this);
        attend_ques.setOnClickListener(this);
        publish_ques.setOnClickListener(this);
        watch_video.setOnClickListener(this);
        refer_earn.setOnClickListener(this);
        balance_available.setOnClickListener(this);
        coins_available.setOnClickListener(this);
        entertainment.setOnClickListener(this);
        technology.setOnClickListener(this);
        news.setOnClickListener(this);
        business.setOnClickListener(this);
        sports.setOnClickListener(this);
        general_knowledge.setOnClickListener(this);
        computer_science.setOnClickListener(this);
        finance.setOnClickListener(this);
        communication.setOnClickListener(this);
        education.setOnClickListener(this);
        history.setOnClickListener(this);
        more.setOnClickListener(this);

        MobileAds.initialize(getApplicationContext(), AD_UNIT_ID);

        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");   //Sample ID
//        mInterstitialAd.setAdUnitId("ca-app-pub-3548280363774169/8898591678");
        mInterstitialAd.setAdUnitId("ca-app-pub-3548280363774169/6915845024");

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
                if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
                    postWatchedVideo();
                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong! It may be due to network issues", Toast.LENGTH_SHORT).show();
                }
            }
        });

        animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.blink);
        publish_ques.startAnimation(animation);


        Log.d("Token ",""+ FirebaseInstanceId.getInstance().getToken());
        FirebaseMessaging.getInstance().subscribeToTopic("allDevices");

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        mAppPreferences = new AppPreferences(getApplicationContext());

      /*  int[] images = {R.drawable.first, R.drawable.second, R.drawable.third, R.drawable.faur};

        for (int i = 0; i < images.length; i++) {
            flipperImage(images[i]);
        }*/

        checker = new PermissionsChecker(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer_layout, null,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer_layout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);

        // header code
        design_navigation_view.setNavigationItemSelectedListener(this);
        design_navigation_view.setItemIconTintList(null);
        View headerLayout = design_navigation_view.getHeaderView(0);
        img_profile = (ImageView) headerLayout.findViewById(R.id.img_profile);
        floatingActionButton = (FloatingActionButton) headerLayout.findViewById(R.id.floatingActionButton);
        tv_user_Name = headerLayout.findViewById(R.id.tv_user_Name);
        tv_email_Id = headerLayout.findViewById(R.id.tv_email_Id);
        question_uploaded = headerLayout.findViewById(R.id.question_uploaded);
        watched_videos = headerLayout.findViewById(R.id.watched_videos);
        attend_quizes = headerLayout.findViewById(R.id.attend_quizes);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(imagePath)) {
                    showImagePopup();
                    if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
//                        uploadImage();
                    } else {
                        Toast.makeText(HomeActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showImagePopup();
//                    Toast.makeText(HomeActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_layout.openDrawer(GravityCompat.START);

            }
        });
        toggle.syncState();

        if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
            getSubjectList();
        } else {
            Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
        }

       /* gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,  this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();*/
        if (mAppPreferences.getCountry().equals("India")) {
            refer_earn_desc.setText("Earn upto ₹70 from Referral Install");
        } else {
            refer_earn_desc.setText("Earn upto 1$ from Referral Install");
        }
    }

    private void getSubjectList() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<SubjectList> subjectListCall = apiInterface.getSubjectList();

        subjectListCall.enqueue(new Callback<SubjectList>() {
            @Override
            public void onResponse(Call<SubjectList> call, Response<SubjectList> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    data1 = response.body().getData();
                    setCategoryList();

                } else {
                    progressDialog.dismiss();
//                    Toast.makeText(HomeActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubjectList> call, Throwable t) {
                progressDialog.dismiss();
                t.getMessage();
            }
        });
    }

    private void setCategoryList() {
        recycler_View.setAdapter(new CategoryAdapter(getApplicationContext(), data1));
        recycler_View.setLayoutManager(new GridLayoutManager(this, 3));
        recycler_View.setHasFixedSize(true);
    }

    private void showImagePopup() {
        if (checker.lacksPermissions(PERMISSIONS_READ_STORAGE)) {
            startPermissionsActivity(PERMISSIONS_READ_STORAGE);
        } else {
            // File System.
            final Intent galleryIntent = new Intent();
            galleryIntent.setType("image/*");
            galleryIntent.setAction(Intent.ACTION_PICK);
            // Chooser of file system options.
            final Intent chooserIntent = Intent.createChooser(galleryIntent, getString(R.string.string_choose_image));
            startActivityForResult(chooserIntent, 1010);
        }
    }

    private void startPermissionsActivity(String[] permissionsReadStorage) {
        CheckPermissionActivity.startActivityForResult(this, 0, permissionsReadStorage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1010) {
            if (data != null) {
                contentUris = data.getData();

                Uri selectedImageUri = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);

                if (cursor != null) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    postPath = cursor.getString(columnIndex);
                    cursor.close();
                    uploadImage();
                    img_profile.setVisibility(View.VISIBLE);

                } else {
                    Toast.makeText(this, "Unable to load image !", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "Unable to pick image !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadImage() {
        if (postPath.equals(null) || postPath.isEmpty()) {
            Toast.makeText(this, "please select an image", Toast.LENGTH_SHORT).show();
            return;
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();

            imageProfile = new ImageProfile(mAppPreferences.getUserId(), profileImage);
            File file = new File(postPath);
            RequestBody user_Id = RequestBody.create(MediaType.parse("text/plain"), imageProfile.getUserId().toString());
            RequestBody requestFile = RequestBody.create(MediaType.parse("*/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("profile", file.getName(), requestFile);

            APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
            Call<ResponseBody> requestBodyCall = apiInterface.changeUserProfile(body, user_Id);

            requestBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        Log.i("result", response.body().toString());
                        if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
                            getUserProfileDetails();
                        } else {
                            Toast.makeText(getApplicationContext(), "Check Internet Connection !", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressDialog.dismiss();

                }
            });
        }
    }

    private void getUserProfileDetails() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<UserProfile> profileCall = apiInterface.getDetailsUseProfile(mAppPreferences.getUserId());

        profileCall.enqueue(new Callback<UserProfile>() {
            @RequiresApi(api = Build.VERSION_CODES.O_MR1)
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
//                Log.i("result", response.body().toString());
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        UserProfile userProfile = response.body();
                        String message = response.body().getMessage();
                        if (message.equals("Success")) {
                            data = userProfile.getData();
                            userName = data.get(0).getFirstName();
                            userEmailId = data.get(0).getEmail();
                            mCountry = data.get(0).getCountry();
                            mAppPreferences.saveCountry(mCountry);
                            mAppPreferences.saveEmail(userEmailId);
                            mAppPreferences.savePhone(data.get(0).getPhone());
                            mAppPreferences.saveUserId(data.get(0).getId());
                            mAppPreferences.setReferCode(data.get(0).getRefferal_code());

                            wallet = userProfile.getWallet();
                            totalCoins = wallet.get(0).getTotal().toString();
                            uploadQuestion = wallet.get(0).getUploadedPoint() != null ? Integer.toString(wallet.get(0).getUploadedPoint()) : "0.0";
                            videoWatched = wallet.get(0).getWatchedPoint() != null ? Integer.toString(wallet.get(0).getWatchedPoint()) : "0.0";
                            questionsAttend = wallet.get(0).getRightPoint() != null ? Integer.toString(wallet.get(0).getRightPoint()) : "0.0";

                            String display;
                            amount = Float.valueOf(totalCoins);
                            if (mAppPreferences.getCountry().equals("India")) {
                                display = String.valueOf((amount / 10000) * 70);
                                available_balance.setText("₹" + display);

                            } else {
                                display = String.valueOf((amount / 10000) * 1);
                                available_balance.setText(display + "$");
                            }

                            try {
                                profileImage = data.get(0).getImage().toString();
                            } catch (NullPointerException nlp) {
                            }

                            tv_user_Name.setText(userName);
                            tv_email_Id.setText(userEmailId);
                            profile_user_Name.setText(userName);

                            current_coin_balance.setText(totalCoins);
                            question_uploaded.setText(uploadQuestion);
                            watched_videos.setText(videoWatched);
                            attend_quizes.setText(questionsAttend);

                            Picasso.with(getApplicationContext()).load(profileImage).resize(185, 185).transform(new CropCircleTransformation()).placeholder(R.drawable.iconfinder).into(img_profile);
                            Picasso.with(getApplicationContext()).load(profileImage).resize(185, 185).transform(new CropCircleTransformation()).placeholder(R.drawable.iconfinder).into(profile_Image);

                        } else {
                            Toast.makeText(HomeActivity.this, "something went wrong !", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(HomeActivity.this, "something went wrong !", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(HomeActivity.this, "something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(HomeActivity.this, "something went wrong !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void flipperImage(int image) {
        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setBackgroundResource(image);

        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);

        viewFlipper.setInAnimation(getApplicationContext(), R.anim.slide_in);
        viewFlipper.setOutAnimation(getApplicationContext(), R.anim.slide_out);
    }

    private void getUiInit() {
        balance_available = (LinearLayout) findViewById(R.id.balance_available);
        coins_available = (LinearLayout) findViewById(R.id.coins_available);
        refer_earn = (LinearLayout) findViewById(R.id.refer_earn);
        attend_ques = (LinearLayout) findViewById(R.id.attend_ques);
        publish_ques = (LinearLayout) findViewById(R.id.publish_ques);
        watch_video = (LinearLayout) findViewById(R.id.watch_video);
        recycler_View = (RecyclerView) findViewById(R.id.recycler_View);
//        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        support = (ImageView) findViewById(R.id.support);
        notification = (ImageView) findViewById(R.id.notification);
        navigation = (ImageView) findViewById(R.id.navigation);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        design_navigation_view = (NavigationView) findViewById(R.id.design_navigation_view);
        toolbar_title = findViewById(R.id.toolbar_title);
        profile_Image = (ImageView) findViewById(R.id.profile_Image);
        profile_user_Name = (TextView) findViewById(R.id.profile_user_Name);
        available_balance = (TextView) findViewById(R.id.available_balance);
        current_coin_balance = (TextView) findViewById(R.id.current_coin_balance);
        refer_earn_desc = (TextView) findViewById(R.id.refer_earn_desc);
        entertainment = (LinearLayout) findViewById(R.id.entertainment);
        technology = (LinearLayout) findViewById(R.id.technology);
        news = (LinearLayout) findViewById(R.id.news);
        business = (LinearLayout) findViewById(R.id.business);
        sports = (LinearLayout) findViewById(R.id.sports);
        general_knowledge = (LinearLayout) findViewById(R.id.general_knowledge);
        computer_science = (LinearLayout) findViewById(R.id.computer_science);
        finance = (LinearLayout) findViewById(R.id.finance);
        communication = (LinearLayout) findViewById(R.id.communication);
        education = (LinearLayout) findViewById(R.id.education);
        history = (LinearLayout) findViewById(R.id.history);
        more = (LinearLayout) findViewById(R.id.more);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.wallet_nav) {
            startActivity(new Intent(getApplicationContext(), WalletActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

        } else if (id == R.id.change_password_nav) {
            startActivity(new Intent(getApplicationContext(), ChangePasswordActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

        } else if (id == R.id.share_nav) {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String shareMessage;
                if (mAppPreferences.getCountry().equals("India")) {
                    shareMessage = "Hello Dear Friends.\n" +
                            "I have earned Rs. 15000 by QUIZAPP. You can also earn by QWIZAPP Simply,\n" +
                            "Quick Download from below link and enter Referral code " + mAppPreferences.getReferCode() + " to get free ₹70 Credits \n" +
                            "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;

                } else {
                    shareMessage = "Hello Dear Friends.\n" +
                            "I have earned $200 By QUIZAPP. You can also earn by QWIZAPP Simply,\n" +
                            "Quick Download from below link and enter Referral code " + mAppPreferences.getReferCode() + " to get free $1 Credits \n" +
                            "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
                }

//                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n" + "Use the Referral Code: " + appPreferences.getReferCode();
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch (Exception e) {
                e.getMessage();
            }

        } else if (id == R.id.terms_condition_nav) {
            startActivity(new Intent(getApplicationContext(), TermsAndConditionsActivity.class));

        } else if (id == R.id.logout_nav) {
            mAppPreferences.ClearPreferences();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();

            /* Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                if (status.isSuccess()){
                                    gotoLoginActivity();
                                }else{
                                    Toast.makeText(getApplicationContext(),"Session not close",Toast.LENGTH_LONG).show();
                                }
                            }
                        });*/


        } else if (id == R.id.subscription_nav) {
            Intent intent = new Intent(HomeActivity.this, SubscriptionPlanActivity.class);
            startActivity(intent);

        } else if (id == R.id.history_nav) {
            Intent intent = new Intent(HomeActivity.this, QuestionListActivity.class);
            startActivity(intent);

        }
//        else if (id == R.id.editProfile_nav) {
//            Intent intent = new Intent(HomeActivity.this, EditProfileActivity.class);
//            startActivity(intent);
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.support:
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "", null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
                break;

            case R.id.notification:
//                startActivity(new Intent(getApplicationContext(), NotificationActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                break;

            case R.id.attend_ques:

            case R.id.more:
                mAppPreferences.setCategoryId("0");
                startActivity(new Intent(getApplicationContext(), AttendQuizActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                break;

            case R.id.publish_ques:
                startActivity(new Intent(getApplicationContext(), PublishQuestionsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                break;

            case R.id.watch_video:
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "The Ad wasn't loaded yet", Toast.LENGTH_SHORT).show();
                    loadRewardedVideoAd();
                }
//                if (mRewardedVideoAd.isLoaded()) {
//                    mRewardedVideoAd.show();
//                } else {
//                    Toast.makeText(getApplicationContext(), "The Ad wasn't loaded yet", Toast.LENGTH_SHORT).show();
//                    loadRewardedVideoAd();
//                }
                break;

            case R.id.refer_earn:
                startActivity(new Intent(getApplicationContext(), ReferAndEarnActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                break;

            case R.id.balance_available:

            case R.id.coins_available:
                startActivity(new Intent(getApplicationContext(), WalletActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                break;

            case R.id.entertainment:
                mAppPreferences.setCategoryId("6");
                startActivity(new Intent(getApplicationContext(), AttendQuizActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                break;

            case R.id.technology:
                mAppPreferences.setCategoryId("5");
                startActivity(new Intent(getApplicationContext(), AttendQuizActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                break;

            case R.id.news:
                mAppPreferences.setCategoryId("4");
                startActivity(new Intent(getApplicationContext(), AttendQuizActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                break;

            case R.id.business:
                mAppPreferences.setCategoryId("3");
                startActivity(new Intent(getApplicationContext(), AttendQuizActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                break;

            case R.id.sports:
                mAppPreferences.setCategoryId("2");
                startActivity(new Intent(getApplicationContext(), AttendQuizActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                break;

            case R.id.general_knowledge:
                mAppPreferences.setCategoryId("7");
                startActivity(new Intent(getApplicationContext(), AttendQuizActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                break;

            case R.id.computer_science:
                mAppPreferences.setCategoryId("9");
                startActivity(new Intent(getApplicationContext(), AttendQuizActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                break;

            case R.id.finance:
                mAppPreferences.setCategoryId("11");
                startActivity(new Intent(getApplicationContext(), AttendQuizActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                break;

            case R.id.communication:
                mAppPreferences.setCategoryId("12");
                startActivity(new Intent(getApplicationContext(), AttendQuizActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                break;

            case R.id.education:
                mAppPreferences.setCategoryId("13");
                startActivity(new Intent(getApplicationContext(), AttendQuizActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                break;

            case R.id.history:
                mAppPreferences.setCategoryId("16");
                startActivity(new Intent(getApplicationContext(), AttendQuizActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                break;
        }
    }

    @Override
    protected void onResume() {
//        MobileAds.initialize(getApplicationContext(), AD_UNIT_ID);
//        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getApplicationContext());
//        mRewardedVideoAd.setRewardedVideoAdListener(this);
//        loadRewardedVideoAd();

        if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
            getUserProfileDetails();
        } else {
            Toast.makeText(this, "Check Internet Connection !", Toast.LENGTH_SHORT).show();
        }

        super.onResume();
    }

    private void loadRewardedVideoAd() {
//        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
//                new AdRequest.Builder().build());             // Sample Id
//        mRewardedVideoAd.loadAd("ca-app-pub-3548280363774169/5357241414",
//                new AdRequest.Builder().build());

        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        loadRewardedVideoAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        loadRewardedVideoAd();
        if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
            postWatchedVideo();
        } else {
            Toast.makeText(getApplicationContext(), "Something went wrong! It may be due to network issues", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }

    private void postWatchedVideo() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<WatchVideo> call = apiInterface.postWatchedVideo(mAppPreferences.getUserId());
        call.enqueue(new Callback<WatchVideo>() {
            @Override
            public void onResponse(Call<WatchVideo> call, Response<WatchVideo> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WatchVideo> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*private void gotoLoginActivity() {
        Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr= Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(opr.isDone()){
            GoogleSignInResult result=opr.get();
            handleSignInResult(result);
        }else{
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if(result.isSuccess()){
            GoogleSignInAccount account=result.getSignInAccount();
            profile_user_Name.setText(account.getDisplayName());
            tv_user_Name.setText(account.getDisplayName());
            tv_email_Id.setText(account.getEmail());
            try{
                Picasso.with(getApplicationContext()).load(profileImage).resize(185, 185).transform(new CropCircleTransformation()).into(img_profile);
                Picasso.with(getApplicationContext()).load(profileImage).resize(185, 185).transform(new CropCircleTransformation()).into(profile_Image);
            }catch (NullPointerException e){
                Toast.makeText(getApplicationContext(),"image not found",Toast.LENGTH_LONG).show();
            }

        }else{
            gotoLoginActivity();
        }
    }*/

}