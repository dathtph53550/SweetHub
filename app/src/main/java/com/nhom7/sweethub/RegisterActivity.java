package com.nhom7.sweethub;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.nhom7.sweethub.Model.Response;
import com.nhom7.sweethub.Model.User;
import com.nhom7.sweethub.servers.HttpRequest;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class RegisterActivity extends AppCompatActivity {
    TextInputEditText txtEmail, txtPass, txtUsername,txtName;
    Button btnSignUp;
    TextView tvSignIn;
    ImageView ivGoogle, iv_guest;
    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private File file;
    ImageView ivImg;
    HttpRequest httpRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        httpRequest = new HttpRequest();
        // Ánh xạ các thành phần giao diện
        txtEmail = findViewById(R.id.txtEmail);
        txtPass = findViewById(R.id.txtPass);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvSignIn = findViewById(R.id.tvSignIn);
        ivGoogle = findViewById(R.id.ivGoogle);
        mAuth = FirebaseAuth.getInstance();
        txtUsername = findViewById(R.id.txtUsername);
        txtName = findViewById(R.id.txtName);
        ivImg = findViewById(R.id.ivImg);

        ivImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });



        btnSignUp.setOnClickListener(v -> {
            // Lấy dữ liệu từ các trường nhập liệu
            String username = txtUsername.getText().toString().trim();
            String password = txtPass.getText().toString().trim();
            String email = txtEmail.getText().toString().trim();
            String name = txtName.getText().toString().trim();

                if(username.isEmpty() || password.isEmpty() || email.isEmpty() || name.isEmpty()) {
                    if(username.isEmpty()) txtUsername.setError("Vui lòng nhập tên đăng nhập!!");
                    if(password.isEmpty()) txtPass.setError("Vui lòng nhập mật khẩu!!!!");
                    if(email.isEmpty()) txtEmail.setError("Vui lòng nhập email!!!");
                    if(name.isEmpty()) txtName.setError("Vui lòng nhập tên!!!");
                    return;
                }

                else if (password.length() < 6) {
                    txtPass.setError("Mật khẩu không được để trống và phải có ít nhất 6 ký tự");
                    txtPass.requestFocus();
                    return;
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    txtEmail.setError("Email không hợp lệ");
                    txtEmail.requestFocus();
                    return;
                } else if(file == null) {
                    Toast.makeText(RegisterActivity.this, "Vui lòng chọn ảnh đại diện", Toast.LENGTH_SHORT).show();
                    return;
                }

            // Tạo các RequestBody cho API
            RequestBody _username = RequestBody.create(MediaType.parse("multipart/form-data"), username);
            RequestBody _password = RequestBody.create(MediaType.parse("multipart/form-data"), encodeToBase64(password));
            RequestBody _email = RequestBody.create(MediaType.parse("multipart/form-data"), email);
            RequestBody _name = RequestBody.create(MediaType.parse("multipart/form-data"), name);

            // Xử lý file avatar (nếu có)
            MultipartBody.Part multipartBody;
            if (file != null) {
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                multipartBody = MultipartBody.Part.createFormData("avartar", file.getName(), requestFile);
            } else {
                multipartBody = null;
            }

            // Gửi yêu cầu đến API
            httpRequest.callAPI().register(_username, _password, _email, _name, multipartBody).enqueue(responseUser);
        });



        // Chuyển sang màn hình Đăng nhập
        tvSignIn.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    Callback<Response<User>> responseUser = new Callback<Response<User>>() {
        @Override
        public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
            if (response.isSuccessful()) {
                Log.d("123123", "onResponse: " + response.body().getStatus());
                if (response.body().getStatus() ==200) {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                }else {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thất bại lỗi!" + response.body().getStatus(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<User>> call, Throwable t) {
            Log.d("zzzz", "onFailure: " + t.getMessage());
        }
    };




    private void chooseImage() {
        Log.d("123123", "chooseAvatar: " +123123);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getImage.launch(intent);

    }

    private File createFileFormUri (Uri path, String name) {
        File _file = new File(RegisterActivity.this.getCacheDir(), name + ".png");
        try {
            InputStream in = RegisterActivity.this.getContentResolver().openInputStream(path);
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

    public static String encodeToBase64(String input) {
        return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
    }

    ActivityResultLauncher<Intent> getImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == Activity.RESULT_OK) {
                        Intent data = o.getData();
                        Uri imageUri = data.getData();
                        Log.d("zzzz", "onActivityResult: " + imageUri);
                        Log.d("RegisterActivity", imageUri.toString());

                        Log.d("123123", "onActivityResult: "+data);

                        file = createFileFormUri(imageUri, "avatar");


                        Glide.with(ivImg)
                                .load(imageUri)
                                .centerCrop()
                                .circleCrop()
                                .into(ivImg);

                    }
                }
            });

}
