package com.xcrino.mcqproject.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hbb20.CountryCodePicker;
import com.xcrino.mcqproject.R;
import com.xcrino.mcqproject.appInterface.APIInterface;
import com.xcrino.mcqproject.models.LogInSignUpBase;
import com.xcrino.mcqproject.utils.APIClient;
import com.xcrino.mcqproject.utils.NetworkUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    TextView mToolbar_title, check_text;
    Button mSign_up, mSign_in;
    Context mContext;
    private ImageView back_arrow;
    CheckBox mPolicy_checkbox, refer_check;

    private CountryCodePicker ccp;
    private TextInputEditText fullname_edittext, mobile_number, email_id, password, confirm_password, referal_code_editText;
    private TextInputLayout input_box;

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private String userFullName, userMobileNumber, userCountryName = "India", userEmail, userEmailPassword, confirmPassword;
    private String countryName = "India";
    boolean checked = true, referr_check = false;
    String Token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mContext = this;
        mToolbar_title = findViewById(R.id.toolbar_title);
        mToolbar_title.setText("Sign Up");
        mSign_up = findViewById(R.id.sign_up);
        mSign_in = findViewById(R.id.sign_in);
        mPolicy_checkbox = findViewById(R.id.policy_checkbox);
        back_arrow = findViewById(R.id.back_arrow);
        check_text = findViewById(R.id.check_text);
        refer_check = findViewById(R.id.refer_check);
        referal_code_editText = findViewById(R.id.referal_code_editText);

        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        fullname_edittext = (TextInputEditText) findViewById(R.id.fullname_edittext);
        mobile_number = (TextInputEditText) findViewById(R.id.mobile_number);
        email_id = (TextInputEditText) findViewById(R.id.email_id);
        password = (TextInputEditText) findViewById(R.id.password);
        confirm_password = (TextInputEditText) findViewById(R.id.confirm_password);
        input_box = findViewById(R.id.input_box);

        Token = FirebaseInstanceId.getInstance().getToken();
        Log.d("Token ",""+ FirebaseInstanceId.getInstance().getToken());
        FirebaseMessaging.getInstance().subscribeToTopic("allDevices");

        back_arrow.setVisibility(View.GONE);

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryName = ccp.getSelectedCountryName();

            }
        });

        mSign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValid()) {
                    if (NetworkUtil.isNetworkAvailable(mContext)) {
                        signUpProcess();
                    } else {
                        Toast.makeText(mContext, "Something went wrong! It may be due to network issues", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        mSign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });

        mPolicy_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checked = true;
                } else {
                    checked = false;
                }
            }
        });

        String text = "I agree to Privacy Policy.";
        SpannableString ssBuilder = new SpannableString(text);
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.privacy_policy_popup, null);
                builder.setPositiveButton("OK", null);
                builder.setView(dialogLayout);
                builder.show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
            }
        };
        ssBuilder.setSpan(clickableSpan1, 11, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        check_text.setText(ssBuilder);
        check_text.setMovementMethod(LinkMovementMethod.getInstance());

        refer_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    input_box.setVisibility(View.VISIBLE);
                    referr_check = true;
                } else {
                    input_box.setVisibility(View.GONE);
                    referr_check = false;
                }
            }
        });
    }

    private boolean checkValid() {
        boolean valid = true;

        userFullName = fullname_edittext.getText().toString().trim();
        userMobileNumber = mobile_number.getText().toString().trim();
        userCountryName = countryName.toString().trim();
        userEmail = email_id.getText().toString().trim();
        userEmailPassword = password.getText().toString().trim();
        confirmPassword = confirm_password.getText().toString().trim();

        if (TextUtils.isEmpty(userFullName)) {
            fullname_edittext.setError("Enter Full Name");
            fullname_edittext.requestFocus();
            valid = false;

        } else if (TextUtils.isEmpty(userMobileNumber)) {
            mobile_number.setError("Enter Mobile Number");
            mobile_number.requestFocus();
            valid = false;

        } else if (TextUtils.isEmpty(userEmail)) {
            email_id.setError("Enter Email Id");
            email_id.requestFocus();
            valid = false;

        } else if (!userEmail.matches(emailPattern)) {
            email_id.setError("Invalid email address");
            email_id.requestFocus();
            valid = false;

        } else if (TextUtils.isEmpty(userEmailPassword) && userEmailPassword.length() < 8) {
            password.setError("Enter User Password");
            password.requestFocus();
            valid = false;

        } else if (confirmPassword.equals(userEmailPassword) && TextUtils.isEmpty(confirmPassword) || confirmPassword.length() < 8) {
            confirm_password.setError("Enter Confirm Password");
            confirm_password.requestFocus();
            valid = false;

        } else if (!confirmPassword.equals(userEmailPassword)) {
            confirm_password.setError("Your entered password not match");
            confirm_password.requestFocus();
            valid = false;
        } else if (checked == false) {
            Toast.makeText(this, "Agree to Privacy Policy ", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }

    private void signUpProcess() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<LogInSignUpBase> call = apiInterface.createUser(userFullName,
                userMobileNumber,
                userCountryName,
                userEmail,
                userEmailPassword,
                confirmPassword,
                referr_check,
                referal_code_editText.getText().toString(),
                Token);
        call.enqueue(new Callback<LogInSignUpBase>() {
            @Override
            public void onResponse(Call<LogInSignUpBase> call, Response<LogInSignUpBase> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null){
                        if (response.body().getResult()) {
                            progressDialog.dismiss();
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            startActivity(intent);
                            Toast.makeText(mContext, "Sign Up Successful", Toast.LENGTH_SHORT).show();
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
}
