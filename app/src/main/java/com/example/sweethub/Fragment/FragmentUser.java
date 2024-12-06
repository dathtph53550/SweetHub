package com.example.sweethub.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sweethub.EditProfileActivity;
import com.example.sweethub.FeedBackActivity;
import com.example.sweethub.LoginActivity;
import com.example.sweethub.OrderHistoryActivity;
import com.example.sweethub.R;
import com.google.firebase.auth.FirebaseAuth;


public class FragmentUser extends Fragment {
    TextView tvName, tvEmail;
    ImageView ivImg;

    String data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            data = bundle.getString("email");
            // Sử dụng dữ liệu
            Log.d("aliiiiiiiii", "Received data: " + data);
        }
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvName = view.findViewById(R.id.ivName);
        tvEmail = view.findViewById(R.id.tvEmail);
        ivImg = view.findViewById(R.id.ivImg);


        tvName.setText(data);

        LinearLayout orderHistoryLayout = view.findViewById(R.id.orderHistoryLayout);
        orderHistoryLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), OrderHistoryActivity.class);
            startActivity(intent);
        });

        LinearLayout EditProfileLayout = view.findViewById(R.id.EditProfileLayout);
        EditProfileLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intent);
        });


        LinearLayout feedBackLayout = view.findViewById(R.id.feedBackLayout);
        feedBackLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), FeedBackActivity.class);
            intent.putExtra("userName", data);
            startActivity(intent);
        });

        LinearLayout btnLogOut = view.findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

    }
}