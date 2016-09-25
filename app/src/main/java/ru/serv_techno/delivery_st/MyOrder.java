package ru.serv_techno.delivery_st;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
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
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
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

        Map<String, String> params = new HashMap<String, String>();

        params.put("price", String.valueOf(OrderProducts.getBoxSumm()));
        params.put("number_person", "1");
        params.put("payment_type", "cash");
        params.put("delivery", "yes");
        params.put("client[name]", this.clientname);
        params.put("client[phone]", this.clientphone);
        params.put("client[address]", this.clientaddress);

        List<OrderProducts> orderProducts = OrderProducts.getOrderProductsNew();
        for (int i = 0; i < orderProducts.size(); i++) {
            OrderProducts p = orderProducts.get(i);

            Product product = Product.getProductById(p.productid);
            if (product != null) {

                params.put("products["+ String.valueOf(i)+"][product_id]", String.valueOf(product.extid));
                params.put("products["+ String.valueOf(i)+"][amount]", String.valueOf(p.amount));
            }
        }

        Call<Object> call = intface.SendOrder(params);
        //RequestBody

        try {
            retrofit2.Response<Object> resp = null;
            resp = call.execute();

            if(resp.isSuccessful()) {
                Map<String, String> map = gson.fromJson(resp.body().toString(), Map.class);
                for(Map.Entry e : map.entrySet()){
                    System.out.println(e.getKey()+ " " + e.getValue());
                }

                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;

    }


}
