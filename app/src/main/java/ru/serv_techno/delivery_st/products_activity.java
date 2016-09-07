package ru.serv_techno.delivery_st;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

public class products_activity extends AppCompatActivity {

    public static String LOG_TAG = "deliveryST_log";
    ActionBarDrawerToggle mDrawerToggle;
    String mDrawerTitle;
    CharSequence mTitle;
    MainViewAdapter mainViewAdapter;
    MainCatalogsAdapter mainCatalogsAdapter;
    ListView mDrawerList;
    DrawerLayout mDrawerLayout;
    NavigationView NavView;
    RelativeLayout RelLayoutNavDrawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_activity);

        List<Product> MainViewList = Product.getProductsMainView();
        mainViewAdapter = new MainViewAdapter(this, MainViewList);
        ListView lvMainProducts = (ListView) findViewById(R.id.lwMainView);

        lvMainProducts.setAdapter(mainViewAdapter);

        List<Catalog> CatalogList = Catalog.getCatalogsMain();
        mainCatalogsAdapter = new MainCatalogsAdapter(this, CatalogList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        RelLayoutNavDrawer = (RelativeLayout) findViewById(R.id.relLayoutDrawer);

        // Включаем значок у ActionBar для управления выдвижной панелью щелчком
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                null,  /* значок-гамбургер для замены стрелки 'Up' */
                R.string.drawer_open,  /* добавьте строку "open drawer" - описание для  accessibility */
                R.string.drawer_close  /* добавьте "close drawer" - описание для accessibility */
        ){
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);

        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(mainCatalogsAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        getSupportActionBar().setTitle("Популярные товары");

//        FloatingActionButton fabButton = new FloatingActionButton(this);
//        fabButton.setImageResource(R.drawable.box_ico);
//        fabButton.setBackgroundColor(Color.CYAN);
//        fabButton.animate();
//        //fabButton.s
////        fabButton.withGravity(Gravity.BOTTOM | Gravity.RIGHT);
////        fabButton.withMargins(0, 0, 16, 16);
//        fabButton.show();
//        if (savedInstanceState == null) {
//            selectItem(0);
//        }

        //mDrawerLayout.openDrawer(GravityCompat.START);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

     private void selectItem(int position) {

        Catalog catalog = mainCatalogsAdapter.getCatalog(position);

        if(catalog!=null) {
            List<Product> MainViewList = Product.getProductsCatalog1(String.valueOf(catalog.extid));
            MainViewAdapter mainViewAdapter = new MainViewAdapter(this, MainViewList);
            ListView lvMainProducts = (ListView) findViewById(R.id.lwMainView);
            lvMainProducts.setAdapter(mainViewAdapter);
            mDrawerList.setItemChecked(position, true);
            getSupportActionBar().setTitle(catalog.name);
            //mDrawerLayout.closeDrawer(mDrawerList);

            mDrawerLayout.closeDrawer(RelLayoutNavDrawer);
        }
    }

    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (id) {
            case R.id.main_menu_item_order:
                Intent i = new Intent(products_activity.this, BoxActivity.class);
                startActivity(i);
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

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
