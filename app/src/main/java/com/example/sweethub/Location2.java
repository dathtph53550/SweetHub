package com.example.sweethub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sweethub.Adapter.Adapter_Item_District_Select_GHN;
import com.example.sweethub.Adapter.Adapter_Item_Province_Select_GHN;
import com.example.sweethub.Adapter.Adapter_Item_Ward_Select_GHN;
import com.example.sweethub.Model.Category;
import com.example.sweethub.Model.Product;
import com.example.sweethub.Models.District;
import com.example.sweethub.Models.DistrictRequest;
import com.example.sweethub.Models.GHNItem;
import com.example.sweethub.Models.GHNOrderRespone;
import com.example.sweethub.Models.Order;
import com.example.sweethub.Models.Province;
import com.example.sweethub.Models.ResponseGHN;
import com.example.sweethub.Models.SendOrderRequest;
import com.example.sweethub.Models.Ward;
import com.example.sweethub.databinding.ActivityLocation2Binding;
import com.example.sweethub.servers.GHNRequest;
import com.example.sweethub.servers.GHNServices;
import com.example.sweethub.servers.HttpRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Location2 extends AppCompatActivity {

    private ActivityLocation2Binding binding;
    private GHNRequest request;
    private GHNServices ghnServices;
    private String productId, productTypeId, productName, description, WardCode;
    private double rate, price;
    private int image, DistrictID, ProvinceID ;
    private String email;
    Button btn_order;
    HttpRequest httpRequest;
    Product product;
    Button btnBack;
    EditText edt_name, edt_phone, edt_location;
    private Adapter_Item_Province_Select_GHN adapter_item_province_select_ghn;
    private Adapter_Item_District_Select_GHN adapter_item_district_select_ghn;
    private Adapter_Item_Ward_Select_GHN adapter_item_ward_select_ghn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLocation2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        httpRequest = new HttpRequest();
        request = new GHNRequest();
        btn_order = findViewById(R.id.btn_order);
        edt_name = findViewById(R.id.edt_name);
        edt_phone = findViewById(R.id.edt_phone);
        edt_location = findViewById(R.id.edt_location);
        btnBack = findViewById(R.id.btnBack);
        Intent intent = getIntent();
        intent.getExtras();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            productId = bundle.getString("productId");
            productTypeId = bundle.getString("productTypeId");
            productName = bundle.getString("productName");
            description = bundle.getString("description");
            rate = bundle.getDouble("rate");
            price = bundle.getDouble("price");
            image = bundle.getInt("image");
        }

        request.callAPI().getListProvince().enqueue(responseProvince);
        binding.spProvince.setOnItemSelectedListener(onItemSelectedListener);
        binding.spDistrict.setOnItemSelectedListener(onItemSelectedListener);
        binding.spWard.setOnItemSelectedListener(onItemSelectedListener);

        binding.spProvince.setSelection(1);
        binding.spDistrict.setSelection(1);
        binding.spWard.setSelection(1);

        product = (Product) intent.getSerializableExtra("product");
        email = intent.getStringExtra("email");
        Log.d("maillll", "onCreate: " + email + "--" + product.getQuantity());

        edt_name.setText(email);



        Log.d("aaaaaaaaa", "onClick: "+ product.getName()+ "  -  "+ product.getPrice()+ "  -  "+ product.get_id() + "  -  "+ product.getQuantity());


        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (WardCode.equals("")) return;
                Log.d("ccccc", "onClick: " + WardCode);
                Log.d("bbbbb", "Thành Công !!");
                Log.d("aaaaaaaaa", "onClick: "+ product.getName()+ "  -  "+ product.getPrice()+ "  -  "+ product.get_id() + "  -  "+ product.getQuantity());
                GHNItem ghnItem = new GHNItem();
                ghnItem.setName(product.getName());
                ghnItem.setPrice(Integer.parseInt(product.getPrice()) * Integer.parseInt(product.getQuantity()));
                ghnItem.setCode(product.get_id());
                ghnItem.setQuantity(Integer.parseInt(product.getQuantity()));
                ghnItem.setWeight(50);
                ArrayList<GHNItem> items = new ArrayList<>();
                items.add(ghnItem);
                SendOrderRequest sendOrderRequest = new SendOrderRequest(
                        edt_name.getText().toString(),
                        edt_phone.getText().toString(),
                        edt_location.getText().toString(),
                        WardCode,
                        DistrictID,
                        items
                );
                request.callAPI().GHNOrder(sendOrderRequest).enqueue(responseOrder);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

