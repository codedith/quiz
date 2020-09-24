package com.xcrino.mcqproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xcrino.mcqproject.R;
import com.xcrino.mcqproject.ViewHolder.WalletDeatailsViewHolder;
import com.xcrino.mcqproject.models.Data;
import com.xcrino.mcqproject.models.WalletData;

import java.util.List;

public class WalletDetailsListAdapter extends RecyclerView.Adapter<WalletDeatailsViewHolder> {

    private Context context;
    private List<Data> data1;
    private Data walletData;

    public WalletDetailsListAdapter(Context context, List<Data> data1){
        this.context = context;
        this.data1 = data1;
    }


    @NonNull
    @Override
    public WalletDeatailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet_card_list, parent, false);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.bottomupanimation);
        view.startAnimation(animation);
        return new WalletDeatailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WalletDeatailsViewHolder holder, int position) {
        walletData = data1.get(position);
        holder.msg_count_tv.setText("₹"+walletData.getAttendedPoint());

    }

    @Override
    public int getItemCount() {
        return data1.size();
    }
}
