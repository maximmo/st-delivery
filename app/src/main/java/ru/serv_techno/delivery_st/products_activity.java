package ru.serv_techno.delivery_st;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class products_activity extends AppCompatActivity implements View.OnClickListener {

    public static String LOG_TAG = "deliveryST_log";
    EditText catalogNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products_activity);

        Button btnreq = (Button) findViewById(R.id.testrequest);
        btnreq.setOnClickListener((View.OnClickListener) this);

        Button btnreq2 = (Button) findViewById(R.id.testrequest2);
        btnreq2.setOnClickListener((View.OnClickListener) this);

        catalogNumber = (EditText) findViewById(R.id.catalogNumber);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.testrequest:

                try {

                    int catalogNumberValue = Integer.valueOf(catalogNumber.getText().toString());

                    Catalog pr = Catalog.findById(Catalog.class, catalogNumberValue);

                    Toast toast = Toast.makeText(getApplicationContext(), "Имя группы: " + pr.name, Toast.LENGTH_SHORT);
                    toast.show();
                    Log.d(LOG_TAG, "Имя группы: " + pr.name);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast toast = Toast.makeText(getApplicationContext(), "Не удалось найти группу!", Toast.LENGTH_SHORT);
                    toast.show();
                }

                break;
            case R.id.testrequest2:

                try {

                    int catalogNumberValue = Integer.valueOf(catalogNumber.getText().toString());

                    Product pr = Product.findById(Product.class, catalogNumberValue);

                    Toast toast = Toast.makeText(getApplicationContext(), "Имя товара: " + pr.name, Toast.LENGTH_SHORT);
                    toast.show();
                    Log.d(LOG_TAG, "Имя товара: " + pr.name);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast toast = Toast.makeText(getApplicationContext(), "Не удалось найти товар!", Toast.LENGTH_SHORT);
                    toast.show();
                }

                break;
        }

    }

}
