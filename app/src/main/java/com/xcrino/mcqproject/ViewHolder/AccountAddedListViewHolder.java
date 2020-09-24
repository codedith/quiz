package com.xcrino.mcqproject.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xcrino.mcqproject.R;

public class AccountAddedListViewHolder extends RecyclerView.ViewHolder {

    public TextView accountHolderName, accountNumbers;

    public AccountAddedListViewHolder(@NonNull View itemView) {
        super(itemView);

        accountHolderName = (TextView) itemView.findViewById(R.id.accountHolderName);
        accountNumbers = (TextView) itemView.findViewById(R.id.accountNumbers);

    }
}
