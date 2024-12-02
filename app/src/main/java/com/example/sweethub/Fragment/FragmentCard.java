package com.example.sweethub.Fragment;

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
import com.example.sweethub.R;

import java.util.ArrayList;

public class FragmentCard extends Fragment implements AdapterCard.CartUpdateListener {

    RecyclerView recyclerViewCart;
    TextView totall;
    Button checkOut;
    ArrayList<CartItem> cartItems = new ArrayList<>();
    AdapterCard adapterCard;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewCart = view.findViewById(R.id.recyclerViewCart);
        totall = view.findViewById(R.id.cartTotalText);
        checkOut = view.findViewById(R.id.checkOutButton);
        Log.d("zzzzz", "onViewCreated: " + cartItems.size());
        adapterCard = new AdapterCard(getContext(), cartItems, this);
        recyclerViewCart.setAdapter(adapterCard);

    }

    @Override
    public void onCartUpdated() {
        int total = 0;
        for (CartItem item : cartItems) {
            total += Integer.parseInt(item.getProduct().getPrice()) * item.getQuantity();
        }
        totall.setText("Total: " + total + " đ");
    }
}
