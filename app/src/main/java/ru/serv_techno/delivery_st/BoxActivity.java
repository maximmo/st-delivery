package ru.serv_techno.delivery_st;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.view.MenuItem;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by Maxim on 27.08.2016.
 */
public class BoxActivity extends AppCompatActivity implements View.OnClickListener{

    BoxAdapter boxAdapter;
    Button btnSend;
    Button btnClearBox;
    private final int CLEAR_DIALOG = 1;
    private final int SEND_ORDER_DIALOG = 2;
    int DialogID = 0;
    EditText PersonName;
    EditText PersonPhone;
    EditText PersonAddress;
    String TextMessage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box);

        List<OrderProducts> BoxViewList = OrderProducts.getOrderProductsNew();
        boxAdapter = new BoxAdapter(this, BoxViewList);
        ListView lvBoxOrderProducts = (ListView) findViewById(R.id.lwBox);
        lvBoxOrderProducts.setAdapter(boxAdapter);
        setTitle("Корзина");

//        lvBoxOrderProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                RefreshBoxSumm();
//            }
//        });

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

                boolean res = CreateOrder();
                if(!res){
                    Snackbar mSnackbar = Snackbar.make(btnClearBox, TextMessage, Snackbar.LENGTH_SHORT);
                    View snackbarView = mSnackbar.getView();
                    snackbarView.setBackgroundResource(R.color.SnackbarBgRed);
                    mSnackbar.show();

                    RefreshBoxSumm();
                }
                else{
                    Snackbar mSnackbar = Snackbar.make(btnSend, "Заказ отправлен! Менеджер свяжется с Вами для уточнения деталей =)", Snackbar.LENGTH_LONG);
                    View snackbarView = mSnackbar.getView();
                    snackbarView.setBackgroundResource(R.color.SnackbarBg);
                    mSnackbar.show();

                    setDefaultStatus();
                }
            }
        }
    }

    public boolean CreateOrder() {

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
            return false;
        }

        String personPhone = PersonPhone.getText().toString();
        if(personPhone.equals("")){
            TextMessage = "Пожалуйста, укажите телефон и повторите попытку!";
            return false;
        }

        String personAddress = PersonAddress.getText().toString();
        if(personAddress.equals("")){
            TextMessage = "Пожалуйста, укажите адрес доставки и повторите попытку!";
            return false;
        }

        List<OrderProducts> orderProducts = OrderProducts.getOrderProductsNew();
        if(orderProducts.size()==0){
            TextMessage = "Корзина пуста!";
            return false;
        }

        MyOrder newOrder = new MyOrder(extid, summ, createdat, numberperson, delivery, personName, personPhone, personAddress);
        try{
            newOrder.save();
            for (int i=0;i<orderProducts.size();i++){
                OrderProducts p = orderProducts.get(i);
                p.setOrderid(newOrder.getId());
            }
        }catch (Exception e){
            e.printStackTrace();
            TextMessage = "Ошибка при сохранении заказа: " + e.toString();
            return false;
        }


        boolean res = newOrder.sendOrder();

        if (res) {
            return true;
        }

        TextMessage = "Не удалось отправить заказ! Повторите попытку позже";
        return false;
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
}
