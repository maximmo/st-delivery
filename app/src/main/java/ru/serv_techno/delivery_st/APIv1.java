package ru.serv_techno.delivery_st;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/**
 * Created by Maxim on 24.09.2016.
 */
public interface APIv1 {

    @Multipart
    @POST("/api/v1/order/create")
    Call<ResponseBody> SendOrder(@PartMap Map<String, RequestBody> params);

}
