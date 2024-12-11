package com.nhom7.sweethub.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.nhom7.sweethub.Adapter.CategoryAdapter;
import com.nhom7.sweethub.Adapter.ProductAdapter1;
import com.nhom7.sweethub.Adapter.SlideshowAdapter;
import com.nhom7.sweethub.AddProductActivity;
import com.nhom7.sweethub.DetailProductActivity;
import com.nhom7.sweethub.Model.Category;
import com.nhom7.sweethub.Model.Product;
import com.nhom7.sweethub.Model.Response;
import com.nhom7.sweethub.R;
import com.nhom7.sweethub.UpdateProductActivity;
import com.nhom7.sweethub.servers.HttpRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class FragmentHome extends Fragment implements ProductAdapter1.FruitClick,CategoryAdapter.CateClick{
    private static final Logger log = LoggerFactory.getLogger(FragmentHome.class);
    FirebaseFirestore db;
    private ViewPager2 viewPagerSlideshow;
    private RecyclerView rvProducts;
    private ArrayList<Product> listProduct;
    private int currentPosition = 0;
    ProductAdapter1 adapterProduct;
    FloatingActionButton btnAdd;
    EditText edtSearch;
    FloatingActionButton btnAddCategory;

    RecyclerView rycCategory;
    ArrayList<Category> listCate;
    CategoryAdapter adapterCate;
    HttpRequest  httpRequest;
    String address,username;
    TextView tvGreeting;


    SharedPreferences sharedPreferences;
    String token;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if(bundle != null){
            address = bundle.getString("address");
            username = bundle.getString("username");
            Log.d("888", "onCreateView: " + username);
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
        edtSearch = view.findViewById(R.id.ettSearch);
        tvGreeting = view.findViewById(R.id.tvGreeting);

//        tvGreeting.setText("Hi,"+ username);


        SharedPreferences sharedPreferences = getContext().getSharedPreferences("INFO", Context.MODE_PRIVATE);
        int role = sharedPreferences.getInt("role", -1);
        Log.d("11111", "onViewCreated: " + role);
        // Hide FloatingActionButton for guests (role == 0)
        if (role == 0) {
            btnAdd.setVisibility(View.GONE);
            btnAddCategory.setVisibility(View.GONE);
        } else {
            btnAdd.setVisibility(View.VISIBLE);
            btnAddCategory.setVisibility(View.VISIBLE);
        }


        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần thay đổi gì ở đây
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String key = edtSearch.getText().toString().trim();

                if (key.isEmpty()) {
                    httpRequest.callAPI().getListFruit("Bearer " + token).enqueue(new Callback<Response<ArrayList<Product>>>() {
                        @Override
                        public void onResponse(Call<Response<ArrayList<Product>>> call, retrofit2.Response<Response<ArrayList<Product>>> response) {
                            if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                                listProduct.clear();
                                listProduct = response.body().getData();  // Cập nhật danh sách gốc
                                adapterProduct = new ProductAdapter1(getContext(), listProduct, FragmentHome.this);
                                rvProducts.setAdapter(adapterProduct);
                                adapterProduct.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFailure(Call<Response<ArrayList<Product>>> call, Throwable t) {
                        }
                    });
                } else {
                    ArrayList<Product> searchResults = new ArrayList<>();
                    for (Product product : listProduct) {
                        if (product.getName().toLowerCase().contains(key.toLowerCase())) {
                            searchResults.add(product);
                        }
                    }

                    if (!searchResults.isEmpty()) {
                        listProduct.clear();
                        listProduct.addAll(searchResults);
                        adapterProduct.notifyDataSetChanged();
                    } else {
                        listProduct.clear();
                        adapterProduct.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                // Không cần thay đổi gì ở đây
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
        intent.putExtra("email",username);
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
