package com.example.sweethub;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sweethub.Adapter.OrderAdapter;
import com.example.sweethub.Model.Product;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

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
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Product> products = new ArrayList<>();
//        products.add(new Product("1", "Chocolate Cake", R.drawable.cake1, "Rich and creamy chocolate cake with dark chocolate ganache", 15.99, 1, true, 101));
//        products.add(new Product("2", "Strawberry Tart", R.drawable.cake2, "Fresh strawberries on a flaky butter tart crust", 12.99, 0, false, 102));
//        products.add(new Product("3", "New York Cheesecake", R.drawable.cake3, "Classic cheesecake with a graham cracker crust and creamy filling", 8.99, 1, true, 103));
//        products.add(new Product("4", "Red Velvet Cake", R.drawable.cake4, "Soft and moist red velvet layers with cream cheese frosting", 14.49, 1, true, 104));


        OrderAdapter adapter = new OrderAdapter(this, products);
        recyclerView.setAdapter(adapter);




        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  startActivity(new Intent(OrderHistoryActivity.this, MainActivity.class));
                finish();
            }
        });


    }
}