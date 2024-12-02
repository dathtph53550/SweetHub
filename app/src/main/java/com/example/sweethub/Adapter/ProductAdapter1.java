package com.example.sweethub.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sweethub.Model.Category;
import com.example.sweethub.Model.Product;
import com.example.sweethub.Model.Response;
import com.example.sweethub.R;
import com.example.sweethub.servers.HttpRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class ProductAdapter1 extends RecyclerView.Adapter<ProductAdapter1.ProductHolder> {

    Context context;
    ArrayList<Product> list;
    FruitClick fruitClick;
    HttpRequest httpRequest;
    String id_Cate;
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

//        if (list == null || list.isEmpty()) {
//            Log.d("ProductAdapter", "List is empty or null.");
//            return; // Exit early if the list is empty
//        }

        // Now it's safe to access elements from the list
        httpRequest = new HttpRequest();
        Product product = list.get(position);
        holder.tvProductName.setText(product.getName());
        holder.tvProductPrice.setText(product.getPrice() + " đ");
        if(product.getImage().get(0) == null){
            Toast.makeText(context, "Image null", Toast.LENGTH_SHORT).show();
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
        String newUrl = url.replace("localhost", "192.168.0.100");
        Glide.with(context)
                .load(newUrl)
                .thumbnail(Glide.with(context)
                        .load(R.drawable.baseline_broken_image_24))
                .into(holder.ivProductImage);
        Log.d("urlll", "onBindViewHolder: " + url);
        Log.d("urlll", "onBindViewHolder: " + newUrl);

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fruitClick.edit(product);
                notifyDataSetChanged();
            }
        });


        holder.btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct.add(product);
                // Cập nhật RecyclerView để hiển thị các sản phẩm đã thêm
                notifyDataSetChanged();
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

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xóa người dùng")
                        .setMessage("Bạn có chắc chắn muốn xóa người dùng này không?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            httpRequest.callAPI().deleteProduct(product.get_id()).enqueue(new Callback<Response<Product>>() {
                                @Override
                                public void onResponse(Call<Response<Product>> call, retrofit2.Response<Response<Product>> response) {
                                    if(response.isSuccessful()){
                                        if(response.body().getStatus() == 200){
                                            list.remove(product);
                                            notifyDataSetChanged();
                                            Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<Response<Product>> call, Throwable t) {
                                    Log.d("zzzz", "onFailure: " + t.getMessage());
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
