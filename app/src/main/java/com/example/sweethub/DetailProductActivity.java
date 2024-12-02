package com.example.sweethub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.sweethub.Model.Product;
import com.example.sweethub.Model.Response;
import com.example.sweethub.servers.HttpRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class DetailProductActivity extends AppCompatActivity {

    ImageView iv_image,ivFavourite,iv_back;
    TextView tvName,tv_price,tv_description;
    Button btn_add_to_cart;
    private int quantity = 1;
    Product product;
    HttpRequest httpRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        httpRequest = new HttpRequest();
        iv_image = findViewById(R.id.iv_image);
        ivFavourite = findViewById(R.id.ivFavourite);
        tvName = findViewById(R.id.tvName);
        tv_price = findViewById(R.id.tv_price);
        tv_description = findViewById(R.id.tv_description);
        btn_add_to_cart = findViewById(R.id.btn_add_to_cart);
        iv_back = findViewById(R.id.btn_back);

        ImageView btnDecrease = findViewById(R.id.btn_decrease);
        ImageView btnIncrease = findViewById(R.id.btn_increase);
        TextView tvQuantity = findViewById(R.id.tv_quantity);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        product = (Product) intent.getSerializableExtra("product");
        Log.d("product", "onCreate: " + product.getName());
        Log.d("emaillll", "onCreate: " + email);
        tvName.setText(product.getName());
        tv_price.setText(product.getPrice() + " đ");
        tv_description.setText(product.getDescribe());

        if(product.getFavorite()){
            ivFavourite.setImageResource(R.drawable.heartt);
        }else{
            ivFavourite.setImageResource(R.drawable.heart);
        }

        Log.d("zzzz", "onCreate: " + product.getImage());

        Glide.with(this)
                .load(product.getImage().get(0)) // URL hoặc đường dẫn ảnh
                .override(200, 350) // Điều chỉnh kích thước
                .centerCrop()
                .placeholder(R.drawable.baseline_broken_image_24) // Ảnh mặc định khi đang tải
                .into(iv_image);

        ivFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpRequest.callAPI().changeFavourite(product.getId()).enqueue(new Callback<Response<ArrayList<Product>>>() {
                    @Override
                    public void onResponse(Call<Response<ArrayList<Product>>> call, retrofit2.Response<Response<ArrayList<Product>>> response) {
                        if(response.isSuccessful()){
                            if(response.body().getStatus() == 200){
                                if(product.getFavorite()){
                                    ivFavourite.setImageResource(R.drawable.heart);
                                    product.setFavorite(false);
                                }else{
                                    ivFavourite.setImageResource(R.drawable.heartt);
                                    product.setFavorite(true);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Response<ArrayList<Product>>> call, Throwable t) {

                    }
                });
            }
        });

        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                product.setQuantity(quantity + "");
                bundle.putSerializable("product", product);
                bundle.putString("email", email);
                Intent intent = new Intent(DetailProductActivity.this, Location2.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btnDecrease.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvQuantity.setText(String.format("%02d", quantity));
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnIncrease.setOnClickListener(v -> {
            quantity++;
            tvQuantity.setText(String.format("%02d", quantity));
        });


    }
}