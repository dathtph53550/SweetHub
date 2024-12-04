package com.example.sweethub;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sweethub.Adapter.OrderAdapter;
import com.example.sweethub.Model.Product;
import com.example.sweethub.Models.District;
import com.example.sweethub.Models.GHNOrderRespone;
import com.example.sweethub.Models.Order;
import com.example.sweethub.Models.ResponseGHN;
import com.example.sweethub.servers.GHNRequest;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OrderHistoryActivity extends AppCompatActivity {


    public static final MediaType JSON = MediaType.get("application/json");
    List<ResponseDataOrderCode> products;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        RecyclerView recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        List<Product> products = new ArrayList<>();
//
//
//        OrderAdapter adapter = new OrderAdapter(this, products);
//        recyclerView.setAdapter(adapter);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
       // RecyclerView recyclerView = findViewById(R.id.recyclerView);

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  startActivity(new Intent(OrderHistoryActivity.this, MainActivity.class));
                finish();
            }
        });



        //String url = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/detail";

//        String jsonBody = "{\"order_code\": \"LDYGCF\"}";
//        OkHttpClient client = new OkHttpClient();
//        RequestBody body = RequestBody.create(
//                jsonBody, MediaType.get("application/json; charset=utf-8")
//        );
//
//        Request request = new Request.Builder()
//                .url(url)
//                .addHeader("Content-Type", "application/json")
//                .addHeader("Token", "09248b0c-acdc-11ef-accc-c6f6f22065b5")
//                .post(body)
//                .build();
//        String responseBody = "";
//        try (Response response = client.newCall(request).execute()) {
//            if (response.isSuccessful()) {
//                responseBody = response.body().string();
//            } else {
//                Log.d("Error", "Request failed: " + response.code());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        Gson gson = new Gson();
//        ResponseDataOrderCode responseData = gson.fromJson(responseBody, ResponseDataOrderCode.class);
//
//        String toAddress = responseData.getData().getToAddress();
//        Log.d("To Address", toAddress);
//
//        products = new ArrayList<>();
//        products.add(responseData);
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        OrderAdapter adapter = new OrderAdapter(this, products);
//        recyclerView.setAdapter(adapter);


        List<String> orderCodes = Arrays.asList("LDYGCF", "LDYCCM", "LDYNXC");

        List<ResponseDataOrderCode> allOrders = new ArrayList<>();
        for (String orderCode : orderCodes) {
            String url = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/detail";
            String jsonBody = "{\"order_code\": \"" + orderCode + "\"}";
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Token", "09248b0c-acdc-11ef-accc-c6f6f22065b5")
                    .post(body)
                    .build();

            String responseBody = "";
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    responseBody = response.body().string();
                    Log.d("Response Body", responseBody);

                    Gson gson = new Gson();
                    ResponseDataOrderCode responseData = gson.fromJson(responseBody, ResponseDataOrderCode.class);

                    allOrders.add(responseData);
                } else {
                    Log.d("Error", "Request failed: " + response.code());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        OrderAdapter adapter = new OrderAdapter(this, allOrders);
        recyclerView.setAdapter(adapter);
    }


}