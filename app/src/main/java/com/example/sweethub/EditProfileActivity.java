package com.example.sweethub;

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


        EditText editName = findViewById(R.id.editName);
        EditText editEmail = findViewById(R.id.editEmail);
        EditText editPhoneNumber = findViewById(R.id.editPhoneNumber);
        EditText editLocation = findViewById(R.id.editLocation);
        Button saveChangesButton = findViewById(R.id.saveChangesButton);

        editName.setText("Darren");
        editEmail.setText("example@gmail.com");
        editPhoneNumber.setText("0123456789");
        editLocation.setText("Vietnam");

        saveChangesButton.setOnClickListener(v -> {
            Toast.makeText(this, "Luu thanh cong", Toast.LENGTH_SHORT).show();
        });


    }
}