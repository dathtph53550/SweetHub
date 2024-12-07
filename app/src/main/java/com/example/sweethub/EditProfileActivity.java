package com.example.sweethub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.sweethub.servers.ApiServices;

public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String userName = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");
        String email = intent.getStringExtra("email");
        String name = intent.getStringExtra("name");
        String avartar = intent.getStringExtra("avartar");




        EditText edtUsername = findViewById(R.id.editName);
        EditText edtPassword = findViewById(R.id.editEmail);
        EditText edtEmail = findViewById(R.id.editPhoneNumber);
        EditText edtName = findViewById(R.id.editLocation);
        Button saveChangesButton = findViewById(R.id.saveChangesButton);
        ImageView profilePicture = findViewById(R.id.profilePicture);


        edtUsername.setText(userName);
        edtPassword.setText(password);
        edtEmail.setText(email);
        edtName.setText(name);

        String newUrl = avartar.replace("localhost", ApiServices.IPv4);
        Glide.with(this)
            .load(newUrl).centerCrop().circleCrop()
            .into(profilePicture);

        saveChangesButton.setOnClickListener(v -> {
            Toast.makeText(this, "Luu thanh cong", Toast.LENGTH_SHORT).show();
        });


    }
}