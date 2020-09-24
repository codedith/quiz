package com.xcrino.mcqproject.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xcrino.mcqproject.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    public TextView mCategory_text;
    public ImageView category_image;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        category_image = (ImageView) itemView.findViewById(R.id.category_image);
        mCategory_text = itemView.findViewById(R.id.category_text);
    }
}
