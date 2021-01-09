package com.example.medica.ViewHolder;

import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medica.Interface.ItemClickListener;
import com.example.medica.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView pName, pDesc, pPrice;
   public Button bNow;
   public ImageView pImage;
    public ItemClickListener listener;


    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        pDesc = itemView.findViewById(R.id.pDesc);
        pName = itemView.findViewById(R.id.pName);
        pPrice = itemView.findViewById(R.id.pPrice);
        bNow = itemView.findViewById(R.id.bNow);
        pImage = itemView.findViewById(R.id.pImage);



    }

    public void setItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }


    @Override
    public void onClick(View v) {

        listener.onClick(v , getAdapterPosition(), false);

    }
}
