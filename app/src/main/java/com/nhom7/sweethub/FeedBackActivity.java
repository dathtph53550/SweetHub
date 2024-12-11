package com.nhom7.sweethub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.nhom7.sweethub.Model.FeedBack;
import com.nhom7.sweethub.Model.Response;
import com.nhom7.sweethub.servers.HttpRequest;

import retrofit2.Call;
import retrofit2.Callback;

public class FeedBackActivity extends AppCompatActivity {

    HttpRequest httpRequest;

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

        Intent intent = getIntent();

        String email = intent.getStringExtra("userName");
        Log.d("mmmmmm", "onCreate: " + email);

        httpRequest = new HttpRequest();

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        EditText feedbackInput = findViewById(R.id.feedbackInput);
        Button sendFeedbackButton = findViewById(R.id.sendFeedbackButton);

        sendFeedbackButton.setOnClickListener(v -> {
            String edtfeedback = feedbackInput.getText().toString().trim();
            if (edtfeedback.isEmpty()) {
                Toast.makeText(this, "Please enter your feedback!", Toast.LENGTH_SHORT).show();
            } else {
                FeedBack feedBack = new FeedBack();
                feedBack.setNoi_dung(edtfeedback);
                feedBack.setUser(email);
                httpRequest.callAPI().addFeedBack(feedBack).enqueue(new Callback<Response<FeedBack>>() {
                    @Override
                    public void onResponse(Call<Response<FeedBack>> call, retrofit2.Response<Response<FeedBack>> response) {
                        if(response.isSuccessful()){
                            if(response.body().getStatus() == 200){
                                Toast.makeText(FeedBackActivity.this, "Feedback sent successfully!", Toast.LENGTH_SHORT).show();
                                feedbackInput.setText("");
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<Response<FeedBack>> call, Throwable t) {

                    }
                });

            }
        });

    }
}