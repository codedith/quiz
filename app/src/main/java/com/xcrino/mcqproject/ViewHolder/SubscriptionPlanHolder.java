package com.xcrino.mcqproject.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xcrino.mcqproject.R;

public class SubscriptionPlanHolder extends RecyclerView.ViewHolder {

    public TextView amount_inr, line_one, line_two, line_three, line_four, rupees_dollar;
    public LinearLayout back_colore;
    public Button bt_choose_plan;

    public SubscriptionPlanHolder(@NonNull View itemView) {
        super(itemView);

        back_colore = (LinearLayout) itemView.findViewById(R.id.back_colore);
        amount_inr = (TextView) itemView.findViewById(R.id.amount_inr);
        bt_choose_plan = (Button) itemView.findViewById(R.id.bt_choose_plan);
        line_one = (TextView) itemView.findViewById(R.id.line_one);
        line_two = (TextView) itemView.findViewById(R.id.line_two);
        line_three = (TextView) itemView.findViewById(R.id.line_three);
        line_four = (TextView) itemView.findViewById(R.id.line_four);
        rupees_dollar = (TextView) itemView.findViewById(R.id.rupees_dollar);
    }
}
