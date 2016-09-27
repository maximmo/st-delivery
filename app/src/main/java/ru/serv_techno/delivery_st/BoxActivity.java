package ru.serv_techno.delivery_st;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Maxim on 27.08.2016.
 */
public class BoxActivity extends AppCompatActivity implements View.OnClickListener{

    private final int CLEAR_DIALOG = 1;
    private final int SEND_ORDER_DIALOG = 2;
    int DialogID = 0;

    BoxAdapter boxAdapter;
    Button btnSend;
    Button btnClearBox;

    EditText PersonName;
    EditText PersonPhone;
    EditText PersonAddress;

    String TextMessage = "";

    MyOrder newOrder;

    private Gson gson = new GsonBuilder().create();
    private Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl("http://admin.serv-techno.ru")
            .build();
    private APIv1 intface = retrofit.create(APIv1.class);

    ProgressDialog OrderProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box);

        List<OrderProducts> BoxViewList = OrderProducts.getOrderProductsNew();
        boxAdapter = new BoxAdapter(this, BoxViewList);
        ListView lvBoxOrderProducts = (ListView) findViewById(R.id.lwBox);
        lvBoxOrderProducts.setAdapter(boxAdapter);
        setTitle("Корзина");

        btnSend = (Button) findViewById(R.id.btn_make_order);
        btnSend.setOnClickListener(this);

        btnClearBox = (Button) findViewById(R.id.btn_clear_box);
        btnClearBox.setOnClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        PersonName = (EditText) findViewById(R.id.PersonName);
        PersonPhone = (EditText) findViewById(R.id.PersonPhone);
        PersonAddress = (EditText) findViewById(R.id.PersonAddress);

        RefreshBoxSumm();
    }

    //Обработка нажатия позитивной кнопки
    public void doPositiveClick() {
        if(DialogID == CLEAR_DIALOG){

            int i;
            List<OrderProducts> orderProducts = OrderProducts.getOrderProductsNew();
            for (i=0;i<orderProducts.size();i++){
                OrderProducts p = orderProducts.get(i);
                p.delete();
            }

            List<OrderProducts> BoxViewList = OrderProducts.getOrderProductsNew();
            boxAdapter = new BoxAdapter(this, BoxViewList);
            ListView lvBoxOrderProducts = (ListView) findViewById(R.id.lwBox);
            lvBoxOrderProducts.setAdapter(boxAdapter);

            Snackbar mSnackbar = Snackbar.make(btnClearBox, "Корзина очищена!", Snackbar.LENGTH_SHORT);
            View snackbarView = mSnackbar.getView();
            snackbarView.setBackgroundResource(R.color.SnackbarBgRed);
            mSnackbar.show();

            RefreshBoxSumm();
        }
        else{
            if(DialogID == SEND_ORDER_DIALOG){

                CreateOrder();
//                boolean res = CreateOrder();
//                if(!res){
//                    Snackbar mSnackbar = Snackbar.make(btnClearBox, TextMessage, Snackbar.LENGTH_SHORT);
//                    View snackbarView = mSnackbar.getView();
//                    snackbarView.setBackgroundResource(R.color.SnackbarBgRed);
//                    mSnackbar.show();
//
//                    RefreshBoxSumm();
//                }
//                else{
//                    Snackbar mSnackbar = Snackbar.make(btnSend, "Заказ отправлен! Менеджер свяжется с Вами для уточнения деталей =)", Snackbar.LENGTH_LONG);
//                    View snackbarView = mSnackbar.getView();
//                    snackbarView.setBackgroundResource(R.color.SnackbarBg);
//                    mSnackbar.show();
//
//                    setDefaultStatus();
//                }
            }
        }
    }

    public void showMySnackbar(String textMessage, boolean positive){

        Snackbar mSnackbar = Snackbar.make(btnClearBox, textMessage, Snackbar.LENGTH_SHORT);
        View snackbarView = mSnackbar.getView();
        snackbarView.setBackgroundResource(R.color.SnackbarBg);
        if (!positive) {
            snackbarView.setBackgroundResource(R.color.SnackbarBgRed);
        }
        mSnackbar.show();
    }

    public void CreateOrder() {

        int extid=0;
        float summ=OrderProducts.getBoxSumm();

        Date date = new Date();
        //long createdat = date.getTime();
        long createdat = System.currentTimeMillis()/1000;

        int numberperson=1;
        int delivery=1;

        String personName = PersonName.getText().toString();
        if(personName.equals("")){
            TextMessage = "Пожалуйста, укажите имя и повторите попытку!";
            showMySnackbar(TextMessage, false);
            return;
        }

        String personPhone = PersonPhone.getText().toString();
        if(personPhone.equals("")){
            TextMessage = "Пожалуйста, укажите телефон и повторите попытку!";
            showMySnackbar(TextMessage, false);
            return;
        }

        String personAddress = PersonAddress.getText().toString();
        if(personAddress.equals("")){
            TextMessage = "Пожалуйста, укажите адрес доставки и повторите попытку!";
            showMySnackbar(TextMessage, false);
            return;
        }

        List<OrderProducts> orderProducts = OrderProducts.getOrderProductsNew();
        if(orderProducts.size()==0){
            TextMessage = "Корзина пуста!";
            showMySnackbar(TextMessage, false);
            return;
        }

        MyOrder newOrder = new MyOrder(extid, summ, createdat, numberperson, delivery, personName, personPhone, personAddress);
        try{
            newOrder.save();
            for (OrderProducts oProducts : orderProducts){
                oProducts.setOrderid(newOrder.getId());
            }
        }catch (Exception e){
            e.printStackTrace();
            TextMessage = "Ошибка при сохранении заказа: " + e.toString();
            showMySnackbar(TextMessage, false);
            return;
        }


        //boolean res = newOrder.sendOrder();
        LinkedHashMap mp = newOrder.MakeRequestBodyOrder();

        if(mp!=null) {
            SendOrder so = new SendOrder();
            so.execute(mp);
        }
        //if (res) {
//            TextMessage = "Заказ принят! Наш менеджер свяжется с Вами! =)";
//            return;
//        }
//
//        TextMessage = "Не удалось отправить заказ! Повторите попытку позже";
//        return false;
    }

    public void setDefaultStatus(){

        //onBackPressed();
    }

    public void RefreshBoxSumm(){
        Float BoxSumm = OrderProducts.getBoxSumm();
        getSupportActionBar().setTitle("Сумма заказа: " + String.valueOf(BoxSumm) + " \u20BD");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClick(View v) {
        // по id определеяем кнопку, вызвавшую этот обработчик
        switch (v.getId()) {
            case R.id.btn_make_order:
                DialogID = SEND_ORDER_DIALOG;

                DialogFragment newFragmentOrder = MyAlert.newInstance("Подтверждение заказа", "Создать заказ?", SEND_ORDER_DIALOG);
                newFragmentOrder.show(getFragmentManager(), "dialog");
                break;
            case R.id.btn_clear_box:
                DialogID = CLEAR_DIALOG;

                DialogFragment newFragmentClear = MyAlert.newInstance("Отмена заказа", "Очистить корзину?", CLEAR_DIALOG);
                newFragmentClear.show(getFragmentManager(), "dialog");
                break;
        }
    }

    class SendOrder extends AsyncTask<LinkedHashMap, Void, Map>{

        Map<String, Object> hmap = new HashMap<String, Object>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            hmap.put("result", false);
            hmap.put("message", "Ошибка при отправке заказа!");
            hmap.put("extid", "0");

            OrderProgressDialog = new ProgressDialog(BoxActivity.this);
            OrderProgressDialog.setTitle("Отправка заказа");
            OrderProgressDialog.setMessage("Выполняется оформление заказа, пожалуйста подождите!");
            OrderProgressDialog.setIndeterminate(true);
            OrderProgressDialog.show();
        }

        @Override
        protected void onPostExecute(Map map) {
            super.onPostExecute(map);

            OrderProgressDialog.setIndeterminate(false);
            OrderProgressDialog.dismiss();

            if(map!=null){
                Object res = map.get("result");
                Object message = map.get("message");
                Object extid = map.get("extid");

                Boolean r = Boolean.getBoolean(res.toString());
                if(r = true){

                    List<OrderProducts> orderProducts = OrderProducts.getOrderProductsNew();
                    for(OrderProducts op : orderProducts){
                        op.setExtid(Integer.valueOf(extid.toString()));
                    }

                    showMySnackbar(message.toString(), true);
                }
                else {
                    TextMessage = "Отправка заказа завершилась с ошибкой: " + message.toString();
                    showMySnackbar(TextMessage, false);
                }
            }
        }

        @Override
        protected Map doInBackground(LinkedHashMap... params) {

            Call<ResponseBody> call = intface.SendOrder(params[0]);

            try {
                retrofit2.Response<ResponseBody> response = call.execute();

                if (response.isSuccessful()) {
                    //response.body().toString()
                    //ResponseBody responseBody = response.body();

                    //Map<String, String> map = gson.fromJson(response.body().toString(), Map.class);

//                    for (Map.Entry e : map.entrySet()) {
//                        System.out.println(e.getKey() + " " + e.getValue());
//                        Log.e("Delivery message:", e.getKey() + " " + e.getValue());
//                    }
                    hmap.put("result", true);
                    hmap.put("message", "Заказ принят! Наш менеджер свяжется с Вами! =)");
                    hmap.put("extid", "1");

                    return hmap;
                }


            } catch (IOException e) {
                e.printStackTrace();

                hmap.put("result", false);
                hmap.put("message", "Ошибка при отправке заказа!");
                hmap.put("extid", "0");

                return hmap;
            }

            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            TextMessage = "Отправка заказа прервана пользователем!";
            showMySnackbar(TextMessage, false);
        }
    }
}
