package com.example.sweethub.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.sweethub.Adapter.CategoryAdapter;
import com.example.sweethub.Adapter.ProductAdapter;
import com.example.sweethub.Adapter.ProductAdapter1;
import com.example.sweethub.Adapter.SlideshowAdapter;
import com.example.sweethub.AddProductActivity;
import com.example.sweethub.DetailProductActivity;
import com.example.sweethub.HomeActivity;
import com.example.sweethub.Model.Category;
import com.example.sweethub.Model.Product;
import com.example.sweethub.Model.Response;
import com.example.sweethub.R;
import com.example.sweethub.UpdateProductActivity;
import com.example.sweethub.servers.HttpRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class FragmentHome extends Fragment implements ProductAdapter1.FruitClick{
    FirebaseFirestore db;
    private EditText etSearch;
    private ViewPager2 viewPagerSlideshow;
    private RecyclerView rvProducts;
    private ArrayList<Product> listProduct;
    private int currentPosition = 0;
    ProductAdapter1 adapterProduct;
    FloatingActionButton btnAdd;

    RecyclerView rycCategory;
    ArrayList<Category> listCate;
    CategoryAdapter adapterCate;
    HttpRequest  httpRequest;
    String data;
    TextView tvGreeting;


    SharedPreferences sharedPreferences;
    String token;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            data = bundle.getString("email");
            // Sử dụng dữ liệu
            Log.d("aliiiiiiiii", "Received data: " + data);
        }
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        FirebaseApp.initializeApp(getContext());
        db = FirebaseFirestore.getInstance();
        httpRequest = new HttpRequest();
        btnAdd = view.findViewById(R.id.btnAdd);
        //getListCategory nhe
        rycCategory = view.findViewById(R.id.rycCategory);
        listCate = new ArrayList<>();
        adapterCate = new CategoryAdapter(getContext(),listCate);
        rycCategory.setAdapter(adapterCate);
        etSearch = view.findViewById(R.id.etSearch);
        tvGreeting = view.findViewById(R.id.tvGreeting);

        tvGreeting.setText("Hi, "+ data);

//        Intent intent = new Intent(getActivity(), AddProductActivity.class);


        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    String key = etSearch.getText().toString().trim();
                    httpRequest.callAPI().searchProduct(key).enqueue(new Callback<Response<ArrayList<Product>>>() {
                        @Override
                        public void onResponse(Call<Response<ArrayList<Product>>> call, retrofit2.Response<Response<ArrayList<Product>>> response) {
                            if(response.isSuccessful()){
                                if(response.body().getStatus() == 200){
                                    listProduct.clear();
                                    listProduct = response.body().getData();
                                    adapterProduct = new ProductAdapter1(getContext(),listProduct,FragmentHome.this);
                                    rvProducts.setAdapter(adapterProduct);
                                    adapterProduct.notifyDataSetChanged();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Response<ArrayList<Product>>> call, Throwable t) {

                        }
                    });

                }
                return false;
            }
        });

        //GetListCAtegory
        httpRequest.callAPI().getListCategory().enqueue(new Callback<Response<ArrayList<Category>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Category>>> call, retrofit2.Response<Response<ArrayList<Category>>> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus() == 200){
                        listCate = response.body().getData();
                        adapterCate = new CategoryAdapter(getContext(),listCate);
                        rycCategory.setAdapter(adapterCate);
                        adapterCate.notifyDataSetChanged();
                        Log.d("zzzz", "onResponse: " + listCate.size());
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<Category>>> call, Throwable t) {

            }
        });




        etSearch = view.findViewById(R.id.etSearch);
        viewPagerSlideshow = view.findViewById(R.id.viewPagerSlideshow);
        rvProducts = view.findViewById(R.id.rvProducts);

        sharedPreferences = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token","");




        rvProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvProducts.setNestedScrollingEnabled(true);
        listProduct = new ArrayList<>();
        adapterProduct = new ProductAdapter1(getContext(),listProduct,FragmentHome.this);

        rvProducts.setAdapter(adapterProduct);

        Bundle args = getArguments();
        if (args != null) {
            Product newProduct = (Product) args.getSerializable("newProduct");
            if (newProduct != null) {
                listProduct.add(newProduct); // Thêm sản phẩm mới vào danh sách
                adapterProduct.notifyDataSetChanged(); // Cập nhật RecyclerView/ListView
            }
        }

