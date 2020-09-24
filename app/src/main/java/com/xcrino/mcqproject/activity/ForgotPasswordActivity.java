package com.xcrino.mcqproject.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.xcrino.mcqproject.R;
import com.xcrino.mcqproject.appInterface.APIInterface;
import com.xcrino.mcqproject.models.ForgotPassword;
import com.xcrino.mcqproject.utils.APIClient;
import com.xcrino.mcqproject.utils.NetworkUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputEditText email_id_edittext;

    private TextView toolbar_title;
    private ImageView back_arrow;

    private Button forgot_password_button;
    private String userMail;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email_id_edittext = (TextInputEditText) findViewById(R.id.email_id_edittext);
        forgot_password_button = (Button) findViewById(R.id.forgot_password_button);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        back_arrow = (ImageView) findViewById(R.id.back_arrow);

        toolbar_title.setText("Forgot Password");
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        forgot_password_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
                    if (getDataForForgotPassword()) {
                        userForgotPassword();
                    }
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Check internet connection!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void userForgotPassword() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ForgotPassword> forgotPasswordCall = apiInterface.forgotPassword(userMail);

        forgotPasswordCall.enqueue(new Callback<ForgotPassword>() {
            @Override
            public void onResponse(Call<ForgotPassword> call, Response<ForgotPassword> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().getResult() == true) {
                        Toast.makeText(ForgotPasswordActivity.this, "Please Check Your Email !", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(ForgotPasswordActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(ForgotPasswordActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ForgotPassword> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private boolean getDataForForgotPassword() {
        boolean valid = true;
        userMail = email_id_edittext.getText().toString().trim();

        if (userMail.isEmpty()) {
            email_id_edittext.setError("Enter Email Id");
            email_id_edittext.requestFocus();
            valid = false;
        } else if (!userMail.matches(emailPattern)) {
            Toast.makeText(getApplicationContext(), "Enter valid email Id", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }
}
