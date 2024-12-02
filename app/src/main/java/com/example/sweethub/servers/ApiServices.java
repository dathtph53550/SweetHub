package com.example.sweethub.servers;


import com.example.sweethub.Model.Category;
import com.example.sweethub.Model.Product;
import com.example.sweethub.Model.Response;
import com.example.sweethub.Model.User;
import com.example.sweethub.Models.Order;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiServices {
    public static String BASE_URL = "http://192.168.0.100:3000/api/";

    @GET("getListCategory")
    Call<Response<ArrayList<Category>>> getListCategory();

    @GET("search-distributor")
    Call<Response<ArrayList<Category>>> searchDistributor(@Query("key") String key);

    @POST("add_distributor")
    Call<Response<ArrayList<Category>>> addDistributor(@Body Category category);


    @PUT("updateDistributorById/{id}")
    Call<Response<ArrayList<Category>>> updateDistributor(@Path("id") String id, @Body Category category);

    @DELETE("destroyDistributorById/{id}")
    Call<Response<ArrayList<Category>>> deleteDistributor(@Path("id") String id);

    @GET("get_list_product")
    Call<Response<ArrayList<Product>>> getListFruit(@Header("Authorization") String token);

    @Multipart
    @POST("ProductWithImage")
    Call<Response<Product>> addProductWithFileImage(@PartMap Map<String, RequestBody> requestBodyMap,
                                                @Part ArrayList<MultipartBody.Part> ds_hinh
    );

    @Multipart
    @PUT("update_ProductById/{id}")
    Call<Response<Product>> updateProductWithFileImage(@PartMap Map<String, RequestBody> requestBodyMap,
                                                   @Path("id") String id,
                                                   @Part ArrayList<MultipartBody.Part> ds_hinh
    );

    @GET("search-Product")
    Call<Response<ArrayList<Product>>> searchProduct(@Query("key") String key);


    @PUT("toggle-favourite/{id}")
    Call<Response<ArrayList<Product>>> changeFavourite(@Path("id") String productId);

    @GET("getListProductByCategory")
    Call<Response<ArrayList<Product>>> getProductsByCategory(@Query("id_category") String idCategory);

    @DELETE("destroy_ProductById/{id}")
    Call<Response<Product>> deleteProduct(@Path("id") String id);

    @POST("add-order")
    Call<Response<ArrayList<Category>>> order(@Body Order order);


    @Multipart
    @POST("register-send-email")
    Call<Response<User>> register(
            @Part("username") RequestBody username,
            @Part("password") RequestBody password,
            @Part("email") RequestBody email,
            @Part("name") RequestBody name,
            @Part MultipartBody.Part avartar
    );

    @POST("login")
    Call<Response<User>> login (@Body User user);

    @GET("getListProductByFavourite")
    Call<Response<ArrayList<Product>>> getListProductByFavourite();

}


