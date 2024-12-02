package com.example.sweethub.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sweethub.Fragment.FragmentTest;
import com.example.sweethub.Model.Category;
import com.example.sweethub.Model.Product;
import com.example.sweethub.Model.Response;
import com.example.sweethub.R;
import com.example.sweethub.servers.HttpRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {

    Context context;
    ArrayList<Category> list;
    HttpRequest httpRequest;

    public CategoryAdapter(Context context, ArrayList<Category> list) {
        this.context = context;
        this.list = list;
    }



    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = ((Activity)context).getLayoutInflater().inflate(R.layout.item_category,parent,false);
        return new CategoryHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        httpRequest = new HttpRequest();
        Category category = list.get(position);
        holder.tvCategoryName.setText(category.getName());
        Log.d("zzzzz", "onBindViewHolder: " + category.getId());
        Log.d("zzzzz", "onBindViewHolder: " + category.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CategoryHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;
        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }
    }


}
