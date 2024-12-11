package com.nhom7.sweethub;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom7.sweethub.Adapter.OrderAdapter;
import com.nhom7.sweethub.Models.Order;
import com.nhom7.sweethub.servers.HttpRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

public class OrderHistoryActivity extends AppCompatActivity {


    public static final MediaType JSON = MediaType.get("application/json");
    List<ResponseDataOrderCode> products;
    HttpRequest httpRequest;
    ArrayList<Order> listOrder;
    ArrayList<String> orderCodes;

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

        listOrder = new ArrayList<>();
        httpRequest = new HttpRequest();
        orderCodes = new ArrayList<>();

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

        httpRequest.callAPI().getListOrder().enqueue(new Callback<com.nhom7.sweethub.Model.Response<ArrayList<Order>>>() {
            @Override
            public void onResponse(Call<com.nhom7.sweethub.Model.Response<ArrayList<Order>>> call, retrofit2.Response<com.nhom7.sweethub.Model.Response<ArrayList<Order>>> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus() == 200){
                        listOrder.clear();
                        listOrder.addAll(response.body().getData());

                        // Tạo danh sách orderCodes
                        for (Order order : listOrder) {
                            orderCodes.add(order.getOrder_code());
                        }

                        // Gọi hàm lấy chi tiết đơn hàng
                        fetchOrderDetails(orderCodes);
                    }
                }
            }

            @Override
            public void onFailure(Call<com.nhom7.sweethub.Model.Response<ArrayList<Order>>> call, Throwable t) {
                Log.d("ttttt", "onFailure: "+ t.getMessage());
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


//        List<String> orderCodes = Arrays.asList("LDYGCF", "LDYCCM", "LDYNXC");

//        List<ResponseDataOrderCode> allOrders = new ArrayList<>();
//        for (String orderCode : orderCodes) {
//            String url = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/detail";
//            String jsonBody = "{\"order_code\": \"" + orderCode + "\"}";
//            OkHttpClient client = new OkHttpClient();
//            RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));
//
//            Request request = new Request.Builder()
//                    .url(url)
//                    .addHeader("Content-Type", "application/json")
//                    .addHeader("Token", "09248b0c-acdc-11ef-accc-c6f6f22065b5")
//                    .post(body)
//                    .build();
//
//            String responseBody = "";
//            try (Response response = client.newCall(request).execute()) {
//                if (response.isSuccessful()) {
//                    responseBody = response.body().string();
//                    Log.d("Response Body", responseBody);
//
//                    Gson gson = new Gson();
//                    ResponseDataOrderCode responseData = gson.fromJson(responseBody, ResponseDataOrderCode.class);
//
//                    allOrders.add(responseData);
//                    Log.d("eeeeeee", "onCreate: " + allOrders.size());
//                } else {
//                    Log.d("Error", "Request failed: " + response.code());
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        RecyclerView recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        OrderAdapter adapter = new OrderAdapter(this, allOrders);
//        recyclerView.setAdapter(adapter);
    }

    private void fetchOrderDetails(List<String> orderCodes) {
        List<ResponseDataOrderCode> allOrders = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();

        for (String orderCode : orderCodes) {
            String url = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/detail";
            String jsonBody = "{\"order_code\": \"" + orderCode + "\"}";
            RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Token", "09248b0c-acdc-11ef-accc-c6f6f22065b5")
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
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

        // Cập nhật RecyclerView
        runOnUiThread(() -> {
            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            OrderAdapter adapter = new OrderAdapter(this, allOrders);
            recyclerView.setAdapter(adapter);

            Log.d("RecyclerView", "Adapter updated with orders");
        });
    }





}