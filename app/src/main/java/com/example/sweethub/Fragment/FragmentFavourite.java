package com.example.sweethub.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sweethub.Adapter.AdapterFavourite;
import com.example.sweethub.DetailProductActivity;
import com.example.sweethub.Model.Product;
import com.example.sweethub.Model.Response;
import com.example.sweethub.R;
import com.example.sweethub.servers.HttpRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class FragmentFavourite extends Fragment implements AdapterFavourite.ProductClick {

    RecyclerView recyclerView;
    HttpRequest httpRequest;
    AdapterFavourite adapterFavourite;
    ArrayList<Product> list;
    String data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            data = bundle.getString("email");
            // Sử dụng dữ liệu
            Log.d("aliiiiiiiii", "Received data: " + data);
        }
        return inflater.inflate(R.layout.fragment_favourite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        httpRequest = new HttpRequest();
        recyclerView = view.findViewById(R.id.recyclerView);

        list = new ArrayList<>();
        adapterFavourite = new AdapterFavourite(getContext(), list,this);
        recyclerView.setAdapter(adapterFavourite);
        httpRequest.callAPI().getListProductByFavourite().enqueue(new Callback<Response<ArrayList<Product>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Product>>> call, retrofit2.Response<Response<ArrayList<Product>>> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus() == 200){
                        list.clear();
                        list.addAll(response.body().getData());
                        adapterFavourite.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<Product>>> call, Throwable t) {

            }
        });
    }

    @Override
    public void showDetail(Product product) {
        Intent intent = new Intent(getContext(), DetailProductActivity.class);
        intent.putExtra("product", product);
        intent.putExtra("email",data);
        startActivity(intent);
    }
}
