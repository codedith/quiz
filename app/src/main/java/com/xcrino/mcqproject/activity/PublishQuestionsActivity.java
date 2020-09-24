package com.xcrino.mcqproject.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.xcrino.mcqproject.R;
import com.xcrino.mcqproject.appInterface.APIInterface;
import com.xcrino.mcqproject.appInterface.SpinnerInterface;
import com.xcrino.mcqproject.models.AddNewQuestion;
import com.xcrino.mcqproject.models.NewQuestions;
import com.xcrino.mcqproject.models.SubjectPosition;
import com.xcrino.mcqproject.utils.APIClient;
import com.xcrino.mcqproject.utils.AppPreferences;
import com.xcrino.mcqproject.utils.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class PublishQuestionsActivity extends AppCompatActivity {

    private TextView toolbar_title;
    private ImageView back_arrow;

    private Spinner spinner;
    private EditText question_editText, option_a_answer, option_b_answer, option_c_answer, option_d_answer, explanationEditText;
    private RadioButton radio_A, radio_B, radio_C, radio_D;
    private RadioGroup radio_group;
    private Button history_button, submit_button;
    private TextView entitlement;

    private String userId, subject1, questions, qAnswerfirst, qAnswerSecond, qAnswerThird, qAnswerFourth, selectedAnswers = "Select", qExplanation;

    private NewQuestions newQuestions;
    private SubjectPosition subjectPosition;
    private List<SubjectPosition> data = null;
    private ArrayList<String> playerNames = new ArrayList<String>();

    private AppPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_questions);

        appPreferences = new AppPreferences(this);
        userId = appPreferences.getUserId();

        getUiIntit();

        if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
            getSubjectPosition();
        } else {
            Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
        }

        toolbar_title.setText("Publish Question");
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        history_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QuestionListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getDataByUser()){
                    if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
                        uploadQuestions();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Check Internet Connection !", Toast.LENGTH_SHORT).show();
                    }
                }
