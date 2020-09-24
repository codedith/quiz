package com.xcrino.mcqproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xcrino.mcqproject.R;
import com.xcrino.mcqproject.adapter.SubscriptionPlanAdapter;
import com.xcrino.mcqproject.adapter.WalletDetailsListAdapter;
import com.xcrino.mcqproject.appInterface.APIInterface;
import com.xcrino.mcqproject.models.Data;
import com.xcrino.mcqproject.models.WalletData;
import com.xcrino.mcqproject.utils.APIClient;
import com.xcrino.mcqproject.utils.AppPreferences;
import com.xcrino.mcqproject.utils.NetworkUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletActivity extends AppCompatActivity {

    private AppPreferences appPreferences;

    private Button withdrawal_button, upgrade_plan_button;
    private TextView toolbar_title, not_transactions;
    private ImageView back_arrow;
    private TextView rght_count_tv, wrong_count_tv, upload_count_tv, attend_count_tv, watch_count_tv, referral_count_tv, balance, balanceInRupees;

    private String rightPoints, wrongPoints, upploadPoints, attendsPoint, watchPoints, referralPoints, balances;
    private String userId;
    private List<Data> data;

    private RecyclerView recycler_view;
    private WalletDetailsListAdapter walletDetailsListAdapter;
    private WalletData walletData;
    private LinearLayout wallet_data;
    private float amount, availableBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        getUiInit();

        appPreferences = new AppPreferences(this);
        userId = appPreferences.getUserId();

        toolbar_title.setText("My Wallet");
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

            withdrawal_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    appPreferences.setAvailableBalance(availableBalance);
                    if (appPreferences.getCountry().equals("India")) {
                        if (availableBalance <= 500) {
                            Toast.makeText(getApplicationContext(), "Your withdrawal amount should be more than 500 INR", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            startActivity(new Intent(getApplicationContext(), CashWithdrawalActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        }
                    } else {
                        if (availableBalance <= 100) {
                            Toast.makeText(getApplicationContext(), "Your withdrawal amount should be more than 100 USD", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            startActivity(new Intent(getApplicationContext(), CashWithdrawalActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        }
                    }

                }
            });

        if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
            getWalletData();
        } else {
            Toast.makeText(this, "Check Internet Connection !", Toast.LENGTH_SHORT).show();
        }

        upgrade_plan_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SubscriptionPlanActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getWalletData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        final APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<WalletData> walletDataCall = apiInterface.getWalletDataDetails(userId);

        walletDataCall.enqueue(new Callback<WalletData>() {
            @Override
            public void onResponse(Call<WalletData> call, Response<WalletData> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        if (response.body().getData() != null) {
                            data = response.body().getData();
                            not_transactions.setVisibility(View.GONE);
                            wallet_data.setVisibility(View.VISIBLE);
//                        setWalletDetailsAdapterinLayout();
                            rightPoints = data.get(0).getRightPoint().toString();
                            wrongPoints = data.get(0).getWrongPoint().toString();
                            upploadPoints = data.get(0).getUploadedPoint().toString();
                            attendsPoint = data.get(0).getAttendedPoint().toString();
                            watchPoints = data.get(0).getWatchedPoint().toString();
                            balances = data.get(0).getTotal().toString();
                            referralPoints = data.get(0).getRefferPoint().toString();

                            if (balances.isEmpty()) {
                                balance.setText("0.0");
                            } else {
                                balance.setText(balances);
                            }

                            String display;
                            amount = Float.valueOf(balances);
                            if (appPreferences.getCountry().equals("India")) {
                                display = String.valueOf((amount / 10000) * 70);
                                balanceInRupees.setText("₹" + display);

                            } else {
                                display = String.valueOf((amount / 10000) * 1);
                                balanceInRupees.setText(display + "$");
                            }

                           availableBalance = Float.valueOf(display);

                            rght_count_tv.setText(rightPoints);
                            wrong_count_tv.setText(wrongPoints);
                            upload_count_tv.setText(upploadPoints);
                            attend_count_tv.setText(attendsPoint);
                            watch_count_tv.setText(watchPoints);
                            referral_count_tv.setText(referralPoints);
                        } else {
                            progressDialog.dismiss();
                            not_transactions.setVisibility(View.VISIBLE);
                            wallet_data.setVisibility(View.GONE);
                            balance.setText("0.0");

                        }
                    } else {
                        progressDialog.dismiss();
                        balance.setText("0.0");
                        not_transactions.setVisibility(View.VISIBLE);
                        wallet_data.setVisibility(View.GONE);
                    }
                } else {
                    progressDialog.dismiss();
                    balance.setText("0.0");
                    not_transactions.setVisibility(View.VISIBLE);
                    wallet_data.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<WalletData> call, Throwable t) {
                progressDialog.dismiss();
                not_transactions.setVisibility(View.VISIBLE);
                wallet_data.setVisibility(View.GONE);
            }
        });
    }

    private void setWalletDetailsAdapterinLayout() {
        walletDetailsListAdapter = new WalletDetailsListAdapter(getApplicationContext(), data);
        LinearLayoutManager linearHorizontal = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recycler_view.setLayoutManager(linearHorizontal);
        recycler_view.setAdapter(walletDetailsListAdapter);
        recycler_view.setHasFixedSize(true);
    }

    private void getUiInit() {
//        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        wallet_data = (LinearLayout) findViewById(R.id.wallet_data);
        back_arrow = (ImageView) findViewById(R.id.back_arrow);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        not_transactions = (TextView) findViewById(R.id.not_transactions);
        rght_count_tv = (TextView) findViewById(R.id.rght_count_tv);
        wrong_count_tv = (TextView) findViewById(R.id.wrong_count_tv);
        upload_count_tv = (TextView) findViewById(R.id.upload_count_tv);
        attend_count_tv = (TextView) findViewById(R.id.attend_count_tv);
        watch_count_tv = (TextView) findViewById(R.id.watch_count_tv);
        referral_count_tv = (TextView) findViewById(R.id.referral_count_tv);
        balance = (TextView) findViewById(R.id.balance);
        balanceInRupees = (TextView) findViewById(R.id.balanceInRupees);
        withdrawal_button = (Button) findViewById(R.id.withdrawal_button);
        upgrade_plan_button = (Button) findViewById(R.id.upgrade_plan_button);
    }
}