//        adapterProduct.notifyDataSetChanged();
        //Get List Product
        httpRequest.callAPI().getListFruit("Bearer" + token).enqueue(new Callback<Response<ArrayList<Product>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Product>>> call, retrofit2.Response<Response<ArrayList<Product>>> response) {
                if (response.isSuccessful()){
                    if(response.body().getStatus() == 200){
                        listProduct.clear();
                        listProduct = response.body().getData();
                        adapterProduct = new ProductAdapter1(getContext(),listProduct,FragmentHome.this);
                        rvProducts.setAdapter(adapterProduct);
                        adapterProduct.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<Product>>> call, Throwable t) {

            }
        });

        // Setup Event Listeners for Buttons (Category Filter)
//        btnCake.setOnClickListener(v -> filterProductsByCategory("Cake"));
//        btnDonuts.setOnClickListener(v -> filterProductsByCategory("Donuts"));
//        btnCookies.setOnClickListener(v -> filterProductsByCategory("Cookies"));
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddProductActivity.class));
            }
        });




//        docDuLieu();
        // Setup slideshow
        setupSlideshow();

        // Load product data
//        loadProducts();
        // Search functionality

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
//            Toast.makeText(getContext(), "No products found in " + category, Toast.LENGTH_SHORT).show();
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


//    Callback<Response<ArrayList<Category>>> getListCate =new Callback<Response<ArrayList<Category>>>() {
//        @Override
//        public void onResponse(Call<Response<ArrayList<Category>>> call, retrofit2.Response<Response<ArrayList<Category>>> response) {
//            if (response.isSuccessful()) {
//                if (response.body().getStatus() == 200) {
//                    listCate = response.body().getData();
////                    list.clear();
////                    list.addAll(list);
//                    getData(listCate);
////                    adapter.notifyDataSetChanged();
//                    Log.d("zz", "onResponse: "+ "da vo day");
//
//                    Log.d("zz", "onResponse: "+ listCate.size());
//                }
//                else {
//                    Log.d("zz", "onResponse: "+ "khong thi day");
//                }
//            }
//        }
//
//        @Override
//        public void onFailure(Call<Response<ArrayList<Category>>> call, Throwable t) {
//            Log.d("loi", "onFailure: " + t.getMessage());
//        }
//    };






    // Load products into the list
//    private void loadProducts() {
//        Product product = new Product();
//        product.setName("Baba");
//        productList.add(product);
//
////        Category category = new Category();
////        category.setName("Cake");
////        listCate.add(category);
////
////        Category category1 = new Category();
////        category1.setName("Cake");
////        listCate.add(category1);
//
////        productList.add(new Product("Royal Fudge Cake with Tobleron", "Cake", 2000, R.drawable.cake2));
////        productList.add(new Product("Classic Sansrival", "Cake", 2050, R.drawable.cake3));
////        productList.add(new Product("Customized Cake Rainbow", "Cake", 2100, R.drawable.cake4));
//
//        filteredProducts.addAll(productList);
//        Log.d("mmm", "loadProducts: " + filteredProducts.size());
//        productAdapter.notifyDataSetChanged();
//
//    }

    @Override
    public void delete(Product product) {

    }

    @Override
    public void edit(Product product) {
        Intent intent =new Intent(getContext(), UpdateProductActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);
    }

    @Override
    public void showDetail(Product product) {
        Intent intent = new Intent(getContext(), DetailProductActivity.class);
        intent.putExtra("product", product);
        intent.putExtra("email",data);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        httpRequest.callAPI().getListFruit("Bearer" + token).enqueue(new Callback<Response<ArrayList<Product>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Product>>> call, retrofit2.Response<Response<ArrayList<Product>>> response) {
                if (response.isSuccessful()){
                    if(response.body().getStatus() == 200){
                        listProduct.clear();
                        listProduct = response.body().getData();
                        adapterProduct = new ProductAdapter1(getContext(),listProduct,FragmentHome.this);
                        rvProducts.setAdapter(adapterProduct);
                        adapterProduct.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<Product>>> call, Throwable t) {

            }
        });

    }

    private ActivityResultLauncher<Intent> addProductLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Product newProduct = (Product) result.getData().getSerializableExtra("newProduct");
                    if (newProduct != null) {
                        // Thêm sản phẩm vào danh sách và làm mới RecyclerView
                        listProduct.add(newProduct);
                        adapterProduct.notifyDataSetChanged();
                    }
                }
            }
    );
}
