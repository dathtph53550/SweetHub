package com.example.sweethub.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sweethub.Fragment.CartItem;
import com.example.sweethub.Model.Cart;
import com.example.sweethub.Model.Product;
import com.example.sweethub.Model.Response;
import com.example.sweethub.R;
import com.example.sweethub.servers.ApiServices;
import com.example.sweethub.servers.HttpRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class AdapterCard extends RecyclerView.Adapter<AdapterCard.CardHolder> {

    Context context;
    ArrayList<Cart> list;
    HttpRequest httpRequest;

    public AdapterCard(Context context, ArrayList<Cart> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = ((Activity)context).getLayoutInflater().inflate(R.layout.item_card_product, parent, false);
        return new CardHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, int position) {
        httpRequest = new HttpRequest();
        Cart cart = list.get(position);
        holder.productName.setText(cart.getName());
        holder.productPrice.setText(cart.getPrice() + " đ");
        holder.productQuantity.setText(cart.getQuantity());
        holder.productDescription.setText(cart.getDescribe());
        String url = cart.getImage();
        String newUrl = url.replace("localhost", ApiServices.IPv4);
        Glide.with(context)
                .load(newUrl)
                .thumbnail(Glide.with(context)
                        .load(R.drawable.baseline_broken_image_24))
                .into(holder.productImage);

        holder.increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("zzzzzzzzzzzz", "onClick: " + cart.get_id());
                httpRequest.callAPI().increaseQuantity(cart.get_id()).enqueue(new Callback<Response<ArrayList<Cart>>>() {
                    @Override
                    public void onResponse(Call<Response<ArrayList<Cart>>> call, retrofit2.Response<Response<ArrayList<Cart>>> response) {
                        if(response.isSuccessful()){
                            if(response.body().getStatus() == 200){
                                list.clear();
                                list.addAll(response.body().getData());
                                notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Response<ArrayList<Cart>>> call, Throwable t) {
                        Log.d("ooooooooooo", "onFailure: " + t.getMessage());
                    }
                });
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xóa người dùng")
                        .setMessage("Bạn có chắc chắn muốn xóa người dùng này không?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            httpRequest.callAPI().deleteCart(cart.get_id()).enqueue(new Callback<Response<ArrayList<Cart>>>() {
                                @Override
                                public void onResponse(Call<Response<ArrayList<Cart>>> call, retrofit2.Response<Response<ArrayList<Cart>>> response) {
                                    if(response.isSuccessful()){
                                        if(response.body().getStatus() == 200){
                                            list.clear();
                                            list.addAll(response.body().getData());
                                            notifyDataSetChanged();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<Response<ArrayList<Cart>>> call, Throwable t) {

                                }
                            });

                        })
                        .setNegativeButton("Hủy", (dialog, which) -> {
                            dialog.dismiss();
                        });
                builder.create().show();
                return false;
            }
        });

        holder.decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpRequest.callAPI().decreaseQuantity(cart.get_id()).enqueue(new Callback<Response<ArrayList<Cart>>>() {
                    @Override
                    public void onResponse(Call<Response<ArrayList<Cart>>> call, retrofit2.Response<Response<ArrayList<Cart>>> response) {
                        if(response.isSuccessful()){
                            if(response.body().getStatus() == 200){
                                list.clear();
                                list.addAll(response.body().getData());
                                notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Response<ArrayList<Cart>>> call, Throwable t) {
                        Log.d("ooooooooooo", "onFailure: " + t.getMessage());

                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }





    class CardHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productQuantity,productDescription;
        ImageButton increaseButton, decreaseButton;
        ImageView productImage;
        public CardHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            increaseButton = itemView.findViewById(R.id.increaseButton);
            decreaseButton = itemView.findViewById(R.id.decreaseButton);
            productDescription = itemView.findViewById(R.id.productDescription);
            productImage = itemView.findViewById(R.id.productImage);
        }
    }

    public interface CartUpdateListener {
        void onCartUpdated();
    }
}