//                if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
//                    if (getDataByUser()) {
//                        uploadQuestions();
//                    }
//                    else {
//                        Toast.makeText(getApplicationContext(), "Something went wrong !", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(getApplicationContext(), "Check Internet Connection !", Toast.LENGTH_SHORT).show();
//                }
            }
        });

        radio_group.clearCheck();
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int options = radio_group.getCheckedRadioButtonId();
                switch (options) {
                    case R.id.radio_A:
                        if (radio_A.isChecked()) {
                            selectedAnswers = option_a_answer.getText().toString();
                        }
                        break;

                    case R.id.radio_B:
                        if (radio_B.isChecked()) {
                            selectedAnswers = option_b_answer.getText().toString();
                        }
                        break;

                    case R.id.radio_C:
                        if (radio_C.isChecked()) {
                            selectedAnswers = option_c_answer.getText().toString();
                        }
                        break;

                    case R.id.radio_D:
                        if (radio_D.isChecked()){
                            selectedAnswers = option_d_answer.getText().toString();
                        }
                        break;
                }
            }
        });
    }

    private void uploadQuestions() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        newQuestions = new NewQuestions(userId, subject1, questions, qAnswerfirst, qAnswerSecond, qAnswerThird, qAnswerFourth, selectedAnswers, qExplanation);
        Call<AddNewQuestion> newQuestionCall = apiInterface.addNewQuestionByUser(newQuestions.getUsersid(), newQuestions.getSubject(), newQuestions.getQuestion(), newQuestions.getOption1(),
                newQuestions.getOption2(), newQuestions.getOption3(), newQuestions.getOption4(), newQuestions.getAnswer(), newQuestions.getDescription());

        newQuestionCall.enqueue(new Callback<AddNewQuestion>() {
            @Override
            public void onResponse(Call<AddNewQuestion> call, Response<AddNewQuestion> response) {
//                Log.i("result", response.body().toString());
                if (response.isSuccessful()) {

                    if(response.body() != null){

                        if (response.body().getSuccess()){
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            clearDataByUser();
                        }
                        else {
                            progressDialog.dismiss();
                            startActivity(new Intent(getApplicationContext(), SubscriptionPlanActivity.class));
                            clearDataByUser();
                        }

                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddNewQuestion> call, Throwable t) {
                t.getMessage();
            }
        });

    }

    private void clearDataByUser() {
        question_editText.getText().clear();
        option_a_answer.getText().clear();
        option_b_answer.getText().clear();
        option_c_answer.getText().clear();
        option_d_answer.getText().clear();
        explanationEditText.getText().clear();
        radio_group.clearCheck();
    }

    private boolean getDataByUser() {
        boolean valid = true;
        questions = question_editText.getText().toString().trim();
        qAnswerfirst = option_a_answer.getText().toString().trim();
        qAnswerSecond = option_b_answer.getText().toString().trim();
        qAnswerThird = option_c_answer.getText().toString().trim();
        qAnswerFourth = option_d_answer.getText().toString().trim();
        qExplanation = explanationEditText.getText().toString().trim();

        if (questions.isEmpty()) {
            question_editText.setError("Enter Your Question");
            question_editText.requestFocus();
            valid = false;
        }

        else if (qAnswerfirst.isEmpty()) {
            option_a_answer.setError("Enter first option");
            option_a_answer.requestFocus();
            valid = false;
        }

        else if (qAnswerSecond.isEmpty()) {
            option_b_answer.setError("Enter second option");
            option_b_answer.requestFocus();
            valid = false;
        }

        else if (qAnswerThird.isEmpty()) {
            option_c_answer.setError("Enter third option");
            option_c_answer.requestFocus();
            valid = false;
        }

        else if (qAnswerFourth.isEmpty()) {
            option_d_answer.setError("Enter fourth option");
            option_d_answer.requestFocus();
            valid = false;
        }

        else if (selectedAnswers.equals("Select")) {
            Toast.makeText(this, "Select the correct answer option", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }

    private void getSubjectPosition() {
//        final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
//        progressDialog.setTitle("Please wait...");
//        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(SpinnerInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create()).build();

        SpinnerInterface api = retrofit.create(SpinnerInterface.class);
        Call<String> call = api.getSubjectList();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
//                Log.i("result", response.body().toString());
                if (response.isSuccessful()) {
//                    progressDialog.dismiss();
                    if (response.body() != null) {
                        String jsonresponse = response.body().toString();
                        spinJSON(jsonresponse);

                    } else {
                        Toast.makeText(getApplicationContext(), "Something went wrong !", Toast.LENGTH_SHORT).show();
                    }
                } else {
//                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
//                progressDialog.dismiss();
                t.getMessage();

            }
        });

    }

    private void spinJSON(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            if (obj.optString("success").equals("true")) {
                data = new ArrayList<>();
                JSONArray dataArray = obj.getJSONArray("data");

                for (int i = 0; i < dataArray.length(); i++) {
                    subjectPosition = new SubjectPosition();
                    JSONObject dataobj = dataArray.getJSONObject(i);
                    subjectPosition.setId(dataobj.getString("id"));
                    subjectPosition.setName(dataobj.getString("name"));
                    data.add(subjectPosition);

                }
                for (int i = 0; i < data.size(); i++) {
                    playerNames.add(data.get(i).getName().toString());
                }

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, playerNames);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                spinner.setAdapter(spinnerArrayAdapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position >= 0) {
                            String subject11 = data.get(position).getName();
                            subject1 = subject11;
                            subject1 = (String) parent.getItemAtPosition(position);
                            ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#ffffff"));

                        } else {
                            Toast.makeText(getApplicationContext(), "Something went wrong !", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getUiIntit() {
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        entitlement = (TextView) findViewById(R.id.entitlement);
        back_arrow = (ImageView) findViewById(R.id.back_arrow);
        spinner = (Spinner) findViewById(R.id.spinner);
        question_editText = (EditText) findViewById(R.id.question_editText);
        option_a_answer = (EditText) findViewById(R.id.option_a_answer);
        option_b_answer = (EditText) findViewById(R.id.option_b_answer);
        option_c_answer = (EditText) findViewById(R.id.option_c_answer);
        option_d_answer = (EditText) findViewById(R.id.option_d_answer);
        explanationEditText = (EditText) findViewById(R.id.explanationEditText);
        radio_group = (RadioGroup) findViewById(R.id.radio_group);
        radio_A = (RadioButton) findViewById(R.id.radio_A);
        radio_B = (RadioButton) findViewById(R.id.radio_B);
        radio_C = (RadioButton) findViewById(R.id.radio_C);
        radio_D = (RadioButton) findViewById(R.id.radio_D);
        radio_D = (RadioButton) findViewById(R.id.radio_D);
        history_button = (Button) findViewById(R.id.history_button);
        submit_button = (Button) findViewById(R.id.submit_button);


    }
}