//    Callback<ResponseGHN<GHNOrderRespone>> responseOrder = new Callback<ResponseGHN<GHNOrderRespone>>() {
//        @Override
//        public void onResponse(Call<ResponseGHN<GHNOrderRespone>> call, Response<ResponseGHN<GHNOrderRespone>> response) {
//            if(response.isSuccessful()){
//                if(response.body().getCode() == 200){
//                    Toast.makeText(Location2.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
//                    Order order = new Order();
//                    order.setOrder_code(response.body().getData().getOrder_code());
//                    order.setId_user(getSharedPreferences("INFO",MODE_PRIVATE).getString("id",""));
//                    httpRequest.callAPI().order(order).enqueue(new Callback<com.example.sweethub.Model.Response<ArrayList<Order>>>() {
//                        @Override
//                        public void onResponse(Call<com.example.sweethub.Model.Response<ArrayList<Order>>> call, Response<com.example.sweethub.Model.Response<ArrayList<Order>>> response) {
//                            if(response.isSuccessful()){
//                                if(response.body().getStatus() == 200){
//                                    Toast.makeText(Location2.this, "Đặt hàng thành công !!", Toast.LENGTH_SHORT).show();
//                                    finish();
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<com.example.sweethub.Model.Response<ArrayList<Order>>> call, Throwable t) {
//                            Log.d("Faill", "onFailure: " + t.getMessage());
//                        }
//                    });
////                }
////            }
////        }
//
//        @Override
//        public void onFailure(Call<ResponseGHN<GHNOrderRespone>> call, Throwable t) {
//            Log.d("Faill", "onFailure: " + t.getMessage());
//        }
//    };

    Callback<ResponseGHN<GHNOrderRespone>> responseOrder = new Callback<ResponseGHN<GHNOrderRespone>>() {
        @Override
        public void onResponse(Call<ResponseGHN<GHNOrderRespone>> call, Response<ResponseGHN<GHNOrderRespone>> response) {
            if (response.isSuccessful()){
                Toast.makeText(Location2.this, "Đặt hàng thành công !!", Toast.LENGTH_SHORT).show();
                if (response.body().getCode() == 200){
                    Toast.makeText(Location2.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                    Order order = new Order();
                    order.setOrder_code(response.body().getData().getOrder_code());
                    order.setId_user(getSharedPreferences("INFO",MODE_PRIVATE).getString("id",""));
                    httpRequest.callAPI().order(order).enqueue(new Callback<com.example.sweethub.Model.Response<ArrayList<Category>>>() {
                        @Override
                        public void onResponse(Call<com.example.sweethub.Model.Response<ArrayList<Category>>> call, Response<com.example.sweethub.Model.Response<ArrayList<Category>>> response) {
                            if (response.isSuccessful()){
                                if (response.body().getStatus() == 200){
                                    Toast.makeText(Location2.this, "Đặt hàng thành công !!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<com.example.sweethub.Model.Response<ArrayList<Category>>> call, Throwable t) {
                            Log.d("Faill", "onFailure: " + t.getMessage());
                        }
                    });

                }
            }
        }

        @Override
        public void onFailure(Call<ResponseGHN<GHNOrderRespone>> call, Throwable t) {

        }
    };

    AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (parent.getId() == R.id.sp_province) {
                ProvinceID = ((Province) parent.getAdapter().getItem(position)).getProvinceID();
                DistrictRequest districtRequest = new DistrictRequest(ProvinceID);
                request.callAPI().getListDistrict(districtRequest).enqueue(responseDistrict);
            } else if (parent.getId() == R.id.sp_district) {
                DistrictID = ((District) parent.getAdapter().getItem(position)).getDistrictID();
                request.callAPI().getListWard(DistrictID).enqueue(responseWard);
            } else if (parent.getId() == R.id.sp_ward) {
                WardCode = ((Ward) parent.getAdapter().getItem(position)).getWardCode();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    Callback<ResponseGHN<ArrayList<Province>>> responseProvince = new Callback<ResponseGHN<ArrayList<Province>>>() {
        @Override
        public void onResponse(Call<ResponseGHN<ArrayList<Province>>> call, Response<ResponseGHN<ArrayList<Province>>> response) {
            if(response.isSuccessful()){
                if(response.body().getCode() == 200){
                    ArrayList<Province> ds = new ArrayList<>(response.body().getData());
                    SetDataSpinProvince(ds);
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseGHN<ArrayList<Province>>> call, Throwable t) {
            Toast.makeText(Location2.this, "Lấy dữ liệu bị lỗi", Toast.LENGTH_SHORT).show();
        }
    };

    Callback<ResponseGHN<ArrayList<District>>> responseDistrict = new Callback<ResponseGHN<ArrayList<District>>>() {
        @Override
        public void onResponse(Call<ResponseGHN<ArrayList<District>>> call, Response<ResponseGHN<ArrayList<District>>> response) {
            if(response.isSuccessful()){
                if(response.body().getCode() == 200){
                    ArrayList<District> ds = new ArrayList<>(response.body().getData());
                    SetDataSpinDistrict(ds);
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseGHN<ArrayList<District>>> call, Throwable t) {

        }
    };

    Callback<ResponseGHN<ArrayList<Ward>>> responseWard = new Callback<ResponseGHN<ArrayList<Ward>>>() {
        @Override
        public void onResponse(Call<ResponseGHN<ArrayList<Ward>>> call, Response<ResponseGHN<ArrayList<Ward>>> response) {
            if(response.isSuccessful()){
                if(response.body().getCode() == 200){

                    if(response.body().getData() == null)
                        return;

                    ArrayList<Ward> ds = new ArrayList<>(response.body().getData());

                    ds.addAll(response.body().getData());
                    SetDataSpinWard(ds);
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseGHN<ArrayList<Ward>>> call, Throwable t) {
            Toast.makeText(Location2.this, "Lỗi", Toast.LENGTH_SHORT).show();
        }
    };

    private void SetDataSpinProvince(ArrayList<Province> ds){
        adapter_item_province_select_ghn = new Adapter_Item_Province_Select_GHN(this, ds);
        binding.spProvince.setAdapter(adapter_item_province_select_ghn);
    }

    private void SetDataSpinDistrict(ArrayList<District> ds){
        adapter_item_district_select_ghn = new Adapter_Item_District_Select_GHN(this, ds);
        binding.spDistrict.setAdapter(adapter_item_district_select_ghn);
    }

    private void SetDataSpinWard(ArrayList<Ward> ds){
        adapter_item_ward_select_ghn = new Adapter_Item_Ward_Select_GHN(this, ds);
        binding.spWard.setAdapter(adapter_item_ward_select_ghn );
    }
}