package com.xcrino.mcqproject.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.xcrino.mcqproject.R;
import com.xcrino.mcqproject.appInterface.APIInterface;
import com.xcrino.mcqproject.models.ChangePassowrd;
import com.xcrino.mcqproject.models.ChangeUserPassword;
import com.xcrino.mcqproject.utils.APIClient;
import com.xcrino.mcqproject.utils.AppPreferences;
import com.xcrino.mcqproject.utils.NetworkUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private TextView toolbar_title;
    private ImageView back_arrow;
    private TextInputEditText old_edittext, new_password_edittext, confirm_password_edittext;
    private Button change_password_button;

    private String userId, oldEmailPassword, newEmailPassword, confirmEmailPassword;
    private ChangeUserPassword changeUserPassword;
    private Call<ChangePassowrd> mCallData;
    private AppPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getUiInit();

        appPreferences = new AppPreferences(this);
        userId = appPreferences.getUserId();

        toolbar_title.setText("Change Password");
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        change_password_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
                    if (getDataFromInput()) {
                        changePassword();
                    }
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Check internet connection!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void changePassword() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        changeUserPassword = new ChangeUserPassword(userId, oldEmailPassword, newEmailPassword, confirmEmailPassword);
        mCallData = apiInterface.changePassword(changeUserPassword.getUserId(), changeUserPassword.getOldPassword(), changeUserPassword.getNewPassword(), changeUserPassword.getConfirmPassword());

        mCallData.enqueue(new Callback<ChangePassowrd>() {
            @Override
            public void onResponse(Call<ChangePassowrd> call, Response<ChangePassowrd> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(ChangePasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    appPreferences.ClearPreferences();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(ChangePasswordActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChangePassowrd> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ChangePasswordActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private boolean getDataFromInput() {
        boolean valid = true;
        oldEmailPassword = old_edittext.getText().toString().trim();
        newEmailPassword = new_password_edittext.getText().toString().trim();
        confirmEmailPassword = confirm_password_edittext.getText().toString().trim();

        if (oldEmailPassword.isEmpty() && oldEmailPassword.length() < 8) {
            old_edittext.setError("Enter old password");
            old_edittext.requestFocus();
            valid = false;
        } else if (newEmailPassword.isEmpty() && newEmailPassword.length() < 8) {
            new_password_edittext.setError("Enter new password");
            new_password_edittext.requestFocus();
            valid = false;
        } else if (confirmEmailPassword.isEmpty() && confirmEmailPassword.length() < 8) {
            confirm_password_edittext.setError("Enter confirm password");
            confirm_password_edittext.requestFocus();
            valid = false;
        } else if (!confirmEmailPassword.equals(newEmailPassword)) {
            Toast.makeText(this, "Your entered password not match ", Toast.LENGTH_SHORT).show();
        }

        return valid;
    }

    private void getUiInit() {
        back_arrow = (ImageView) findViewById(R.id.back_arrow);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        old_edittext = (TextInputEditText) findViewById(R.id.old_edittext);
        new_password_edittext = (TextInputEditText) findViewById(R.id.new_password_edittext);
        confirm_password_edittext = (TextInputEditText) findViewById(R.id.confirm_password_edittext);
        change_password_button = (Button) findViewById(R.id.change_password_button);
    }
}