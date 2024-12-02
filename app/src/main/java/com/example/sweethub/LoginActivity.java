package com.example.sweethub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    TextInputEditText txtUser, txtPass;
    Button btnSignIn;
    TextView tvForgotPassword, tvSignUp;
    ImageView ivGoogle, iv_guest;
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    HttpRequest httpRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ các thành phần giao diện
        txtUser = findViewById(R.id.txtEmail);
        txtPass = findViewById(R.id.txtPass);
        btnSignIn = findViewById(R.id.btnSignIn);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvSignUp = findViewById(R.id.tvSignUp);
        ivGoogle = findViewById(R.id.ivGoogle);
        iv_guest = findViewById(R.id.iv_guest);
        httpRequest = new HttpRequest();

        // Cấu hình Google Sign-In



        mAuth = FirebaseAuth.getInstance();

        // Đăng nhập bằng Google
        ivGoogle.setOnClickListener(v -> signInWithGoogle());

        // Đăng nhập với tư cách khách
        iv_guest.setOnClickListener(v -> loginAsGuest());

        // Đăng nhập bằng email và mật khẩu
        btnSignIn.setOnClickListener(v -> {

            User user = new User();
            String _username = txtUser.getText().toString().trim();
            String _password = txtPass.getText().toString().trim();
            user.setUsername(_username);
            user.setPassword(_password);
            httpRequest.callAPI().login(user).enqueue(new Callback<Response<User>>() {
                @Override
                public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() ==200) {
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            SharedPreferences sharedPreferences = getSharedPreferences("INFO",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("token", response.body().getToken());
                            editor.putString("refreshToken", response.body().getRefreshToken());
                            editor.putString("id", response.body().getData().get_id());
                            editor.apply();
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            intent.putExtra("userName",response.body().getData().getUsername());
                            startActivity(intent);
                        }
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Đăng nhập không thành công !!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Response<User>> call, Throwable t) {

                }
            });

        });

        // Quên mật khẩu
        tvForgotPassword.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class)));

        // Chuyển sang màn hình Đăng ký
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void loginAsGuest() {
        mAuth.signInAnonymously().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                Toast.makeText(LoginActivity.this, "Log in as guest successfully!!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                intent.putExtra("userName","Guest");
                intent.putExtra("role", 1);
                startActivity(intent);
            } else {
                Log.w("LoginActivity", "signInAnonymously:failure", task.getException());
                Toast.makeText(LoginActivity.this, "Đăng nhập với tư cách khách thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w("LoginActivity", "Google sign in failed", e);
                Toast.makeText(this, "Đăng nhập Google thất bại!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                // Đăng nhập thành công, chuyển hướng hoặc cập nhật giao diện
                Toast.makeText(this, "Đăng nhập thành công !!" + user.getEmail(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                intent.putExtra("userName",user.getEmail());
                intent.putExtra("role", 0);
                startActivity(intent);
            } else {
                Log.w("LoginActivity", "signInWithCredential:failure", task.getException());
                Toast.makeText(this, "Đăng nhập Google thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
