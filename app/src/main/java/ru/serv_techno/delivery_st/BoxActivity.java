package ru.serv_techno.delivery_st;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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


    }

    //Обработка нажатия позитивной кнопки
    public void doPositiveClick() {
        if(DialogID == CLEAR_DIALOG){
            List<OrderProducts> orderProducts = OrderProducts.listAll(OrderProducts.class);
            OrderProducts.deleteAll(OrderProducts.class);

            List<OrderProducts> BoxViewList = OrderProducts.getOrderProductsNew();
            boxAdapter = new BoxAdapter(this, BoxViewList);
            ListView lvBoxOrderProducts = (ListView) findViewById(R.id.lwBox);
            lvBoxOrderProducts.setAdapter(boxAdapter);

            Toast mToast = Toast.makeText(this, "Корзина очищена!", Toast.LENGTH_SHORT);
            mToast.show();
        }
        else{
            if(DialogID == SEND_ORDER_DIALOG){
                Toast mToast = Toast.makeText(this, "Создание заказа!", Toast.LENGTH_SHORT);
                mToast.show();
            }
        }
    }
//    //Обработка нажатия негативной кнопки
//    public void doNegativeClick() {
//
//    }

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
