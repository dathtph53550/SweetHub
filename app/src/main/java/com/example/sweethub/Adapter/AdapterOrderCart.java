package com.example.sweethub.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sweethub.Model.OrderCart;
import com.example.sweethub.R;

import java.util.ArrayList;

public class AdapterOrderCart extends RecyclerView.Adapter<AdapterOrderCart.OrderCartHolder> {

    Context context;
    ArrayList<String> names, prices, quantities;

    public AdapterOrderCart(Context context, ArrayList<String> names, ArrayList<String> prices, ArrayList<String> quantities) {
        this.context = context;
        this.names = names;
        this.prices = prices;
        this.quantities = quantities;
    }


    @NonNull
    @Override
    public OrderCartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderCartHolder(((Activity)context).getLayoutInflater().inflate(R.layout.item_order_product,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderCartHolder holder, int position) {
        holder.tvName.setText(names.get(position));
        holder.tvPrice.setText("Price: " + prices.get(position) + " đ");
        holder.tvQuantity.setText("Quantity: " + quantities.get(position));
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    class OrderCartHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvQuantity;
        public OrderCartHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
        }
    }
}
