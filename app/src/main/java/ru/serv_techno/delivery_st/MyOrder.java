package ru.serv_techno.delivery_st;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.provider.Settings.Global.getString;
import static ru.serv_techno.delivery_st.R.string.order_create_request;

/**
 * Created by Maxim on 18.08.2016.
 */
public class MyOrder extends SugarRecord {

    int extid;
    float price;
    long createdat;
    int numberperson;
    int delivery;
    String clientname;
    String clientphone;
    String clientaddress;


    private static Gson gson = new GsonBuilder().create();
    private static Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl("http://admin.serv-techno.ru")
            .build();
    private static APIv1 intface = retrofit.create(APIv1.class);


    public MyOrder() {
    }

    public MyOrder(int extid, float price, long createdat, int numberperson, int delivery, String clientname, String clientphone, String clientaddress) {
        this.extid = extid;
        this.price = price;
        this.createdat = createdat;
        this.numberperson = numberperson;
        this.delivery = delivery;
        this.clientname = clientname;
        this.clientphone = clientphone;
        this.clientaddress = clientaddress;
    }

    public boolean sendOrder() {

        RequestBody rb;
        LinkedHashMap<String, RequestBody> mp = new LinkedHashMap<>();

        rb = RequestBody.create(MediaType.parse("text/plain"), String.valueOf((int) OrderProducts.getBoxSumm()));
        mp.put("price", rb);

        rb = RequestBody.create(MediaType.parse("text/plain"), "1");
        mp.put("number_person", rb);

        rb = RequestBody.create(MediaType.parse("text/plain"), "cash");
        mp.put("payment_type", rb);

        rb = RequestBody.create(MediaType.parse("text/plain"), "yes");
        mp.put("delivery", rb);

        rb = RequestBody.create(MediaType.parse("text/plain"), this.clientname);
        mp.put("client[name]", rb);

        rb = RequestBody.create(MediaType.parse("text/plain"), this.clientphone);
        mp.put("client[phone]", rb);

        rb = RequestBody.create(MediaType.parse("text/plain"), this.clientaddress);
        mp.put("client[address]", rb);

        List<OrderProducts> orderProducts = OrderProducts.getOrderProductsNew();
        for (int i = 0; i < orderProducts.size(); i++) {
            OrderProducts p = orderProducts.get(i);

            Product product = Product.getProductById(p.productid);
            if (product != null) {

                rb = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(product.extid));
                mp.put("products[" + String.valueOf(i) + "][product_id]", rb);

                rb = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(p.amount));
                mp.put("products[" + String.valueOf(i) + "][amount]", rb);

            }
        }

        Call<ResponseBody> call = intface.SendOrder(mp);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    Map<String, String> map = gson.fromJson(response.toString(), Map.class);

                    for (Map.Entry e : map.entrySet()) {
                        System.out.println(e.getKey() + " " + e.getValue());
                        Log.e("Delivery message:", e.getKey() + " " + e.getValue());
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
//        try {
//            retrofit2.Response<Object> response = call.execute();
//            if(response.isSuccessful()) {
//                Map<String, String> map = gson.fromJson(response.body().toString(), Map.class);
//                for(Map.Entry e : map.entrySet()){
//                    System.out.println(e.getKey()+ " " + e.getValue());
//                }
//            }
        //Response<ResponseBody> response = call.execute();
        //String response = call.execute().body().toString();

//            retrofit2.Response<ResponseBody> response = call.execute();
//
//            if(response.isSuccessful()) {
//                Map<String, String> map = gson.fromJson(response.body().toString(), Map.class);
//                for(Map.Entry e : map.entrySet()){
//                    System.out.println(e.getKey()+ " " + e.getValue());
//                }

        return true;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //return false;

    }


}
