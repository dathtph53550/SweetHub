package com.example.sweethub.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sweethub.DetailProductActivity;
import com.example.sweethub.GlobalValue;
import com.example.sweethub.Location2;
import com.example.sweethub.Model.Cart;
import com.example.sweethub.Model.Category;
import com.example.sweethub.Model.Product;
import com.example.sweethub.Model.Response;
import com.example.sweethub.R;
import com.example.sweethub.servers.ApiServices;
import com.example.sweethub.servers.HttpRequest;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class ProductAdapter1 extends RecyclerView.Adapter<ProductAdapter1.ProductHolder>{

    Context context;
    ArrayList<Product> list;
    FruitClick fruitClick;
    HttpRequest httpRequest;
    ArrayList<Product> addProduct = new ArrayList<>();


    public ProductAdapter1(Context context, ArrayList<Product> list, FruitClick fruitClick) {
        this.context = context;
        this.list = list;
        this.fruitClick = fruitClick;
    }



    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {

        httpRequest = new HttpRequest();
        Product product = list.get(position);
        holder.tvProductName.setText(product.getName());
        holder.tvProductPrice.setText(product.getPrice() + " đ");
        if(product.getImage().get(0) == null){
            Toast.makeText(context, "Image null", Toast.LENGTH_SHORT).show();
            Glide.with(context)
                    .load(R.drawable.baseline_broken_image_24)
                    .into(holder.ivProductImage);
            return;
        }
        Log.d("image", "onBindViewHolder: " + product.getImage().get(0));
        Log.d("favourite", "onBindViewHolder: " + product.getFavorite());
        if (product.getFavorite()) {
            holder.ivFavourite.setImageResource(R.drawable.heartt); // Hình trái tim đầy
        } else {
            holder.ivFavourite.setImageResource(R.drawable.heart); // Hình trái tim trống
        }
        String url = product.getImage().get(0);
        String newUrl = url.replace("localhost", ApiServices.IPv4);
        Glide.with(context)
                .load(newUrl)
                .thumbnail(Glide.with(context)
                        .load(R.drawable.baseline_broken_image_24))
                .into(holder.ivProductImage);
        Log.d("urllliiiii", "onBindViewHolder: " + url);
//        Log.d("urlll", "onBindViewHolder: " + newUrl);

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fruitClick.edit(product);
                notifyDataSetChanged();
            }
        });

        SharedPreferences sharedPreferences = context.getSharedPreferences("INFO", Context.MODE_PRIVATE);
        int role = sharedPreferences.getInt("role", -1);

        if (role == 0) {
            holder.ivEdit.setVisibility(View.GONE);
        } else {
            holder.ivEdit.setVisibility(View.VISIBLE);
        }


        holder.btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cart cart = new Cart();
                cart.setId_product(product.get_id());
                cart.setName(product.getName());
                Locale vnLocale = new Locale("vi", "VN");
                NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(vnLocale);
                // Cập nhật TextView
                cart.setPrice(currencyFormat.format(Double.parseDouble(product.getPrice())) + "");
                cart.setDescribe(product.getDescribe());
                cart.setImage(product.getImage().get(0));
                cart.setQuantity("1");
                cart.setId_category(product.getId_category().getId());


                Log.d("mmmmmm", "onClick: " +product.getId_category().getId() + "---" + product.getId() );
                httpRequest.callAPI().addCart(cart).enqueue(new Callback<Response<Cart>>() {
                    @Override
                    public void onResponse(Call<Response<Cart>> call, retrofit2.Response<Response<Cart>> response) {
                        if(response.isSuccessful()){
                            if(response.body().getStatus() == 200){
                                Toast.makeText(context, "Thêm giỏ hàng thành công  !!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Response<Cart>> call, Throwable t) {
                        Log.d("tttttttt", "onFailure: " + t.getMessage());
                    }
                });
            }


        });


        holder.ivFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpRequest.callAPI().changeFavourite(product.getId()).enqueue(new Callback<Response<ArrayList<Product>>>() {
                    @Override
                    public void onResponse(Call<Response<ArrayList<Product>>> call, retrofit2.Response<Response<ArrayList<Product>>> response) {
                        if(response.isSuccessful()){
                            Log.d("hihihi", "onResponse: "+ response.body().getStatus());
                            if(response.body().getStatus() == 200){
                                list.clear();
                                list.addAll(response.body().getData());
                                notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Response<ArrayList<Product>>> call, Throwable t) {
                        Log.d("huhu", "onFailure: " + t.getMessage());
                    }
                });
            }
        });


//

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fruitClick.showDetail(product);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }




    public interface FruitClick {
        void delete(Product product);
        void edit(Product product);

        void showDetail(Product product);

    }

    public ArrayList<Product> getAddedProducts() {
        return addProduct;
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvProductPrice;
        ImageView ivProductImage;
        ImageView ivEdit,ivFavourite;
        ImageView btnAddProduct;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            ivFavourite = itemView.findViewById(R.id.ivFavourite);
            btnAddProduct = itemView.findViewById(R.id.btnAddProduct);
        }
    }
}
