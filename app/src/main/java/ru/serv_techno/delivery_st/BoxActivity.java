package ru.serv_techno.delivery_st;

import android.app.DialogFragment;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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
import java.util.concurrent.TimeUnit;

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

    public void showMyNotification(String textMessage){

        int NOTIFY_ID = 101;

        Intent notificationIntent = new Intent(this, products_activity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);
        // оставим только самое необходимое
        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.icon_snoopy)
                .setContentTitle("Snoopy`s Sandwitch Club")
                .setContentText(textMessage); // Текст уведомления

        Notification notification = builder.build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFY_ID, notification);
    }

    public void CreateOrder() {

        int extid=0;
        float summ=OrderProducts.getBoxSumm();
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

        LinkedHashMap mp = newOrder.MakeRequestBodyOrder();

        if(mp!=null) {
            SendOrder so = new SendOrder();
            so.execute(mp);
        }

    }

    public void setDefaultStatus() throws InterruptedException {

        onBackPressed();
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

        public Map<String, Object> hmap = new HashMap<String, Object>();

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

                String StringRes = res.toString();

                boolean BoolRes = Boolean.parseBoolean(StringRes);

                if(BoolRes == true){

                    List<OrderProducts> orderProducts = OrderProducts.getOrderProductsNew();
                    for(OrderProducts op : orderProducts){
                        op.setExtid(Integer.valueOf(extid.toString()));
                    }

                    //showMySnackbar(message.toString(), true);
                    showMyNotification(message.toString());
                    try {
                        setDefaultStatus();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    TextMessage = "Ошибка: " + message.toString();
                    showMySnackbar(TextMessage, false);
                }
            }
        }

        @Override
        protected Map doInBackground(LinkedHashMap... params) {

            String extid = "0";
            Call<ResponseBody> call = intface.SendOrder(params[0]);
            ResponseBody responseBody;
            ResponseBody errorBody;

            try {
                retrofit2.Response<ResponseBody> response = call.execute();

                //if (response.isSuccessful()) {

                    if(response.code()==200||response.code()==201){

                        responseBody = response.body();

                        hmap.put("message", "Заказ принят! Наш менеджер свяжется с Вами! =)");
                        hmap.put("result", true);
                        hmap.put("extid", extid);

                        String MyMessage = responseBody.string();

                        Map<String, String> map = gson.fromJson(MyMessage, Map.class);

                        for (Map.Entry e : map.entrySet()) {
                            if(e.getKey().equals("id")){
                                extid = e.getValue().toString();
                                float fextid = Float.parseFloat(extid);
                                int IntExtId = (int)fextid;
                                extid = String.valueOf(IntExtId);

                                hmap.put("extid", extid);
                                hmap.put("message", "Ваш заказ принят! Номер заказа: " + extid);
                            }
                        }

                    }
                    else {
                        errorBody = response.errorBody();

                        hmap.put("result", false);
                        hmap.put("message", "Ошибка: ");
                        hmap.put("extid", extid);

                        String MyMessage = errorBody.string();

                        Map<String, String> map = gson.fromJson(MyMessage, Map.class);

                        for (Map.Entry e : map.entrySet()) {
                            if(e.getKey().equals("message")){
                                hmap.put("message", e.getValue().toString());
                            }
                        }
                    }

                    return hmap;
                //}


            } catch (IOException e) {
                e.printStackTrace();

                hmap.put("result", false);
                hmap.put("message", e.getMessage());
                hmap.put("extid", "0");

                return hmap;
            }

            //return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            TextMessage = "Отправка заказа прервана пользователем!";
            showMySnackbar(TextMessage, false);
        }
    }
}
