package com.xcrino.mcqproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xcrino.mcqproject.R;
import com.xcrino.mcqproject.adapter.SubscriptionPlanAdapter;
import com.xcrino.mcqproject.appInterface.APIInterface;
import com.xcrino.mcqproject.models.Datum;
import com.xcrino.mcqproject.models.SubcriptionPlan;
import com.xcrino.mcqproject.models.Subscription;
import com.xcrino.mcqproject.models.UserSubscription;
import com.xcrino.mcqproject.utils.APIClient;
import com.xcrino.mcqproject.utils.AppPreferences;
import com.xcrino.mcqproject.utils.NetworkUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscriptionPlanActivity extends AppCompatActivity {

    private AppPreferences appPreferences;

    private TextView toolbar_title, data_not_found;
    private ImageView back_arrow;

    private RecyclerView plan_list;
    private List<SubcriptionPlan> data;
    private SubcriptionPlan subcriptionPlan;
    private SubscriptionPlanAdapter subcriptionPlanAdapter;
    private List<Datum> datumList;
    private UserSubscription userSubscription;

    private String userId;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_plan);

        appPreferences = new AppPreferences(this);
        userId = appPreferences.getUserId();
        userSubscription = UserSubscription.userSubscription();
        mContext = this;

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        data_not_found = (TextView) findViewById(R.id.data_not_found);
        back_arrow = (ImageView) findViewById(R.id.back_arrow);
        plan_list = (RecyclerView) findViewById(R.id.plan_list);

        toolbar_title.setText("Subscription Plan");
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        getSubscribedPlanByUser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
            getUserSubscription();
            getSubscribedPlanByUser();
        } else {
            Toast.makeText(this, "Check Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    private void getSubscribedPlanByUser() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<Subscription> subscriptionCall = apiInterface.getSubscriptionValue();

        subscriptionCall.enqueue(new Callback<Subscription>() {
            @Override
            public void onResponse(Call<Subscription> call, Response<Subscription> response) {
//                Log.i("results", response.body().toString());
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        data = response.body().getData();
                        plan_list.setVisibility(View.VISIBLE);
                        data_not_found.setVisibility(View.GONE);
                        setDataAdapterinLayout();
                    } else {
                        progressDialog.dismiss();
                        plan_list.setVisibility(View.GONE);
                        data_not_found.setVisibility(View.VISIBLE);
                    }
                } else {
                    progressDialog.dismiss();
                    plan_list.setVisibility(View.GONE);
                    data_not_found.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Subscription> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(SubscriptionPlanActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setDataAdapterinLayout() {
        subcriptionPlanAdapter = new SubscriptionPlanAdapter(mContext, data);
        LinearLayoutManager linearHorizontal = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        plan_list.setLayoutManager(linearHorizontal);
        plan_list.setAdapter(subcriptionPlanAdapter);
        plan_list.setHasFixedSize(true);
    }

    private void getUserSubscription() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<UserSubscription> userSubscriptionCall = apiInterface.get_user_subscription(userId);
        userSubscriptionCall.enqueue(new Callback<UserSubscription>() {
            @Override
            public void onResponse(Call<UserSubscription> call, Response<UserSubscription> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        progressDialog.dismiss();
//                        plan_list.setVisibility(View.VISIBLE);
//                        data_not_found.setVisibility(View.GONE);
                        userSubscription.setData(response.body().getData());
                        if (userSubscription.getData().get(0).getStatus().equals("1")) {
                            appPreferences.saveSubStatus(true);
                        } else {
                            appPreferences.saveSubStatus(false);
                        }
                        getSubscribedPlanByUser();
                    } else {
                        progressDialog.dismiss();
//                        plan_list.setVisibility(View.GONE);
//                        data_not_found.setVisibility(View.VISIBLE);

                    }
                } else {
                    progressDialog.dismiss();
//                    plan_list.setVisibility(View.GONE);
//                    data_not_found.setVisibility(View.VISIBLE);
                    appPreferences.saveSubStatus(false);
                    getSubscribedPlanByUser();
                }
            }

            @Override
            public void onFailure(Call<UserSubscription> call, Throwable t) {
                progressDialog.dismiss();
//                plan_list.setVisibility(View.GONE);
//                data_not_found.setVisibility(View.VISIBLE);
            }
        });
    }
}
