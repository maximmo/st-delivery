package ru.serv_techno.delivery_st;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "deliveryST_log";
    private Bitmap splashBG;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        new ParseCatalogs().execute();
        new ParseProducts().execute();

        BitmapFactory.Options op0 = new BitmapFactory.Options();
        op0.inJustDecodeBounds = true;
        this.splashBG = BitmapFactory.decodeResource(getResources(), R.drawable.background_splash, op0);
        Display disp = getWindowManager().getDefaultDisplay();
        int scale = Math.max(Math.round((float) (op0.outWidth / disp.getWidth())), Math.round((float) (op0.outHeight / disp.getHeight())));
        if (scale < 1) {
            scale = 1;
        }
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inPreferredConfig = Bitmap.Config.RGB_565;
        op.inSampleSize = scale;
        this.splashBG = BitmapFactory.decodeResource(getResources(), R.drawable.background_splash, op);
        this.imageView = (ImageView) findViewById(R.id.backgroundsplash);
        this.imageView.setImageBitmap(this.splashBG);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this, products_activity.class);
                startActivity(i);
                finish();
            }
        }, 5 * 1000);
    }

    private class ParseCatalogs extends AsyncTask<String, String, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(getString(R.string.catalog_request));

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);

            try {
                List<Catalog> catalogs;

                JSONArray CatalogsJSON = new JSONArray(strJson);

                for (int i = 0; i < CatalogsJSON.length(); i++) {
                    JSONObject CatalogJSON = CatalogsJSON.getJSONObject(i);

                    int extId;
                    int active;
                    int parentId = 0;
                    int countProducts = 0;

                    extId = CatalogJSON.getInt("id");
                    active = CatalogJSON.getInt("active");
                    String name = CatalogJSON.getString("name");

                    try {
                        parentId = CatalogJSON.getInt("parent_id");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        countProducts = Integer.parseInt(CatalogJSON.getString("countProducts"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    catalogs = Catalog.find(Catalog.class, "extid = ?", String.valueOf(extId));

                    if (catalogs.isEmpty()) {
                        Catalog catalog = new Catalog(extId, active, name, parentId, countProducts);
                        try {
                            catalog.save();
                            Log.d(LOG_TAG, "В БД записана группа: " + name);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d(LOG_TAG, "Ошибка при записи группы: " + name + e.getMessage());
                        }
                    } else {
                        Catalog catalog = catalogs.get(0);
                        catalog.extid = extId;
                        catalog.active = active;
                        catalog.name = name;
                        catalog.parentid = parentId;
                        catalog.countproducts = countProducts;
                        try {
                            catalog.save();
                            Log.d(LOG_TAG, "В БД записана группа: " + name);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d(LOG_TAG, "Ошибка при записи группы: " + name + e.getMessage());
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class ParseProducts extends AsyncTask<String, String, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(getString(R.string.product_request));

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);

            try {
                List<Product> products;

                JSONArray ProductsJSON = new JSONArray(strJson);

                for (int i = 0; i < ProductsJSON.length(); i++) {
                    JSONObject ProductJSON = ProductsJSON.getJSONObject(i);

                    int extid = 0;
                    int active = 0;
                    String name = "";
                    String description = "";
                    float price = 0;
                    String weight = "";
                    int catalog1 = 0;
                    int catalog2 = 0;
                    int mainview = 0;
                    String imagelink = "";
                    String bigImageLink = "";

                    extid = ProductJSON.getInt("id");
                    active = ProductJSON.getInt("active");
                    name = ProductJSON.getString("name");
                    description = ProductJSON.getString("description");
                    price = Float.parseFloat(ProductJSON.getString("price"));

                    try {
                        weight = ProductJSON.getString("weight");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        catalog1 = ProductJSON.getInt("catalog_1");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        catalog2 = ProductJSON.getInt("catalog_2");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        mainview = Integer.parseInt(ProductJSON.getString("main_view"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        imagelink = ProductJSON.getString("imageLink");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        bigImageLink = ProductJSON.getString("bigImageLink");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    products = Product.find(Product.class, "extid = ?", String.valueOf(extid));

                    if (products.isEmpty()) {
                        Product product = new Product(extid, active, name, description, price, weight, catalog1, catalog2, mainview, imagelink, bigImageLink);
                        try {
                            product.save();
                            Log.d(LOG_TAG, "В БД записан товар: " + name);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d(LOG_TAG, "Ошибка при записи товара: " + name + e.getMessage());
                        }
                    } else {
                        Product product = products.get(0);
                        product.extid = extid;
                        product.active = active;
                        product.name = name;
                        product.description = description;
                        product.price = price;
                        product.weight = weight;
                        product.catalog1 = catalog1;
                        product.catalog2 = catalog2;
                        product.mainview = mainview;
                        product.imagelink = imagelink;
                        product.bigImageLink = bigImageLink;

                        try {
                            product.save();
                            Log.d(LOG_TAG, "В БД записан товар: " + name);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d(LOG_TAG, "Ошибка при записи товара: " + name + e.getMessage());
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d(LOG_TAG, "Ошибка при парсинге товара: " + e.getMessage());
            }
        }
    }
}
