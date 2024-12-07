package com.example.sweethub.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
    CateClick cateClick;

    public CategoryAdapter(Context context, ArrayList<Category> list, CateClick cateClick) {
        this.context = context;
        this.list = list;
        this.cateClick = cateClick;
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
                cateClick.data(category);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                openDialogUpdate(category);
                return false;
            }
        });
    }

    public interface CateClick {
        void data(Category category);

    }

    void openDialogUpdate(Category category){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = ((Activity)context).getLayoutInflater().inflate(R.layout.dialog_add_category,null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        TextView tvCategory = view.findViewById(R.id.tvCategory);
        tvCategory.setText("Cập nhật Category");
        EditText edtCategory = view.findViewById(R.id.edtCategory);
        Button btnThem = view.findViewById(R.id.btnThem);
        btnThem.setText("Cập nhật");

        edtCategory.setText(category.getName());

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtCategory.getText().toString().isEmpty()){
                    edtCategory.setError("Vui lòng nhập tên category");
                }
                else {
                    category.setName(edtCategory.getText().toString());
                    httpRequest.callAPI().updateCategory(category.getId(),category).enqueue(new Callback<Response<ArrayList<Category>>>() {
                        @Override
                        public void onResponse(Call<Response<ArrayList<Category>>> call, retrofit2.Response<Response<ArrayList<Category>>> response) {
                            if(response.isSuccessful()){
                                if(response.body().getStatus() == 200){
                                    list.clear();
                                    list = response.body().getData();
                                    notifyDataSetChanged();
                                    dialog.dismiss();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Response<ArrayList<Category>>> call, Throwable t) {

                        }
                    });
                }
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
