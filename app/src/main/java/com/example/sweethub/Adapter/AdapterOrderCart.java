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
    ArrayList<OrderCart> list;

    public AdapterOrderCart(Context context, ArrayList<OrderCart> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public OrderCartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderCartHolder(((Activity)context).getLayoutInflater().inflate(R.layout.item_order_product,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderCartHolder holder, int position) {
        OrderCart orderCart = list.get(position);

        // Kết hợp tất cả các thông tin vào một chuỗi
        StringBuilder productNames = new StringBuilder();
        StringBuilder productPrices = new StringBuilder();
        StringBuilder productQuantities = new StringBuilder();
        Double tongTien = 0.0;

        // Duyệt qua tất cả các sản phẩm trong danh sách orderCart
        for (int i = 0; i < orderCart.getName().size(); i++) {
            tongTien += Double.parseDouble(orderCart.getPrice().get(i))  * Integer.parseInt(orderCart.getQuantity().get(i)) ;
            productNames.append(orderCart.getName().get(i)).append("\n");
            productPrices.append(orderCart.getPrice().get(i)).append(" đ\n");
            productQuantities.append(orderCart.getQuantity().get(i)).append("\n");

        }

        // Hiển thị tất cả các thông tin trong TextView
        holder.tvName.setText("Name \n" + productNames);
        holder.tvPrice.setText("Price \n" + productPrices);
        holder.tvQuantity.setText("Quantity \n" + productQuantities);
        holder.tvTongTien.setText("Tong Tien: " + tongTien);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class OrderCartHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvQuantity,tvTongTien;
        public OrderCartHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvTongTien = itemView.findViewById(R.id.tvTongTien);
        }
    }
}
