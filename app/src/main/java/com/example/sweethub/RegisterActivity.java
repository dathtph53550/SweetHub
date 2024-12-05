package com.example.sweethub;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.sweethub.Model.Response;
import com.example.sweethub.Model.User;
import com.example.sweethub.servers.HttpRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
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



        // Xử lý nút Đăng ký
        btnSignUp.setOnClickListener(v -> {
            RequestBody _username = RequestBody.create(MediaType.parse("multipart/form-data"),txtUsername.getText().toString().trim());
            RequestBody _password = RequestBody.create(MediaType.parse("multipart/form-data"),txtPass.getText().toString().trim());
            RequestBody _email = RequestBody.create(MediaType.parse("multipart/form-data"),txtEmail.getText().toString().trim());
            RequestBody _name = RequestBody.create(MediaType.parse("multipart/form-data"),txtName.getText().toString().trim());
            MultipartBody.Part multipartBody;
            if (file !=null) {
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"),file);
                multipartBody = MultipartBody.Part.createFormData("avartar",file.getName(),requestFile);
            }else {
                multipartBody = null;
            }
            Log.d("zzzzzz", "onClick: " + _name);
            httpRequest.callAPI().register(_username,_password,_email,_name,multipartBody).enqueue(responseUser);
//            }
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
//        if (ContextCompat.checkSelfPermission(RegisterActivity.this,
//                android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        Log.d("123123", "chooseAvatar: " +123123);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getImage.launch(intent);
//        }else {
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//
//        }
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

                        //binding.avatar.setImageURI(imageUri);

                        Glide.with(ivImg)
                                .load(imageUri)
                                .centerCrop()
                                .circleCrop()
                                .into(ivImg);

//                        Glide.with(RegisterActivity.this)
//                                .load(file)
//                                .thumbnail(Glide.with(RegisterActivity.this).load(R.drawable.baseline_broken_image_24))
//                                .centerCrop()
//                                .circleCrop()
////                                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                                .skipMemoryCache(true)
//                                .into(binding.avatar);
                    }
                }
            });

}
