package com.example.sweethub.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sweethub.Adapter.CategoryAdapter;
import com.example.sweethub.Adapter.ProductAdapter;
import com.example.sweethub.Adapter.ProductAdapter1;
import com.example.sweethub.Model.Category;
import com.example.sweethub.Model.Product;
import com.example.sweethub.Model.Response;
import com.example.sweethub.R;
import com.example.sweethub.servers.HttpRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class FragmentTest extends Fragment implements ProductAdapter1.FruitClick {

    RecyclerView rycCategory;
    ArrayList<Product> list;
    ProductAdapter1 productAdapter;
    HttpRequest httpRequest;
    SharedPreferences sharedPreferences;
    String token;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rycCategory = view.findViewById(R.id.rycCategory);
        httpRequest = new HttpRequest();
        list = new ArrayList<>();
        sharedPreferences = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);

        token = sharedPreferences.getString("token","");
        rycCategory.setAdapter(productAdapter);
        Log.d("Token", "Token: " + token);

        httpRequest.callAPI().getListFruit("Bearer" + token).enqueue(new Callback<Response<ArrayList<Product>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Product>>> call, retrofit2.Response<Response<ArrayList<Product>>> response) {
                if (response.isSuccessful()){
                    if(response.body().getStatus() == 200){
                        ArrayList<Product> list = response.body().getData();
                        productAdapter = new ProductAdapter1(getContext(),list,FragmentTest.this);
                        rycCategory.setAdapter(productAdapter);
                        rycCategory.getAdapter().notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<Product>>> call, Throwable t) {
                Log.d("dung loi ma", "onFailure: " + t.getMessage());
            }
        });

    }


    @Override
    public void delete(Product product) {

    }

    @Override
    public void edit(Product product) {

    }

    @Override
    public void showDetail(Product product) {

    }
}
