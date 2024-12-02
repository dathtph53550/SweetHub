package com.example.sweethub;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.sweethub.Adapter.ProductAdapter;
import com.example.sweethub.Adapter.SlideshowAdapter;
import com.example.sweethub.Fragment.FragmentHome;
import com.example.sweethub.Model.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private EditText etSearch;
    private ViewPager2 viewPagerSlideshow;
    private RecyclerView rvProducts;
    private BottomNavigationView bottomNavigationView;
    private Button btnCake, btnDonuts, btnCookies;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private List<Product> filteredProducts;

    private int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        if (intent != null) {
            String navigateTo = intent.getStringExtra("navigateTo");
            if ("FragmentHome".equals(navigateTo)) {
                Product newProduct = (Product) intent.getSerializableExtra("newProduct"); // Nhận sản phẩm mới nếu cần
                showFragmentHome(newProduct);
            }
        }

        etSearch = findViewById(R.id.etSearch);
        viewPagerSlideshow = findViewById(R.id.viewPagerSlideshow);
        rvProducts = findViewById(R.id.rvProducts);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);


        btnCake = findViewById(R.id.btnCake);
        btnDonuts = findViewById(R.id.btnDonuts);
        btnCookies = findViewById(R.id.btnCookies);

        rvProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Initialize Product List and Adapter
        productList = new ArrayList<>();
        filteredProducts = new ArrayList<>(); // Initialize filteredProducts
        productAdapter = new ProductAdapter(filteredProducts);
        rvProducts.setAdapter(productAdapter);

        // Setup Event Listeners for Buttons (Category Filter)
//        btnCake.setOnClickListener(v -> filterProductsByCategory("Cake"));
//        btnDonuts.setOnClickListener(v -> filterProductsByCategory("Donuts"));
//        btnCookies.setOnClickListener(v -> filterProductsByCategory("Cookies"));

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_home) {
                Toast.makeText(HomeActivity.this, "Home selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.navigation_cart) {
                Toast.makeText(HomeActivity.this, "Cart selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.navigation_favorite) {
                Toast.makeText(HomeActivity.this, "Favorite selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.navigation_profile) {
                Toast.makeText(HomeActivity.this, "Profile selected", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });


        // Setup slideshow
        setupSlideshow();

        // Load product data
//        loadProducts();

        // Search functionality
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                filterProductsBySearch(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    // Search product filter
    private void filterProductsBySearch(String query) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : productList) {
            if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(product);
            }
        }
        filteredProducts.clear();
        filteredProducts.addAll(filteredList);
        productAdapter.notifyDataSetChanged();
    }

    // Category filter
//    private void filterProductsByCategory(String category) {
//        List<Product> filteredList = new ArrayList<>();
//        for (Product product : productList) {
//            if (product.getCategory().equalsIgnoreCase(category)) {
//                filteredList.add(product);
//            }
//        }
//        if (filteredList.isEmpty()) {
//            Toast.makeText(HomeActivity.this, "No products found in " + category, Toast.LENGTH_SHORT).show();
//        }
//        filteredProducts.clear();
//        filteredProducts.addAll(filteredList);
//        productAdapter.notifyDataSetChanged();
//    }

    // Set up slideshow with images
    private void setupSlideshow() {
        List<Integer> images = new ArrayList<>();
        images.add(R.drawable.cake_banner); // Correct usage of resource ID
        images.add(R.drawable.cake_banner);
        images.add(R.drawable.cake_banner);

        SlideshowAdapter slideshowAdapter = new SlideshowAdapter(images);
        viewPagerSlideshow.setAdapter(slideshowAdapter);

        // Auto-slide functionality
        viewPagerSlideshow.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentPosition == images.size()) {
                    currentPosition = 0;
                }
                viewPagerSlideshow.setCurrentItem(currentPosition++, true);
                viewPagerSlideshow.postDelayed(this, 3000); // Slide every 3 seconds
            }
        }, 3000);
    }

    private void showFragmentHome(Product newProduct) {
        FragmentHome fragmentHome = new FragmentHome();

        // Nếu cần gửi sản phẩm mới đến FragmentHome
        if (newProduct != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("newProduct", newProduct);
            fragmentHome.setArguments(bundle);
        }

        // Thay thế Fragment hiện tại bằng FragmentHome
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frag_container002, fragmentHome)
                .commit();
    }

    // Load products into the list
//    private void loadProducts() {
//        productList.add(new Product("Black Forest Cake", "Cake", 1950, R.drawable.cake1));
//        productList.add(new Product("Royal Fudge Cake with Tobleron", "Cake", 2000, R.drawable.cake2));
//        productList.add(new Product("Classic Sansrival", "Cake", 2050, R.drawable.cake3));
//        productList.add(new Product("Customized Cake Rainbow", "Cake", 2100, R.drawable.cake4));
//
//        filteredProducts.addAll(productList);
//        productAdapter.notifyDataSetChanged();
//    }
}
