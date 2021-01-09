package com.example.medica.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medica.Interface.ItemClickListener;
import com.example.medica.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtProductName, txtProductPrice, txtProductQuantity, productTotalPRice;
    private ItemClickListener itemClickListener;
    public CircleImageView pImage;
    public Button Remove, checkOut;



    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        txtProductName = itemView.findViewById(R.id.cart_product_name);
        txtProductPrice = itemView.findViewById(R.id.cart_product_price);
        txtProductQuantity = itemView.findViewById(R.id.cart_product_quantity);
        pImage = itemView.findViewById(R.id.cart_product_image);
        productTotalPRice = itemView.findViewById(R.id.cart_product_total);
        Remove = itemView.findViewById(R.id.remove);
        checkOut = itemView.findViewById(R.id.checkout);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
