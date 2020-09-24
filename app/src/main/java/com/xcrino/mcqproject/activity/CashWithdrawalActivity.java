package com.xcrino.mcqproject.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.xcrino.mcqproject.R;
import com.xcrino.mcqproject.adapter.AccountAddedListAdapter;
import com.xcrino.mcqproject.adapter.WithdrawalHistoryListAdapter;
import com.xcrino.mcqproject.appInterface.APIInterface;
import com.xcrino.mcqproject.models.AccountTransaction;
import com.xcrino.mcqproject.models.AmountWithdral;
import com.xcrino.mcqproject.models.SaveAccountList;
import com.xcrino.mcqproject.models.TransactionList;
import com.xcrino.mcqproject.models.UserTransaction;
import com.xcrino.mcqproject.utils.APIClient;
import com.xcrino.mcqproject.utils.AppPreferences;
import com.xcrino.mcqproject.utils.NetworkUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CashWithdrawalActivity extends AppCompatActivity {

    private AppPreferences appPreferences;

    private TextView toolbar_title, not_transactions_list, not_withdrawal_list;
    private ImageView back_arrow;
    private CardView add_NewAccount, add_new_TransferAccount;
    private TextInputEditText account_holderName, account_Number, ifsc_Code, branch_Address;
    TextView amountFloat;
    private Button submit_Transaction;
    private RecyclerView recycler_view, withdrawal_recycler_view;
    private LinearLayout accounts_added_list, withdrawal_history_list;

    private String accountHolderName, accountNumber, ifsCode, amountRuppes, branchAddress;
    private String userId;

    private Context mContext;
    private AccountAddedListAdapter cashWithdrawalAdapter;
    private WithdrawalHistoryListAdapter withdrawalHistoryListAdapter;
    private TransactionList transactionList;
    private List<SaveAccountList> data;
    private AccountTransaction accountTransaction;
    private List<UserTransaction> data1;
    private UserTransaction userTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_withdrawal);

        getUiInit();

        mContext = this;
        appPreferences = new AppPreferences(mContext);
        userId = appPreferences.getUserId();

        toolbar_title.setText("Cash Withdrawal");

        if (NetworkUtil.isNetworkAvailable(mContext)) {
            getAccountHistory();
            getWthidrawalHistory();
        } else {
            Toast.makeText(mContext, "Something went wrong! It may be due to network issues", Toast.LENGTH_SHORT).show();
        }

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, WalletActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
//                onBackPressed();
                finish();
            }
        });

        add_NewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appPreferences.getTransactionStatus().equals("0")){
                    Toast.makeText(mContext, "You can not initiate another request for withdraw until your existing request is been cleared", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (add_new_TransferAccount.getVisibility() == View.VISIBLE) {
                        accounts_added_list.setVisibility(View.VISIBLE);
                        withdrawal_history_list.setVisibility(View.VISIBLE);
                        add_new_TransferAccount.setVisibility(View.GONE);
                    } else {
                        accounts_added_list.setVisibility(View.GONE);
                        withdrawal_history_list.setVisibility(View.GONE);
                        add_new_TransferAccount.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        submit_Transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (checkValid()) {
                        if (NetworkUtil.isNetworkAvailable(mContext)) {
                            cashWithdrawalProcess();
                        } else {
                            Toast.makeText(mContext, "Something went wrong! It may be due to network issues", Toast.LENGTH_SHORT).show();
                        }
                    }

            }
        });

        amountFloat.setText("Amount to be withdrawn: " + appPreferences.getAvailableBalance().toString());
    }

    private void getWthidrawalHistory() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<AccountTransaction> accountTransactionCall = apiInterface.getUserTransaction(userId);

        accountTransactionCall.enqueue(new Callback<AccountTransaction>() {
            @Override
            public void onResponse(Call<AccountTransaction> call, Response<AccountTransaction> response) {
                if (response.isSuccessful()) {
                    if (progressDialog.isShowing()) {
                        progressDialog.cancel();
                    }
                    if (response.body() != null) {
                        data1 = response.body().getData();
                        userTransaction = data1.get(0);
                        not_withdrawal_list.setVisibility(View.GONE);
                        setCashWithdrawalAdapterinLayout();

                        appPreferences.setTransactionStatus(userTransaction.getRtStatus());

                    } else {
                        not_withdrawal_list.setVisibility(View.VISIBLE);

                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(mContext, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AccountTransaction> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getAccountHistory() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<TransactionList> transactionListCall = apiInterface.getSaveAccountList(userId);
        transactionListCall.enqueue(new Callback<TransactionList>() {
            @Override
            public void onResponse(Call<TransactionList> call, Response<TransactionList> response) {
                if (response.isSuccessful()) {
                    if (progressDialog.isShowing()) {
                        progressDialog.cancel();
                    }
                    if (response.body() != null) {
                        data = response.body().getData();
                        not_transactions_list.setVisibility(View.GONE);
                        setAccountAddedAdapterinLayout();

                    } else {
                        progressDialog.dismiss();
                        not_transactions_list.setVisibility(View.VISIBLE);
                        recycler_view.setVisibility(View.GONE);

                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(mContext, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TransactionList> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void cashWithdrawalProcess() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<AmountWithdral> amountWithdralCall = apiInterface.getSaveAccountWithdrawal(userId, accountNumber, accountHolderName,
                ifsCode, branchAddress, appPreferences.getAvailableBalance().toString());

        amountWithdralCall.enqueue(new Callback<AmountWithdral>() {
            @Override
            public void onResponse(Call<AmountWithdral> call, Response<AmountWithdral> response) {
                if (response.isSuccessful()) {
                    if (progressDialog.isShowing()) {
                        progressDialog.cancel();
                    }
                    if (response.body() != null) {
//                        Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                        alertDialogBuilder.setCancelable(false);
                        if (appPreferences.getCountry().equals("India")){
                            alertDialogBuilder.setMessage("You will get your balance within 30 working days after verification, minimum amount withdraw is 500 INR");
                        }
                        else {
                            alertDialogBuilder.setMessage("You will get your balance within 30 working days after verification, minimum amount withdraw is 100 USD");
                        }
                        alertDialogBuilder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {

                                    }
                                });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(mContext, "Something went wrong !", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(mContext, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AmountWithdral> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void setAccountAddedAdapterinLayout() {
        cashWithdrawalAdapter = new AccountAddedListAdapter(getApplicationContext(), data);
        LinearLayoutManager linearHorizontal = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recycler_view.setLayoutManager(linearHorizontal);
        recycler_view.setAdapter(cashWithdrawalAdapter);
        recycler_view.setHasFixedSize(true);

    }


    private void setCashWithdrawalAdapterinLayout() {
        withdrawalHistoryListAdapter = new WithdrawalHistoryListAdapter(getApplicationContext(), data1);
        LinearLayoutManager linearHorizontal = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        withdrawal_recycler_view.setLayoutManager(linearHorizontal);
        withdrawal_recycler_view.setAdapter(withdrawalHistoryListAdapter);
        withdrawal_recycler_view.setHasFixedSize(true);
    }

    private boolean checkValid() {
        boolean valid = true;

        accountHolderName = account_holderName.getText().toString().trim();
        accountNumber = account_Number.getText().toString().trim();
        ifsCode = ifsc_Code.toString().trim();
        amountRuppes = amountFloat.getText().toString().trim();
        branchAddress = branch_Address.getText().toString().trim();

        if (TextUtils.isEmpty(accountHolderName)) {
            account_holderName.setError("Enter A/C Holder Name");
            account_holderName.requestFocus();
            valid = false;

        } else if (TextUtils.isEmpty(accountNumber)) {
            account_Number.setError("Enter A/C Number");
            account_Number.requestFocus();
            valid = false;

        } else if (TextUtils.isEmpty(ifsCode)) {
            ifsc_Code.setError("Enter IFS Code");
            ifsc_Code.requestFocus();
            valid = false;

        } else if (TextUtils.isEmpty(amountRuppes)) {
            amountFloat.setError("Enter Branch Name");
            amountFloat.requestFocus();
            valid = false;

        } else if (TextUtils.isEmpty(branchAddress)) {
            branch_Address.setError("Enter Branch Address");
            branch_Address.requestFocus();
            valid = false;
        }

        return valid;
    }

    private void getUiInit() {
        back_arrow = (ImageView) findViewById(R.id.back_arrow);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        not_transactions_list = (TextView) findViewById(R.id.not_transactions_list);
        not_withdrawal_list = (TextView) findViewById(R.id.not_withdrawal_list);
        add_NewAccount = (CardView) findViewById(R.id.add_NewAccount);
        add_new_TransferAccount = (CardView) findViewById(R.id.add_new_TransferAccount);
        submit_Transaction = (Button) findViewById(R.id.submit_Transaction);
        account_holderName = (TextInputEditText) findViewById(R.id.account_holderName);
        account_Number = (TextInputEditText) findViewById(R.id.account_Number);
        ifsc_Code = (TextInputEditText) findViewById(R.id.ifsc_Code);
        amountFloat = (TextView) findViewById(R.id.amountFloat);
        branch_Address = (TextInputEditText) findViewById(R.id.branch_Address);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        withdrawal_recycler_view = (RecyclerView) findViewById(R.id.withdrawal_recycler_view);
        accounts_added_list = (LinearLayout) findViewById(R.id.accounts_added_list);
        withdrawal_history_list = (LinearLayout) findViewById(R.id.withdrawal_history_list);
    }
}
