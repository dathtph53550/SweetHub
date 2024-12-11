package com.nhom7.sweethub;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom7.sweethub.Adapter.AdapterOrderCart;
import com.nhom7.sweethub.Model.OrderCart;
import com.nhom7.sweethub.Model.Response;
import com.nhom7.sweethub.servers.HttpRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class OrderActivity extends AppCompatActivity {
    ImageView backButton;
    RecyclerView recyclerView;
    HttpRequest httpRequest;
    ArrayList<OrderCart> list;
    AdapterOrderCart adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        httpRequest = new HttpRequest();
        backButton = findViewById(R.id.backButton);
        recyclerView = findViewById(R.id.recyclerView);
        list = new ArrayList<>();
        adapter = new AdapterOrderCart(OrderActivity.this, list);
        recyclerView.setAdapter(adapter);

        httpRequest.callAPI().getListOrderCart().enqueue(new Callback<Response<ArrayList<OrderCart>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<OrderCart>>> call, retrofit2.Response<Response<ArrayList<OrderCart>>> response) {
                Log.d("API Response", "HTTP Status: " + response.code());

                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getStatus() == 200) {
                        list.clear();
                        list.addAll(response.body().getData()); // Thêm tất cả sản phẩm vào list
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.d("API Error", "Status code is not 200, or body is null");
                    }
                } else {
                    Log.d("mm", "onResponse: Error response: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<OrderCart>>> call, Throwable t) {

            }
        });



        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}