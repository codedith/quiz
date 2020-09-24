package com.xcrino.mcqproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xcrino.mcqproject.R;
import com.xcrino.mcqproject.ViewHolder.WithdrawalHistoryViewHolder;
import com.xcrino.mcqproject.models.UserTransaction;

import java.util.List;

public class WithdrawalHistoryListAdapter extends RecyclerView.Adapter<WithdrawalHistoryViewHolder> {

    private Context mContext;
    private List<UserTransaction> data1;
    private UserTransaction userTransaction;

    public WithdrawalHistoryListAdapter(Context mContext, List<UserTransaction> data1){
        this.mContext = mContext;
        this.data1 = data1;

    }

    @NonNull
    @Override
    public WithdrawalHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.withdrawal_historylist, parent, false);
        WithdrawalHistoryViewHolder withdrawalHistoryViewHolder = new WithdrawalHistoryViewHolder(view);
        return withdrawalHistoryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WithdrawalHistoryViewHolder holder, int position) {
        userTransaction = data1.get(position);
        holder.accountHolderName.setText("Account Holder Name: " + userTransaction.getRtAccountHolderName());
        holder.accountNumbers.setText("Account Number: " + userTransaction.getRtAccountNo());
        holder.amountInRuppes.setText("Amount Transacted: " + userTransaction.getRtAmt());
        if (userTransaction.getRtStatus().equals("0")){
            holder.refundStatus.setText("Status: Pending");
        }
        else if (userTransaction.getRtStatus().equals("1")){
            holder.refundStatus.setText("Status: Rejected");
        }
        else if (userTransaction.getRtStatus().equals("2")){
            holder.refundStatus.setText("Status: Paid");
        }
        else {
            holder.refundStatus.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return data1.size();
    }
}
