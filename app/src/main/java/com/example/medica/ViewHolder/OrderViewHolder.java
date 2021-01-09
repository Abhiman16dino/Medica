package com.example.medica.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medica.Interface.ItemClickListener;
import com.example.medica.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView name_order , pName_orders , quantity_orders, amount_orders, date_orders, address_orders, phone_orders, orderStatus, productOrderID;
    private ItemClickListener itemClickListener;
    public CardView cardView;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        name_order = itemView.findViewById(R.id.name_order);
        pName_orders = itemView.findViewById(R.id.productname_order);
        quantity_orders = itemView.findViewById(R.id.productquantity_order);
        address_orders = itemView.findViewById(R.id.address_order);
        phone_orders = itemView.findViewById(R.id.phone_order);
        date_orders = itemView.findViewById(R.id.date_order);
        amount_orders = itemView.findViewById(R.id.amount_order);
        orderStatus = itemView.findViewById(R.id.orderstatus);
        cardView = itemView.findViewById(R.id.cardvieworder);
        productOrderID = itemView.findViewById(R.id.orderid);



    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);

    }
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
