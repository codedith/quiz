package com.xcrino.mcqproject.activity;

import androidx.appcompat.app.AppCompatActivity;
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
import com.xcrino.mcqproject.adapter.UploadedQuestionAdapter;
import com.xcrino.mcqproject.appInterface.APIInterface;
import com.xcrino.mcqproject.models.UploadData;
import com.xcrino.mcqproject.models.UploadQuestion;
import com.xcrino.mcqproject.utils.APIClient;
import com.xcrino.mcqproject.utils.AppPreferences;
import com.xcrino.mcqproject.utils.NetworkUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionListActivity extends AppCompatActivity {

    private TextView toolbar_title;
    private ImageView back_arrow;

    private RecyclerView plan_list;
    private UploadedQuestionAdapter uploadedQuestionAdapter;
    private List<UploadData> data;
    private AppPreferences appPreferences;
    private String userId;
    private LinearLayout not_found;
    private Button publish_ques, upgrade_plan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        appPreferences = new AppPreferences(this);
        userId = appPreferences.getUserId();

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        back_arrow = (ImageView) findViewById(R.id.back_arrow);
        plan_list = (RecyclerView) findViewById(R.id.plan_list);
        not_found = (LinearLayout) findViewById(R.id.not_found);
        publish_ques = (Button) findViewById(R.id.publish_ques);
        upgrade_plan = (Button) findViewById(R.id.upgrade_plan);

        toolbar_title.setText("Question List");
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        publish_ques.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PublishQuestionsActivity.class);
                startActivity(intent);
            }
        });

        upgrade_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SubscriptionPlanActivity.class);
                startActivity(intent);
            }
        });

        if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
            getUploadedQuestionsList();
        } else {
            Toast.makeText(this, "Check your internet connection !", Toast.LENGTH_SHORT).show();
        }
    }

    private void getUploadedQuestionsList() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<UploadQuestion> responseCall = apiInterface.getUploadedQuestions(userId);
        responseCall.enqueue(new Callback<UploadQuestion>() {
            @Override
            public void onResponse(Call<UploadQuestion> call, Response<UploadQuestion> response) {
//                Log.i("result", response.body().toString());
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        data = response.body().getData();
                        not_found.setVisibility(View.GONE);
                        plan_list.setVisibility(View.VISIBLE);
                        setAdapterInRecyclerView();
                    } else {
                        progressDialog.dismiss();
                        not_found.setVisibility(View.VISIBLE);
                        plan_list.setVisibility(View.GONE);
                    }
                } else {
                    not_found.setVisibility(View.VISIBLE);
                    plan_list.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<UploadQuestion> call, Throwable t) {
                progressDialog.dismiss();
                not_found.setVisibility(View.VISIBLE);
                plan_list.setVisibility(View.GONE);

            }
        });
    }

    private void setAdapterInRecyclerView() {
        uploadedQuestionAdapter = new UploadedQuestionAdapter(getApplicationContext(), data);
        LinearLayoutManager linearHorizontal = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        plan_list.setLayoutManager(linearHorizontal);
        plan_list.setAdapter(uploadedQuestionAdapter);
        uploadedQuestionAdapter.getItemCount();

    }
}