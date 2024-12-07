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
import com.example.sweethub.Fragment.FragmentFeedBack;
import com.example.sweethub.Fragment.FragmentHome;
import com.example.sweethub.Fragment.FragmentTest;
import com.example.sweethub.Fragment.FragmentUser;
import com.example.sweethub.Model.Product;
import com.example.sweethub.Model.Response;
import com.example.sweethub.Model.User;
import com.example.sweethub.servers.HttpRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {


    private static final Logger log = LoggerFactory.getLogger(MainActivity.class);
    BottomNavigationView bottomNavigationView;
    FragmentCard fragmentCard = new FragmentCard();
    FragmentHome fragmentHome;
    FragmentFavourite fragmentFavourite = new FragmentFavourite();
    FragmentUser fragmentUser = new FragmentUser();
    FragmentTest fragmentTest = new FragmentTest();
    FragmentFeedBack fragmentFeedBack = new FragmentFeedBack();
    HttpRequest httpRequest;
    ArrayList<User> list;

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

        fragmentHome = new FragmentHome();

        httpRequest = new HttpRequest();
        list = new ArrayList<>();

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
        Log.d("zzziiiiiiii", "onCreate: " + email + "Role: " + role);

        Bundle bundle = new Bundle();


        httpRequest.callAPI().getUserByEmail(email).enqueue(new Callback<Response<ArrayList<User>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<User>>> call, retrofit2.Response<Response<ArrayList<User>>> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus() == 200){
                        list.addAll(response.body().getData());
                        for(User user: list){
                            bundle.putString("address",user.getAddress());
                            bundle.putString("id",user.get_id());
                            bundle.putString("username",user.getUsername());
                            bundle.putString("password",user.getPassword());
                            bundle.putString("email",user.getEmail());
                            bundle.putString("name",user.getName());
                            bundle.putString("avartar",user.getAvartar());
                            bundle.putInt("role",role);
                            Log.d("999", "onResponse: " + user.getAddress() + user.getUsername());
                        }

                        fragmentHome.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frag_container002, fragmentHome)
                                .commit();
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<User>>> call, Throwable t) {

            }

        });







        bottomNavigationView = findViewById(R.id.bottom_nav01);
        if(role == 0 || role == 3){
            bottomNavigationView.getMenu().findItem(R.id.nav_feedback).setVisible(false);;
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frag_container002, fragmentHome)
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
                    fr.setArguments(bundle);
                }
                else if(item.getItemId() == R.id.nav_feedback){
                    fr = fragmentFeedBack;
                    fr.setArguments(bundle);
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