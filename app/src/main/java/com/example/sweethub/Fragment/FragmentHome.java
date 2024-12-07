package com.example.sweethub.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
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

public class FragmentHome extends Fragment implements ProductAdapter1.FruitClick,CategoryAdapter.CateClick{
    FirebaseFirestore db;
    private EditText etSearch;
    private ViewPager2 viewPagerSlideshow;
    private RecyclerView rvProducts;
    private ArrayList<Product> listProduct;
    private int currentPosition = 0;
    ProductAdapter1 adapterProduct;
    FloatingActionButton btnAdd;
    FloatingActionButton btnAddCategory;

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
        btnAddCategory = view.findViewById(R.id.btnAddCategory);
        //getListCategory nhe
        rycCategory = view.findViewById(R.id.rycCategory);
        listCate = new ArrayList<>();
        adapterCate = new CategoryAdapter(getContext(),listCate,this);
        rycCategory.setAdapter(adapterCate);
        etSearch = view.findViewById(R.id.etSearch);
        tvGreeting = view.findViewById(R.id.tvGreeting);

        tvGreeting.setText("Hi, "+ data);

//        Intent intent = new Intent(getActivity(), AddProductActivity.class);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("INFO", Context.MODE_PRIVATE);
        int role = sharedPreferences.getInt("role", -1);

        // Hide FloatingActionButton for guests (role == 0)
        if (role == 0) {
            btnAdd.setVisibility(View.GONE);
            btnAddCategory.setVisibility(View.GONE);
        } else {
            btnAdd.setVisibility(View.VISIBLE);
            btnAddCategory.setVisibility(View.VISIBLE);
        }


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
                        adapterCate = new CategoryAdapter(getContext(),listCate,FragmentHome.this);
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

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddProductActivity.class));
            }
        });

        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAddCategory();
            }
        });





        setupSlideshow();



    }


    private void setupSlideshow() {
        List<Integer> images = new ArrayList<>();
        images.add(R.drawable.cake_banner); // Correct usage of resource ID
        images.add(R.drawable.banner2);
        images.add(R.drawable.banner3);

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


    void showDialogAddCategory(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_category,null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        //anh xa
        EditText edtCategory = view.findViewById(R.id.edtCategory);
        Button btnAdd = view.findViewById(R.id.btnThem);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtCategory.getText().toString();
                if(name.isEmpty()){
                    Toast.makeText(getContext(), "Vui lòng nhập tên category", Toast.LENGTH_SHORT).show();
                }
                else {
                    Category category = new Category();
                    category.setName(name);
                    httpRequest.callAPI().addCategory(category).enqueue(new Callback<Response<ArrayList<Category>>>() {
                        @Override
                        public void onResponse(Call<Response<ArrayList<Category>>> call, retrofit2.Response<Response<ArrayList<Category>>> response) {
                            if(response.isSuccessful()){
                                if(response.body().getStatus() == 200){
                                    listCate.clear();
                                    listCate = response.body().getData();
                                    adapterCate = new CategoryAdapter(getContext(),listCate,FragmentHome.this);
                                    rycCategory.setAdapter(adapterCate);
                                    adapterCate.notifyDataSetChanged();
                                    Toast.makeText(getContext(), "Thêm Category Thành Công !!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();

                                }
                            }
                            else Toast.makeText(getContext(), "Thêm Category không thành công !!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<Response<ArrayList<Category>>> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }



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

    @Override
    public void data(Category category) {
        httpRequest.callAPI().getProductsByCategory(category.getId()).enqueue(new Callback<Response<ArrayList<Product>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Product>>> call, retrofit2.Response<Response<ArrayList<Product>>> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus() == 200){
                        listProduct.clear();
                        listProduct = response.body().getData();
                        adapterProduct = new ProductAdapter1(getContext(),listProduct,FragmentHome.this);
                        rvProducts.setAdapter(adapterProduct);
                        Log.d("fffff", "onResponse: " + listProduct.size());
                        adapterProduct.notifyDataSetChanged();
                    }
                }
                else {
                    listProduct.clear();
                    adapterProduct.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<Product>>> call, Throwable t) {

            }
        });

    }
}
