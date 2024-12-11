package com.nhom7.sweethub.servers;


import com.nhom7.sweethub.Model.Cart;
import com.nhom7.sweethub.Model.Category;
import com.nhom7.sweethub.Model.FeedBack;
import com.nhom7.sweethub.Model.OrderCart;
import com.nhom7.sweethub.Model.OrderCartt;
import com.nhom7.sweethub.Model.Product;
import com.nhom7.sweethub.Model.Response;
import com.nhom7.sweethub.Model.User;
import com.nhom7.sweethub.Models.Order;

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

    public String IPv4 = "192.168.0.100";

    public static String BASE_URL() {
        return String.format("http://%s:3000/api/", IPv4);
    }

    @GET("getListCategory")
    Call<Response<ArrayList<Category>>> getListCategory();

    @GET("search-distributor")
    Call<Response<ArrayList<Category>>> searchDistributor(@Query("key") String key);

    @POST("add_Catagory")
    Call<Response<ArrayList<Category>>> addCategory(@Body Category category);


    @PUT("updateCategoryById/{id}")
    Call<Response<ArrayList<Category>>> updateCategory(@Path("id") String id, @Body Category category);

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

    @GET("get-list-order")
    Call<Response<ArrayList<Order>>> getListOrder();

    @GET("getListCart")
    Call<Response<ArrayList<Cart>>> getListCart();

    @POST("addCart")
    Call<Response<Cart>> addCart(@Body Cart cart);

    @PUT("increaseQuantity/{id}")
    Call<Response<ArrayList<Cart>>> increaseQuantity(@Path("id") String productId);

    // Gọi API để giảm số lượng sản phẩm
    @PUT("decreaseQuantity/{id}")
    Call<Response<ArrayList<Cart>>>  decreaseQuantity(@Path("id") String productId);

    @DELETE("deleteCart/{id}")
    Call<Response<ArrayList<Cart>>> deleteCart(@Path("id") String id);


    @GET("getListFeedBack")
    Call<Response<ArrayList<FeedBack>>> getListFeedback();

    @POST("add_feedback")
    Call<Response<FeedBack>> addFeedBack(@Body FeedBack feedBack);

    @GET("getListOrderCart")
    Call<Response<ArrayList<OrderCart>>> getListOrderCart();

    @POST("addListOrderCart")
    Call<Response<OrderCartt>> addListOrderCart(@Body ArrayList<OrderCartt> orderCartList);

    @DELETE("clearListOrderCart")
    Call<Response> clearListOrderCart();

    @GET("getListUserByUsername")
    Call<Response<ArrayList<User>>> getUserByEmail(@Query("username") String username);


}


