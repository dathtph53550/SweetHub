package com.example.sweethub.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

public class AdapterFavourite extends RecyclerView.Adapter<AdapterFavourite.FavouriteHolder> {

    Context context;
    ArrayList<Product> list;
    HttpRequest httpRequest;
    ProductClick productClick;

    public AdapterFavourite(Context context, ArrayList<Product> list,ProductClick productClick) {
        this.context = context;
        this.list = list;
        this.productClick = productClick;
    }

    public AdapterFavourite(Context context, ArrayList<Product> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FavouriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = ((Activity)context).getLayoutInflater().inflate(R.layout.item_favourite, parent, false);
        return new FavouriteHolder(v);
    }



    @Override
    public void onBindViewHolder(@NonNull FavouriteHolder holder, int position) {
        httpRequest = new HttpRequest();
        Product product = list.get(position);
        holder.tvName.setText(product.getName());

        Log.d("bbbbbbb", "onBindViewHolder: " +product.getName() + "--" + product.getImage().get(0));
//        holder.tvCategory.setText(product.getCategory().getId());

        holder.tvCategory.setText(product.getDescribe());
//        Log.d("zzzz", "onBindViewHolder: " + product.getCategory().getId());
        Glide.with(context)
                .load(product.getImage().get(0))
                .thumbnail(Glide.with(context)
                        .load(R.drawable.baseline_broken_image_24))
                .into(holder.imgFavorite);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productClick.showDetail(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface ProductClick {
        void showDetail(Product product);

    }
    class FavouriteHolder extends RecyclerView.ViewHolder {
        ImageView imgFavorite,ivCheck;
        TextView   tvName, tvCategory;
        public FavouriteHolder(@NonNull View itemView) {
            super(itemView);
            imgFavorite = itemView.findViewById(R.id.imgFavorite);
            tvName = itemView.findViewById(R.id.tvName);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            ivCheck = itemView.findViewById(R.id.ivCheck);
        }
    }
}
