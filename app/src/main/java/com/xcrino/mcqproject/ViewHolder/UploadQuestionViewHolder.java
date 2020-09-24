package com.xcrino.mcqproject.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xcrino.mcqproject.R;

public class UploadQuestionViewHolder extends RecyclerView.ViewHolder {
    public TextView questio_name, time_tv;

    public UploadQuestionViewHolder(@NonNull View itemView) {
        super(itemView);

        questio_name = (TextView) itemView.findViewById(R.id.questio_name);
        time_tv = (TextView) itemView.findViewById(R.id.time_tv);

    }
}
