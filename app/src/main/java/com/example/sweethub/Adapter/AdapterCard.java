package com.example.sweethub.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sweethub.Fragment.CartItem;
import com.example.sweethub.Model.Product;
import com.example.sweethub.R;

import java.util.ArrayList;

public class AdapterCard extends RecyclerView.Adapter<AdapterCard.CardHolder> {

    Context context;
    ArrayList<CartItem> list;
    private CartUpdateListener listener;

    public AdapterCard(Context context, ArrayList<CartItem> list, CartUpdateListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = ((Activity)context).getLayoutInflater().inflate(R.layout.item_card_product, parent, false);
        return new CardHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, int position) {
        CartItem item = list.get(position);
        holder.productName.setText(item.getProduct().getName());
        holder.productPrice.setText( item.getProduct().getPrice() + " đ");
        holder.productQuantity.setText(String.valueOf(item.getQuantity()));


        String url = item.getProduct().getImage().get(0);
        String newUrl = url.replace("localhost", "192.168.0.100");
        Glide.with(context)
                .load(newUrl)
                .thumbnail(Glide.with(context).load(R.drawable.baseline_broken_image_24))
                .into(holder.productImage);


        holder.increaseButton.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            listener.onCartUpdated();
            notifyItemChanged(position);
        });

        holder.decreaseButton.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                listener.onCartUpdated();
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CardHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productQuantity;
        ImageButton increaseButton, decreaseButton;
        ImageView productImage;
        public CardHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            increaseButton = itemView.findViewById(R.id.increaseButton);
            decreaseButton = itemView.findViewById(R.id.decreaseButton);
            productImage = itemView.findViewById(R.id.productImage);
        }
    }

    public interface CartUpdateListener {
        void onCartUpdated();
    }
}
