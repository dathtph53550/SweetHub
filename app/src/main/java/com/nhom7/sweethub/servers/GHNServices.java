package com.nhom7.sweethub.servers;


import com.nhom7.sweethub.Models.District;
import com.nhom7.sweethub.Models.DistrictRequest;
import com.nhom7.sweethub.Models.GHNOrderRespone;
import com.nhom7.sweethub.Models.Province;
import com.nhom7.sweethub.Models.ResponseGHN;
import com.nhom7.sweethub.Models.SendOrderRequest;
import com.nhom7.sweethub.Models.Ward;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GHNServices {
    public static String GHN_URL = "https://dev-online-gateway.ghn.vn/";

    @GET("/shiip/public-api/master-data/province")
    Call<ResponseGHN<ArrayList<Province>>> getListProvince();

    @POST("/shiip/public-api/master-data/district")
    Call<ResponseGHN<ArrayList<District>>> getListDistrict(@Body DistrictRequest districtRequest);

    @GET("/shiip/public-api/master-data/ward")
    Call<ResponseGHN<ArrayList<Ward>>> getListWard(@Query("district_id") int district_id);


    @POST("shiip/public-api/v2/shipping-order/create")
    Call<ResponseGHN<GHNOrderRespone>> GHNOrder(@Body SendOrderRequest sendOrderRequest);

    @POST("shiip/public-api/v2/shipping-order/detail")
    Call<ResponseGHN<GHNOrderRespone>> detailOderCode(@Body Map<String, String> body);



}
