package com.xcrino.mcqproject.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xcrino.mcqproject.R;

public class WithdrawalHistoryViewHolder extends RecyclerView.ViewHolder {

    public TextView accountHolderName, accountNumbers, amountInRuppes, refundStatus;

    public WithdrawalHistoryViewHolder(@NonNull View itemView) {
        super(itemView);

        accountHolderName = (TextView) itemView.findViewById(R.id.accountHolderName);
        accountNumbers = (TextView) itemView.findViewById(R.id.accountNumbers);
        amountInRuppes = (TextView) itemView.findViewById(R.id.amountInRuppes);
        refundStatus = (TextView) itemView.findViewById(R.id.refundStatus);
    }
}
