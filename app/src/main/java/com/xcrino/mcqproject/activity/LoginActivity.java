package com.xcrino.mcqproject.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.xcrino.mcqproject.R;
import com.xcrino.mcqproject.appInterface.APIInterface;
import com.xcrino.mcqproject.models.LogInSignUpBase;
import com.xcrino.mcqproject.utils.APIClient;
import com.xcrino.mcqproject.utils.AppPreferences;
import com.xcrino.mcqproject.utils.Facebook;
import com.xcrino.mcqproject.utils.NetworkUtil;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    TextView mToolbar_title, forgot_password;
    Button mSign_up, mSign_in;
    Context mContext;
    private ImageView back_arrow;
    TextInputEditText mUsername_edittext, mPassword_edittext;
    String userMail, password, emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    AppPreferences mAppPreferences;

    private LinearLayout faceBook_Login, gmail_Login;
    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1;
    String Token;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;
        mToolbar_title = findViewById(R.id.toolbar_title);
        forgot_password = findViewById(R.id.forgot_password);
        mToolbar_title.setText("Sign In");
        mSign_up = findViewById(R.id.sign_up);
        mSign_in = findViewById(R.id.sign_in);
        back_arrow = findViewById(R.id.back_arrow);
        back_arrow.setVisibility(View.GONE);
        mUsername_edittext = findViewById(R.id.username_edittext);
        mPassword_edittext = findViewById(R.id.password_edittext);
        faceBook_Login = (LinearLayout) findViewById(R.id.faceBook_Login);
        gmail_Login = (LinearLayout) findViewById(R.id.gmail_Login);

        mAppPreferences = new AppPreferences(mContext);

        Token = FirebaseInstanceId.getInstance().getToken();
        Log.d("Token ",""+ FirebaseInstanceId.getInstance().getToken());
        FirebaseMessaging.getInstance().subscribeToTopic("allDevices");

        // code for facebook login.
        callbackManager = CallbackManager.Factory.create();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        new Facebook().printHashKey(this);
        faceBook_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));
                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                // App code
                                Log.d("token", loginResult.getAccessToken().getToken());
                                Profile profile = Profile.getCurrentProfile();
                                profile.getId();
                                profile.getProfilePictureUri(100, 100);
                                if (profile != null) {
                                    Log.d("name", profile.getName());
                                    if (NetworkUtil.isNetworkAvailable(mContext)) {
                                        socialLogin(profile.getName(),
                                                profile.getId(),
                                                "FB",
                                                "",
                                                "");
                                    } else {
                                        Toast.makeText(mContext, "Something went wrong! It may be due to network issues", Toast.LENGTH_SHORT).show();
                                    }
                                }

//                                AccessToken accessToken = loginResult.getAccessToken();
//                                Profile profile = Profile.getCurrentProfile();
//                                nextActivity(profile);
//                                Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancel() {
                                // App code
                            }

                            @Override
                            public void onError(FacebookException exception) {
                                // App code
                            }
                        });

            }
        });

        // code for google login.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        callbackManager = CallbackManager.Factory.create();
        gmail_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();

            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
                finish();
            }
        });

        mSign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RegisterActivity.class);
                finish();
                startActivity(intent);
            }
        });

        mSign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValid()) {
                    if (NetworkUtil.isNetworkAvailable(mContext)) {
                        signInProcess();
                    } else {
                        Toast.makeText(mContext, "Something went wrong! It may be due to network issues", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void socialLogin(String userName, String user_unique_id, String userLogInType, String profileImage, String email){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<LogInSignUpBase> call = apiInterface.socialLogin(userName, Token, user_unique_id, userLogInType, profileImage, email);
        call.enqueue(new Callback<LogInSignUpBase>() {
            @Override
            public void onResponse(Call<LogInSignUpBase> call, Response<LogInSignUpBase> response) {
                if (response.isSuccessful()) {
                    if (response.body().getResult() == true) {
                        progressDialog.dismiss();
                        mAppPreferences.setUserId(response.body().getUserId());
                        mAppPreferences.setAccessTokenString("Logged In");
                        Intent intent = new Intent(mContext, HomeActivity.class);
                        Toast.makeText(mContext, "Sign In successful", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LogInSignUpBase> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void nextActivity(Profile profile) {
       if (profile != null){
//            mAppPreferences.setUserId(profile.getId());
//            mAppPreferences.setAccessTokenString("Logged In");
           Intent intent = new Intent(mContext, HomeActivity.class);
           intent.putExtra("name", profile.getFirstName());
           intent.putExtra("surname", profile.getLastName());
           intent.putExtra("imageUrl", profile.getProfilePictureUri(185,185).toString());
           startActivity(intent);

       }
    }

    private boolean checkValid() {
        boolean valid = true;
        userMail = mUsername_edittext.getText().toString().trim();
        password = mPassword_edittext.getText().toString().trim();

        if (userMail.isEmpty()) {
            mUsername_edittext.setError("Enter Email Id");
            mUsername_edittext.requestFocus();
            valid = false;
        } else if (!userMail.matches(emailPattern)) {
            mUsername_edittext.setError("Enter Valid Email Id");
            mUsername_edittext.requestFocus();
            valid = false;
        } else if (password.isEmpty()) {
            mPassword_edittext.setError("Enter Password");
            mPassword_edittext.requestFocus();
            valid = false;
        }

        return valid;
    }

    private void signInProcess() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<LogInSignUpBase> call = apiInterface.loginUser(userMail, password, Token);
        call.enqueue(new Callback<LogInSignUpBase>() {
            @Override
            public void onResponse(Call<LogInSignUpBase> call, Response<LogInSignUpBase> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getResult()) {
                            progressDialog.dismiss();
                            mAppPreferences.setUserId(response.body().getUserId());
                            mAppPreferences.setAccessTokenString("Logged In");
                            Intent intent = new Intent(mContext, HomeActivity.class);
                            Toast.makeText(mContext, "Sign In successful", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LogInSignUpBase> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        }
    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 1);

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            account.getDisplayName();
            account.getEmail();
            account.getPhotoUrl();
            Toast.makeText(this, "Name  " + account.getDisplayName(), Toast.LENGTH_SHORT).show();
            if (account != null){
                if (NetworkUtil.isNetworkAvailable(mContext)) {
                    socialLogin(account.getDisplayName(),
                            account.getId(),
                            "GOOGLE",
                            "",
                            account.getEmail());
                } else {
                    Toast.makeText(mContext, "Something went wrong! It may be due to network issues", Toast.LENGTH_SHORT).show();
                }
            }
            // Signed in successfully, show authenticated UI.
            //  updateUI(account);
        } catch (ApiException e) {
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Result", "signInResult:failed code=" + e.getStatusCode());
            // updateUI(null);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(mContext, "Failed to authenticate. Please try again.", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
//        nextActivity(profile);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        AccessTokenTracker accessTokenTracker = null;
//        accessTokenTracker.stopTracking();
    }
}