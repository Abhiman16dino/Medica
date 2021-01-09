package com.example.medica.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medica.Interface.ItemClickListener;
import com.example.medica.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView Comments_Usr , Name_Usr;
    public ItemClickListener listener;
    public CircleImageView userImage;


    public CommentsViewHolder(@NonNull View itemView) {
        super(itemView);

        Comments_Usr = itemView.findViewById(R.id.comusr);
        Name_Usr = itemView.findViewById(R.id.nameusr);
        userImage = itemView.findViewById(R.id.commentsUserImage);
    }
    public void setItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v , getAdapterPosition(), false);
    }
}
