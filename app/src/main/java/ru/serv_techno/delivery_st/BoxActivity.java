package ru.serv_techno.delivery_st;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
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

    Button btnSend;
    Button btnClearBox;
    private final int CLEAR_DIALOG = 0;
    private final int SEND_ORDER_DIALOG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box);

        List<OrderProducts> BoxViewList = OrderProducts.getOrderProductsNew();
        BoxAdapter boxAdapter = new BoxAdapter(this, BoxViewList);
        ListView lvBoxOrderProducts = (ListView) findViewById(R.id.lwBox);
        lvBoxOrderProducts.setAdapter(boxAdapter);
        setTitle("Корзина");

        btnSend = (Button) findViewById(R.id.btn_make_order);
        btnSend.setOnClickListener(this);

        btnClearBox = (Button) findViewById(R.id.btn_clear_box);
        btnClearBox.setOnClickListener(this);


    }

    public static class MyAlert extends DialogFragment {
        public static MyAlert newInstance(String title, String message, int DialogID) {
            MyAlert frag = new MyAlert();
            Bundle args = new Bundle();
            args.putString("title", title);
            args.putString("message", message);
            args.putInt("DialogID", DialogID);
            frag.setArguments(args);
            return frag;
        }

        //Обработка нажатия позитивной кнопки
        public void doPositiveClick(int DialogID) {

        }
        //Обработка нажатия негативной кнопки
        public void doNegativeClick(int DialogID) {

        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Bundle args = getArguments(); //Получаем заголовок и сообщение
            String title = args.getString("title");
            String message = args.getString("message");
            int DialogID = args.getInt("DialogID");

            return new AlertDialog.Builder(getActivity())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int whichButton) {
                                    //((BoxActivity) getActivity()).doPositiveClick(DialogID);
                                    //((BoxActivity) getActivity()).
                                }
                            })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int whichButton) {
                                    //((BoxActivity) getActivity()).doNegativeClick(DialogID);
                                }
                            })
                    .create();
        }
    }

    public void onClick(View v) {
        // по id определеяем кнопку, вызвавшую этот обработчик
        switch (v.getId()) {
            case R.id.btn_make_order:
                DialogFragment newFragmentOrder = MyAlert.newInstance("Подтверждение заказа", "Создать заказ?", SEND_ORDER_DIALOG);
                newFragmentOrder.show(getFragmentManager(), "dialog");
                break;
            case R.id.btn_clear_box:
                DialogFragment newFragmentClear = MyAlert.newInstance("Отмена заказа", "Очистить корзину?", CLEAR_DIALOG);
                newFragmentClear.show(getFragmentManager(), "dialog");
                break;
        }
    }
}
