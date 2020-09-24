package com.xcrino.mcqproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.xcrino.mcqproject.R;
import com.xcrino.mcqproject.ViewHolder.CategoryViewHolder;
import com.xcrino.mcqproject.activity.AttendQuizActivity;
import com.xcrino.mcqproject.models.SubjectPosition;
import com.xcrino.mcqproject.utils.AppPreferences;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    Context mContext;
    private List<SubjectPosition> data = null;
    private SubjectPosition subjectPosition;
    private AppPreferences appPreferences;

    public CategoryAdapter(Context context, List<SubjectPosition> data) {
        this.mContext = context;
        this.data = data;
        appPreferences = new AppPreferences(mContext);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.grid_card, parent, false);
        CategoryViewHolder categoryViewHolder = new CategoryViewHolder(view);
        return categoryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, final int position) {
        subjectPosition = data.get(position);
        Picasso.with(mContext).load(subjectPosition.getImageIcon()).into(holder.category_image);
        holder.mCategory_text.setText(subjectPosition.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appPreferences.setCategoryId(data.get(position).getId());
                mContext.startActivity(new Intent(mContext, AttendQuizActivity.class).addFlags(FLAG_ACTIVITY_NEW_TASK));
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
