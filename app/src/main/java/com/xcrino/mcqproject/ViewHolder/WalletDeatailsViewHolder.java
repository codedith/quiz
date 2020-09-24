package com.xcrino.mcqproject.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xcrino.mcqproject.R;

public class WalletDeatailsViewHolder extends RecyclerView.ViewHolder {

    public TextView msg_count_tv, watch_tv;

    public WalletDeatailsViewHolder(@NonNull View itemView) {
        super(itemView);
        msg_count_tv = (TextView) itemView.findViewById(R.id.msg_count_tv);
        watch_tv = (TextView) itemView.findViewById(R.id.watch_tv);
    }
}
