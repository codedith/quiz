package com.xcrino.mcqproject.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xcrino.mcqproject.R;
import com.xcrino.mcqproject.ViewHolder.AccountAddedListViewHolder;
import com.xcrino.mcqproject.appInterface.APIInterface;
import com.xcrino.mcqproject.models.AmountWithdral;
import com.xcrino.mcqproject.models.SaveAccountList;
import com.xcrino.mcqproject.utils.APIClient;
import com.xcrino.mcqproject.utils.AppPreferences;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountAddedListAdapter extends RecyclerView.Adapter<AccountAddedListViewHolder> {

    private Context mContext;
    private AppPreferences appPreferences;
    private List<SaveAccountList> data;
    private SaveAccountList saveAccountList;

    public AccountAddedListAdapter(Context mContext, List<SaveAccountList> data) {
        this.mContext = mContext;
        appPreferences = new AppPreferences(mContext);
        this.data = data;

    }

    @NonNull
    @Override
    public AccountAddedListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cashwithdrawal_layout, parent, false);
        AccountAddedListViewHolder cashWithdrawalViewHolder = new AccountAddedListViewHolder(view);
        return cashWithdrawalViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AccountAddedListViewHolder holder, final int position) {
        saveAccountList = data.get(position);
        holder.accountHolderName.setText("Account holder Name: " + saveAccountList.getSadAccountHolderName());
        holder.accountNumbers.setText("Account Number: " + saveAccountList.getSadAccountNo());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appPreferences.getTransactionStatus().equals("0")){
                    Toast.makeText(mContext, "You can not initiate another request for withdraw until your existing request is been cleared", Toast.LENGTH_SHORT).show();
                }
                else {
                    final ProgressDialog progressDialog = new ProgressDialog(mContext);
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();

                    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
                    Call<AmountWithdral> amountWithdralCall = apiInterface.getSaveAccountWithdrawal(appPreferences.getUserId(),
                            data.get(position).getSadAccountNo(), data.get(position).getSadAccountHolderName(),
                            data.get(position).getSadIfsc(), data.get(position).getSadBranch(),
                            appPreferences.getAvailableBalance().toString());

                    amountWithdralCall.enqueue(new Callback<AmountWithdral>() {
                        @Override
                        public void onResponse(Call<AmountWithdral> call, Response<AmountWithdral> response) {
                            if (response.isSuccessful()) {
                                if (progressDialog.isShowing()) {
                                    progressDialog.cancel();
                                }
                                if (response.body() != null) {
                                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}