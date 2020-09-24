package com.xcrino.mcqproject.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.xcrino.mcqproject.R;
import com.xcrino.mcqproject.appInterface.APIInterface;
import com.xcrino.mcqproject.models.PaymentModel;
import com.xcrino.mcqproject.models.UserSubscription;
import com.xcrino.mcqproject.utils.APIClient;
import com.xcrino.mcqproject.utils.AppPreferences;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    private static final String TAG = PaymentActivity.class.getSimpleName();

    Button checkout_button;
    private AppPreferences appPreferences;
    private TextView text_amount;

    private TextView toolbar_title;
    private ImageView back_arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        back_arrow = (ImageView) findViewById(R.id.back_arrow);

        toolbar_title.setText("Payment");
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Checkout.clearUserData(getApplicationContext());
        Checkout.preload(getApplicationContext());
        appPreferences = new AppPreferences(getApplicationContext());

        checkout_button = findViewById(R.id.checkout_button);
        text_amount = findViewById(R.id.text_amount);
        if (appPreferences.getCountry().equals("India")) {
            text_amount.setText("₹ " + appPreferences.getAmount().toString());
        }
        else {
            text_amount.setText("$ " + appPreferences.getAmount().toString());
        }

        checkout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayment();
            }
        });
    }

    private void startPayment() {
        final Activity activity = this;
        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "QwizApp");
            options.put("description", "");
            if (appPreferences.getCountry().equals("India")){
                options.put("currency", "INR");
            }
            else {
                options.put("currency", "USD");
            }
            int amount = (Integer.parseInt(appPreferences.getAmount())) * 100;
            options.put("amount", Integer.toString(amount));

            JSONObject preFill = new JSONObject();
            preFill.put("email", appPreferences.getEmail());
            preFill.put("contact", appPreferences.getPhone());

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
            if (appPreferences.getSubStatus()){
                paymentUpdate(razorpayPaymentID);
            }
            else {
                paymentSuccess(razorpayPaymentID);
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }

    private void paymentUpdate(String razorpayPaymentID) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<UserSubscription> updateSubscription = apiInterface.updateSubscription(appPreferences.getUserId(),
                appPreferences.getSubscriptionId(),
                razorpayPaymentID,
                appPreferences.getAmount(),
                appPreferences.getCurrencyCode());
        updateSubscription.enqueue(new Callback<UserSubscription>() {
            @Override
            public void onResponse(Call<UserSubscription> call, Response<UserSubscription> response) {
                if (response.isSuccessful()){
                    progressDialog.dismiss();
                    finish();
                }
                else {
                    progressDialog.dismiss();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<UserSubscription> call, Throwable t) {
                progressDialog.dismiss();
                finish();
            }
        });

    }

    private void paymentSuccess(String razorpayPaymentID) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<PaymentModel> subscriptionCall = apiInterface.postPayment(appPreferences.getUserId(),
                appPreferences.getSubscriptionId(),
                razorpayPaymentID,
                appPreferences.getAmount(),
                appPreferences.getCurrencyCode());

        subscriptionCall.enqueue(new Callback<PaymentModel>() {
            @Override
            public void onResponse(Call<PaymentModel> call, Response<PaymentModel> response) {
//                Log.i("results", response.body().toString());
                if (response.isSuccessful()){
                    progressDialog.dismiss();
                    if (response.body() != null){
                        finish();
//                        Toast.makeText(PaymentActivity.this, "Response Sent!", Toast.LENGTH_SHORT).show();
                    }else {
                        finish();
//                        Toast.makeText(PaymentActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    progressDialog.dismiss();
                    finish();
//                    Toast.makeText(PaymentActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PaymentModel> call, Throwable t) {
                progressDialog.dismiss();
                finish();
//                Toast.makeText(PaymentActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
