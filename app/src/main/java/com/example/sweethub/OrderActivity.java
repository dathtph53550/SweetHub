package com.example.sweethub;

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

import com.example.sweethub.Adapter.AdapterOrderCart;
import com.example.sweethub.Model.OrderCart;
import com.example.sweethub.Model.Response;
import com.example.sweethub.Models.Order;
import com.example.sweethub.servers.HttpRequest;

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

        httpRequest.callAPI().getListOrderCart().enqueue(new Callback<Response<ArrayList<OrderCart>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<OrderCart>>> call, retrofit2.Response<Response<ArrayList<OrderCart>>> response) {
                Log.d("API Response", "HTTP Status: " + response.code());

                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getStatus() == 200) {
                        list.addAll(response.body().getData());
                        OrderCart orderCart = list.get(0);

                        // Truyền dữ liệu vào Adapter
                        adapter = new AdapterOrderCart(
                                OrderActivity.this,
                                orderCart.getName(),
                                orderCart.getPrice(),
                                orderCart.getQuantity()
                        );
                        recyclerView.setAdapter(adapter);
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