package com.xcrino.mcqproject.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.hbb20.CountryCodePicker;
import com.xcrino.mcqproject.R;
import com.xcrino.mcqproject.utils.NetworkUtil;

public class EditProfileActivity extends AppCompatActivity {

    private TextView toolbar_title;
    private ImageView back_arrow;

    private TextInputEditText fullname_profile, mobile_numberProfile, email_id_Profile;
    private Button submit_profile;
    private CountryCodePicker ccp;

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private String fullName, mobileNo, emailId, userCountryName;
    private String countryName = "India";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getUiInitLayout();
        toolbar_title.setText("Edit Profile");
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryName = ccp.getSelectedCountryName();

            }
        });

        submit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidData()) {
                    if (NetworkUtil.isNetworkAvailable(mContext)) {
//                        editProfileProcess();
                    } else {
                        Toast.makeText(mContext, "Something went wrong! It may be due to network issues", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void editProfileProcess() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

    }

    private boolean checkValidData() {
        boolean valid = true;

        fullName = fullname_profile.getText().toString().trim();
        mobileNo = mobile_numberProfile.getText().toString().trim();
        userCountryName = countryName.toString().trim();
        emailId = email_id_Profile.getText().toString().trim();

        if (TextUtils.isEmpty(fullName)) {
            fullname_profile.setError("Enter Full Name");
            fullname_profile.requestFocus();
            valid = false;

        } else if (TextUtils.isEmpty(mobileNo)) {
            mobile_numberProfile.setError("Enter Mobile Number");
            mobile_numberProfile.requestFocus();
            valid = false;

        } else if (TextUtils.isEmpty(emailId)) {
            email_id_Profile.setError("Enter Email Id");
            email_id_Profile.requestFocus();
            valid = false;

        } else if (!emailId.matches(emailPattern)) {
            email_id_Profile.setError("Invalid email address");
            email_id_Profile.requestFocus();
            valid = false;

        }
        return valid;
    }

    private void getUiInitLayout() {
        submit_profile = (Button) findViewById(R.id.submit_profile);
        fullname_profile = (TextInputEditText) findViewById(R.id.fullname_profile);
        mobile_numberProfile = (TextInputEditText) findViewById(R.id.mobile_numberProfile);
        email_id_Profile = (TextInputEditText) findViewById(R.id.email_id_Profile);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        back_arrow = (ImageView) findViewById(R.id.back_arrow);

    }
}