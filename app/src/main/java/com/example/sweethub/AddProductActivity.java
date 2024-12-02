package com.example.sweethub;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.sweethub.Fragment.FragmentHome;
import com.example.sweethub.Model.Category;
import com.example.sweethub.Model.Product;
import com.example.sweethub.Model.Response;
import com.example.sweethub.servers.HttpRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class AddProductActivity extends AppCompatActivity {

    HttpRequest httpRequest;
    EditText etName, etQuantity, etPrice,edtDescribe;
    Spinner spinnerCompany;
    Button btnAdd,btnBack;
    ImageView  ivChoseImage;
    String id_category;
    ArrayList<Product> listProduct;
    ArrayList<File> listImage;
    ArrayList<Uri> imageUris = new ArrayList<>();
    ArrayList<Category> listCategory;
    RadioGroup radioGroup;
    RadioButton radioChuaGiao, radioDangGiao, radioDaGiao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        httpRequest = new HttpRequest();
        etName = findViewById(R.id.etName);
        etQuantity = findViewById(R.id.etQuantity);
        etPrice = findViewById(R.id.etPrice);
        edtDescribe = findViewById(R.id.etDescribe);
        radioGroup = findViewById(R.id.radioGroupStatus);
        btnAdd = findViewById(R.id.btnAdd);
        ivChoseImage = findViewById(R.id.ivChoseImage);
        listImage = new ArrayList<>();
        listCategory = new ArrayList<>();
        spinnerCompany = findViewById(R.id.spinnerCompany);
        btnBack = findViewById(R.id.btnBack);
        radioChuaGiao = findViewById(R.id.radioChuaGiao);
        radioDangGiao = findViewById(R.id.radioDangGiao);
        radioDaGiao = findViewById(R.id.radioDaGiao);

        radioGroup.check(R.id.radioChuaGiao);

        httpRequest.callAPI().getListCategory().enqueue(getCategoryAPI);
        spinnerCompany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_category = listCategory.get(position).getId();
                Log.d("idd", "onItemSelected: " + id_category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerCompany.setSelection(0);


        //
        ivChoseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String , RequestBody> mapRequestBody = new HashMap<>();
                String _name = etName.getText().toString().trim();
                String _quantity = etQuantity.getText().toString().trim();
                String _price = etPrice.getText().toString().trim();
//                String _status = etStatus.getText().toString().trim();
                String _describe = edtDescribe.getText().toString().trim();

                String _status = "";
                if(radioChuaGiao.isChecked()){
                    _status = "0";
                }
                else if(radioDangGiao.isChecked()){
                    _status = "1";
                }
                else{
                    _status = "2";
                }

                Log.d("bbbbbb", "onClick: " + _status);

                mapRequestBody.put("name", getRequestBody(_name));
                mapRequestBody.put("quantity", getRequestBody(_quantity));
                mapRequestBody.put("price", getRequestBody(_price));
                mapRequestBody.put("status", getRequestBody(_status));
                mapRequestBody.put("describe", getRequestBody(_describe));
                mapRequestBody.put("id_category", getRequestBody(id_category));
                Toast.makeText(AddProductActivity.this, " " + _name + " " + _quantity + " " + _price + " " + _status + " " + _describe, Toast.LENGTH_SHORT).show();
                ArrayList<MultipartBody.Part> _ds_image = new ArrayList<>();
                listImage.forEach(file1 -> {
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"),file1);
                    MultipartBody.Part multipartBodyPart = MultipartBody.Part.createFormData("image", file1.getName(),requestFile);
                    _ds_image.add(multipartBodyPart);
                });
                httpRequest.callAPI().addProductWithFileImage(mapRequestBody, _ds_image).enqueue(responseFruit);
            }
        });

    }

    Callback<Response<Product>> responseFruit = new Callback<Response<Product>>() {
        @Override
        public void onResponse(Call<Response<Product>> call, retrofit2.Response<Response<Product>> response) {
            if (response.isSuccessful()) {
                Log.d("123123", "onResponse: " + response.body().getStatus());
                if (response.body().getStatus()==200) {
                    Toast.makeText(AddProductActivity.this, "Thêm mới thành công", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(AddProductActivity.this, HomeActivity.class);
//                    intent.putExtra("navigateTo", "FragmentHome"); // Truyền dữ liệu để điều hướng
//                    intent.putExtra("newProduct", response.body().getData()); // Nếu cần gửi dữ liệu sản phẩm mới
//                    startActivity(intent);
                    finish();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Product>> call, Throwable t) {
            Toast.makeText(AddProductActivity.this, "Thêm  thành công", Toast.LENGTH_SHORT).show();
            Log.e("zzzzzzzzzz", "onFailure: "+t.getMessage());
        }
    };

    private void chooseImage() {
//        if (ContextCompat.checkSelfPermission(RegisterActivity.this,
//                android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        Log.d("123123", "chooseAvatar: " +123123);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        getImage.launch(intent);
//        }else {
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//
//        }
    }

    private RequestBody getRequestBody(String value) {
        return RequestBody.create(MediaType.parse("multipart/form-data"),value);
    }

    ActivityResultLauncher<Intent> getImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == Activity.RESULT_OK) {
                        // Clear danh sách cũ
                        listImage.clear();
                        imageUris.clear();

                        Intent data = o.getData();
                        if (data != null) {
                            // Nếu chọn nhiều ảnh
                            if (data.getClipData() != null) {
                                int count = data.getClipData().getItemCount();
                                for (int i = 0; i < count; i++) {
                                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                                    imageUris.add(imageUri);

                                    // Thêm File vào listImage
                                    File file = createFileFormUri(imageUri, "image" + i);
                                    listImage.add(file);
                                }
                            } else if (data.getData() != null) { // Nếu chỉ chọn 1 ảnh
                                Uri imageUri = data.getData();
                                imageUris.add(imageUri);

                                // Thêm File vào listImage
                                File file = createFileFormUri(imageUri, "image");
                                listImage.add(file);
                            }

                            // Hiển thị tất cả ảnh đã chọn
                            displaySelectedImages();
                        }
                    }
                }
            });

    private void displaySelectedImages() {
        // Lấy container LinearLayout
        LinearLayout imageContainer = findViewById(R.id.imageContainer);

        // Xóa tất cả các ảnh cũ
        imageContainer.removeAllViews();

        // Dùng Glide để load danh sách ảnh mới
        for (Uri uri : imageUris) {
            ImageView imageView = new ImageView(this);
            Log.d("image", "displaySelectedImages: "+ uri);
            Glide.with(this)
                    .load(uri)
                    .override(200, 350) // Kích thước ảnh nhỏ hơn
                    .centerCrop()
                    .thumbnail(Glide.with(this).load(R.drawable.baseline_broken_image_24))
                    .into(imageView);

            // Thêm ImageView mới vào container
            imageContainer.addView(imageView);
        }
    }



    private File createFileFormUri (Uri path, String name) {
        File _file = new File(AddProductActivity.this.getCacheDir(), name + ".png");
        try {
            InputStream in = AddProductActivity.this.getContentResolver().openInputStream(path);
            OutputStream out = new FileOutputStream(_file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) >0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
            Log.d("123123", "createFileFormUri: " +_file);
            return _file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    Callback<Response<ArrayList<Category>>> getCategoryAPI = new Callback<Response<ArrayList<Category>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Category>>> call, retrofit2.Response<Response<ArrayList<Category>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    listCategory = response.body().getData();
                    String[] items = new String[listCategory.size()];

                    for (int i = 0; i< listCategory.size(); i++) {
                        items[i] = listCategory.get(i).getName();
                    }

                    ArrayAdapter<String> adapterSpin = new ArrayAdapter<>(AddProductActivity.this, android.R.layout.simple_spinner_item, items);
                    spinnerCompany.setAdapter(adapterSpin);
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Category>>> call, Throwable t) {

        }
    };
}