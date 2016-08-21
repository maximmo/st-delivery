package ru.serv_techno.delivery_st;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class products_activity extends AppCompatActivity implements View.OnClickListener {

    public static String LOG_TAG = "deliveryST_log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_activity);

        List<Product> MainViewList = Product.getProductsMainView();
        MainViewAdapter mainViewAdapter = new MainViewAdapter(this, MainViewList);
        ListView lvMainProducts = (ListView) findViewById(R.id.lwMainView);
        lvMainProducts.setAdapter(mainViewAdapter);

        List<Catalog> CatalogList = Catalog.getCatalogsMain();
        MainCatalogsAdapter mainCatalogsAdapter = new MainCatalogsAdapter(this, CatalogList);
        //DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ListView mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(mainCatalogsAdapter);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.main_menu_item_order:
                Toast toast = Toast.makeText(this, "Создан заказ!", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            case R.id.main_menu_item_about:
                Toast toastAbout = Toast.makeText(this, "Это приложение-клиент для сайта доставки компании 'Сервисные технологии'", Toast.LENGTH_SHORT);
                toastAbout.show();
                return true;
            case R.id.main_menu_item_exit:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
