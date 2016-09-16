package ru.serv_techno.delivery_st;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.view.MenuItem;
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
        }
        else{
            if(DialogID == SEND_ORDER_DIALOG){
                Toast mToast = Toast.makeText(this, "Создание заказа!", Toast.LENGTH_SHORT);
                mToast.show();
            }
        }
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
