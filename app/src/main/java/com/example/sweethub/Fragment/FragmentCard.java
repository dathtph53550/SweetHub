package com.example.sweethub.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sweethub.Adapter.AdapterCard;
import com.example.sweethub.Model.Cart;
import com.example.sweethub.Model.Response;
import com.example.sweethub.Payment;
import com.example.sweethub.R;
import com.example.sweethub.servers.HttpRequest;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class FragmentCard extends Fragment {

    RecyclerView recyclerViewCart;
    TextView totall;
    Button checkOut;
    AdapterCard adapter;
    ArrayList<Cart> list;
    HttpRequest httpRequest;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewCart = view.findViewById(R.id.recyclerViewCart);
        httpRequest = new HttpRequest();
        list = new ArrayList<>();
        adapter = new AdapterCard(getContext(),list);
        totall = view.findViewById(R.id.cartTotalText);
        checkOut = view.findViewById(R.id.checkOutButton);
        recyclerViewCart.setAdapter(adapter);

        httpRequest.callAPI().getListCart().enqueue(new Callback<Response<ArrayList<Cart>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Cart>>> call, retrofit2.Response<Response<ArrayList<Cart>>> response) {
                list.clear();
                list.addAll(response.body().getData());
                Log.d("pppppp", "onResponse: " + list.size());
                // Tính tổng giá trị
                double totalPrice = 0.0;
                for (Cart cart : list) {
                    try {
                        // Nếu giá trị là String, cần chuyển đổi sang double
                        totalPrice += Double.parseDouble(cart.getPrice()) * Integer.parseInt(cart.getQuantity());
                    } catch (NumberFormatException e) {
                        Log.e("TotalError", "Invalid price or quantity: " + e.getMessage());
                    }
                }
                Log.d("zzzzzzzzzz", "onViewCreated: " + list.get(list.size() - 1).getName());


                // Cập nhật TextView
                totall.setText("Total: " + totalPrice + " đ");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Response<ArrayList<Cart>>> call, Throwable t) {
                Log.d("ppppp", "onFailure: " + t.getMessage());
            }

        });


        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Payment.class));
            }
        });


    }


}
