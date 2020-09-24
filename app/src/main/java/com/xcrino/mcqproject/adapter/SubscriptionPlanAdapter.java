package com.xcrino.mcqproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xcrino.mcqproject.R;
import com.xcrino.mcqproject.ViewHolder.SubscriptionPlanHolder;
import com.xcrino.mcqproject.activity.PaymentActivity;
import com.xcrino.mcqproject.models.Datum;
import com.xcrino.mcqproject.models.SubcriptionPlan;
import com.xcrino.mcqproject.models.UserSubscription;
import com.xcrino.mcqproject.utils.AppPreferences;

import java.util.List;

public class SubscriptionPlanAdapter extends RecyclerView.Adapter<SubscriptionPlanHolder> {

    private AppPreferences appPreferences;

    private Context context;
    private List<SubcriptionPlan> data;
    private UserSubscription userSubscription;
    private SubcriptionPlan subcriptionPlan;
    private int[] textureArrayWin = {R.drawable.card_first, R.drawable.card_second, R.drawable.card_third, R.drawable.card_faur, R.drawable.card_five};
    private int[] buttoncolor = {R.color.subscription_bg_f, R.color.subscription_bg_sec, R.color.subscription_bg_th, R.color.subscription_bg_faurth, R.color.subscription_bg_five};
    String amount;

    public SubscriptionPlanAdapter(Context context, List<SubcriptionPlan> data) {
        this.context = context;
        this.data = data;
        appPreferences = new AppPreferences(context);
        userSubscription = UserSubscription.userSubscription();

    }

    @NonNull
    @Override
    public SubscriptionPlanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_subscription_plan, parent, false);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.bottomupanimation);
        view.startAnimation(animation);
        return new SubscriptionPlanHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubscriptionPlanHolder holder, final int position) {
        subcriptionPlan = data.get(position);
        holder.back_colore.setBackgroundResource(textureArrayWin[position]);
        if (appPreferences.getCountry().equals("India")) {
            holder.amount_inr.setText(subcriptionPlan.getAmountInr());
            amount = subcriptionPlan.getAmountInr();
            holder.rupees_dollar.setText("₹");
            if (subcriptionPlan.getEarning().equals("Unlimited")) {
                holder.line_four.setText("Earnings max : " + subcriptionPlan.getEarning().toString());
            }
            else {
                holder.line_four.setText("Earnings max : " + subcriptionPlan.getEarning().toString() + " INR");
            }
        } else {
            holder.amount_inr.setText(subcriptionPlan.getAmountUsd());
            amount = subcriptionPlan.getAmountUsd();
            holder.rupees_dollar.setText("$");
            if (subcriptionPlan.getEarning().equals("Unlimited")) {
                holder.line_four.setText("Earnings max : " + subcriptionPlan.getEarning().toString());
            }
            else {
                float amount = (Float.valueOf(subcriptionPlan.getEarning().toString())) / 70;
                int roundOfAmount = Math.round(amount);
                holder.line_four.setText("Earnings max : " + Integer.toString(roundOfAmount) + " USD");
            }
        }

        if (appPreferences.getSubStatus()) {
            if (userSubscription.getData().get(0).getStatus().equals("1") &&
                    userSubscription.getData().get(0).getPackageId().equals(data.get(position).getId())) {
                holder.bt_choose_plan.setText("Subscribed");
                holder.bt_choose_plan.setClickable(false);
                holder.bt_choose_plan.setEnabled(false);
                appPreferences.saveSubStatus(true);
            } else {
                holder.bt_choose_plan.setText("Upgrade Now");
                holder.bt_choose_plan.setClickable(true);
                holder.bt_choose_plan.setEnabled(true);
                appPreferences.saveSubStatus(true);
            }
        }

        holder.bt_choose_plan.setBackgroundColor(buttoncolor[position]);
        holder.line_one.setText("Validity : " + subcriptionPlan.getValidity().toString() + " days");
        holder.line_two.setText(subcriptionPlan.getUserResponse().toString() + " User Responses");
        holder.line_three.setText("No. of questions can be uploaded : " + subcriptionPlan.getQuantity().toString());
//        holder.line_four.setText("Earnings max : " + subcriptionPlan.getEarning().toString());
        holder.bt_choose_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appPreferences.getCountry().equals("India")) {
                    amount = data.get(position).getAmountInr();
                    appPreferences.saveCurrencyCode("INR");
                } else {
                    amount = data.get(position).getAmountUsd();
                    appPreferences.saveCurrencyCode("USD");
                }
                appPreferences.saveAmount(amount);
                appPreferences.saveSubscriptionId(data.get(position).getId());
                context.startActivity(new Intent(context, PaymentActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}