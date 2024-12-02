package com.example.sweethub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.sweethub.Fragment.FragmentCard;
import com.example.sweethub.Fragment.FragmentFavourite;
import com.example.sweethub.Fragment.FragmentHome;
import com.example.sweethub.Fragment.FragmentTest;
import com.example.sweethub.Fragment.FragmentUser;
import com.example.sweethub.Model.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;
    FragmentCard fragmentCard = new FragmentCard();
    FragmentHome fragmentHome = new FragmentHome();
    FragmentFavourite fragmentFavourite = new FragmentFavourite();
    FragmentUser fragmentUser = new FragmentUser();
    FragmentTest fragmentTest = new FragmentTest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        if (intent != null) {
            String navigateTo = intent.getStringExtra("navigateTo");
            if ("FragmentHome".equals(navigateTo)) {
                Product newProduct = (Product) intent.getSerializableExtra("newProduct"); // Nhận sản phẩm mới nếu cần
                showFragmentHome(newProduct);
            }
        }

        String email = intent.getStringExtra("userName");
        int role = intent.getIntExtra("role", 0);
        Log.d("zzz", "onCreate: " + email + "Role: " + role);

        Bundle bundle = new Bundle();
        bundle.putString("email",email);
        bottomNavigationView = findViewById(R.id.bottom_nav01);

        fragmentHome.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frag_container002, fragmentHome)
                .commit();


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fr;

                if(item.getItemId() == R.id.nav_home){
                    fr = fragmentHome;
                    fr.setArguments(bundle);
                }else if( item.getItemId() == R.id.nav_shop){
                    fr = fragmentCard;
                    fr.setArguments(bundle);

                }else if( item.getItemId() == R.id.nav_favorite){
                    fr = fragmentFavourite;
                }
                else if(item.getItemId() == R.id.nav_user){
                    fr = fragmentUser;
                }
                else{
                    fr = fragmentHome;
                }

                // cập nhật fragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frag_container002, fr).commit();
                return true;
            }
        });
    }

    private void showFragmentHome(Product newProduct) {
        FragmentHome fragmentHome = new FragmentHome();

        // Nếu cần gửi sản phẩm mới đến FragmentHome
        if (newProduct != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("newProduct", newProduct);
            fragmentHome.setArguments(bundle);
        }

        // Thay thế Fragment hiện tại bằng FragmentHome
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frag_container002, fragmentHome)
                .commit();
    }
}