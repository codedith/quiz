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
import com.xcrino.mcqproject.ViewHolder.UploadQuestionViewHolder;
import com.xcrino.mcqproject.models.UploadData;

import java.util.List;

public class UploadedQuestionAdapter extends RecyclerView.Adapter<UploadQuestionViewHolder> {

    private Context context;
    private List<UploadData> data;
    UploadData uploadData;

    public UploadedQuestionAdapter(Context context, List<UploadData> data) {
        this.context = context;
        this.data = data;

    }

    @NonNull
    @Override
    public UploadQuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upload_question_layout, parent, false);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.bottomupanimation);
        view.startAnimation(animation);
        return new UploadQuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UploadQuestionViewHolder holder, int position) {
        uploadData = data.get(position);
        holder.questio_name.setText(uploadData.getQuestion());
        holder.time_tv.setText(uploadData.getAddDate().substring(0, 10));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
