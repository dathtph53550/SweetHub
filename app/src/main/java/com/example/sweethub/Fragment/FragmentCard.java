package com.example.sweethub.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sweethub.Adapter.AdapterCard;
import com.example.sweethub.Adapter.AdapterOrderCard;
import com.example.sweethub.Model.Cart;
import com.example.sweethub.Model.Response;
import com.example.sweethub.PaidSuccess;
import com.example.sweethub.Payment;
import com.example.sweethub.R;
import com.example.sweethub.servers.HttpRequest;

import java.text.NumberFormat;
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
    double totalPrice = 0.0;
    int quantity = 0;


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
                totalPrice = 0.0;
                quantity = 0;
                for (Cart cart : list) {
                    try {
                        // Nếu giá trị là String, cần chuyển đổi sang double
                        totalPrice += Double.parseDouble(cart.getPrice()) * Integer.parseInt(cart.getQuantity());
                        quantity += Integer.parseInt(cart.getQuantity());
                    } catch (NumberFormatException e) {
                        Log.e("TotalError", "Invalid price or quantity: " + e.getMessage());
                    }
                }
                Log.d("oooooooooo", "onResponse: " + quantity);

                Locale vnLocale = new Locale("vi", "VN");
                NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(vnLocale);
                // Cập nhật TextView
                totall.setText("Total: " + currencyFormat.format(totalPrice));


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
                openDialogCart();
            }
        });


    }

    void openDialogCart(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = ((Activity)getContext()).getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_checkout,null);
        builder.setView(v);
        AlertDialog dialog = builder.create();
        dialog.show();



        //anh xa
        RecyclerView ryc = v.findViewById(R.id.ryc);
        TextView totalAmount = v.findViewById(R.id.totalAmount);
        Button orderButton = v.findViewById(R.id.orderButton);
        TextView quantityy = v.findViewById(R.id.quantity);
        EditText edtAddress = v.findViewById(R.id.edtAddress);
        AdapterOrderCard adapterOrderCard = new AdapterOrderCard(getContext(),list);

        totalAmount.setText(totalPrice + " đ");
        quantityy.setText(quantity+"");

        httpRequest.callAPI().getListCart().enqueue(new Callback<Response<ArrayList<Cart>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Cart>>> call, retrofit2.Response<Response<ArrayList<Cart>>> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus() == 200){
                        list.clear();
                        list.addAll(response.body().getData());
                        ryc.setAdapter(adapterOrderCard);
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<Cart>>> call, Throwable t) {

            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = edtAddress.getText().toString();
                if(address.isEmpty()){
                    Toast.makeText(getContext(), "Vui lòng nhập địa chỉ !!!", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(getContext(), Payment.class);
                intent.putExtra("amount",totalPrice);
                intent.putExtra("quantity", quantity);
                intent.putExtra("address",address);
                Log.d("6666", "onClick: " + address);
                intent.putParcelableArrayListExtra("cartList", list);
                startActivity(intent);
            }
        });
    }


}
