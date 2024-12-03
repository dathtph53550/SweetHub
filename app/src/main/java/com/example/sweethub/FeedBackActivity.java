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

public class FeedBackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_feed_back);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        EditText feedbackInput = findViewById(R.id.feedbackInput);
        Button sendFeedbackButton = findViewById(R.id.sendFeedbackButton);

        sendFeedbackButton.setOnClickListener(v -> {
            String feedback = feedbackInput.getText().toString().trim();
            if (feedback.isEmpty()) {
                Toast.makeText(this, "Please enter your feedback!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
                feedbackInput.setText("");
            }
        });

    }
}