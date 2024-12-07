package com.example.sweethub.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.sweethub.EditProfileActivity;
import com.example.sweethub.FeedBackActivity;
import com.example.sweethub.LoginActivity;
import com.example.sweethub.OrderActivity;
import com.example.sweethub.OrderHistoryActivity;
import com.example.sweethub.R;
import com.example.sweethub.servers.ApiServices;
import com.google.firebase.auth.FirebaseAuth;


public class FragmentUser extends Fragment {
    TextView tvName, tvEmail;
    ImageView ivImg;
    int role;
    String username,password,email,name,avartar,id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getString("id");
            username = bundle.getString("username");
            password = bundle.getString("password");
            email = bundle.getString("email");
            name = bundle.getString("name");
            avartar = bundle.getString("avartar");
            role = bundle.getInt("role");
            // Sử dụng dữ liệu
            Log.d("0000", "onCreateView: " + username + " " + password + " " + email + " " + name + " " + avartar);
        }

        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(role == 3){
            Toast.makeText(getContext(), "Bạn cần phải đăng nhập !!!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            return;
        }

        tvName = view.findViewById(R.id.ivName);
        tvEmail = view.findViewById(R.id.tvEmail);
        ivImg = view.findViewById(R.id.ivImg);

        tvName.setText(username);
        tvEmail.setText(email);
        String newUrl = avartar.replace("localhost", ApiServices.IPv4);

        Log.d("99", "onViewCreated: " + newUrl);

        Glide.with(this).
                load(newUrl)
                .centerCrop()
                .circleCrop()
                .into(ivImg);


        LinearLayout orderHistoryLayout = view.findViewById(R.id.orderHistoryLayout);
        orderHistoryLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), OrderHistoryActivity.class);
            startActivity(intent);
        });

        LinearLayout EditProfileLayout = view.findViewById(R.id.EditProfileLayout);
        EditProfileLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("username", username);
            intent.putExtra("password", password);
            intent.putExtra("email", email);
            intent.putExtra("name", name);
            intent.putExtra("avartar", avartar);
            startActivity(intent);
        });


        LinearLayout feedBackLayout = view.findViewById(R.id.feedBackLayout);
        feedBackLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), FeedBackActivity.class);
//            intent.putExtra("userName", data);
            startActivity(intent);
        });

        LinearLayout btnLogOut = view.findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        LinearLayout order = view.findViewById(R.id.order);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), OrderActivity.class));
            }
        });

    }
}