package ru.serv_techno.delivery_st;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Maxim on 22.08.2016.
 */
public class MainViewAdapter extends BaseAdapter implements View.OnClickListener{

    Context ctx;
    LayoutInflater lInflater;
    List<Product> objects;

    MainViewAdapter(Context context, List<Product> products) {
        ctx = context;
        objects = products;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        Toast.makeText(ctx, "проверка", Toast.LENGTH_SHORT).show();
//    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item_main_view, parent, false);
        }

        Product p = getProduct(position);

        ((TextView) view.findViewById(R.id.ItemMainViewName)).setText(p.name);
        ((TextView) view.findViewById(R.id.ItemMainViewPrice)).setText(String.valueOf(p.price) + " \u20BD");

        if (p.imagelink != null) {
            //new DownloadImageTask((ImageView) view.findViewById(R.id.ItemMainViewImage)).execute(p.imagelink);
            Picasso.with(ctx.getApplicationContext())
                    .load(p.bigImageLink)
                    .placeholder(R.drawable.icon_snoopy)
                    .error(R.drawable.icon_snoopy)
                    .into((ImageView) view.findViewById(R.id.ItemMainViewImage));
        }

        Button btn = (Button) view.findViewById(R.id.ItemMainViewButton);
        btn.setTag(position);
        btn.setOnClickListener(btnItemPress);

        view.setTag(position);
        view.setOnClickListener(this);

        return view;

    }

    Product getProduct(int position) {
        return ((Product) getItem(position));
    }

    @Override
    public void onClick(View v) {

        Product mProduct = getProduct((Integer) v.getTag());

        if (mProduct != null) {
            Intent intent = new Intent(ctx, ProductCardActivity.class);
            intent.putExtra("ProductID", mProduct.getId());
            ctx.startActivity(intent);
        }

    }

    View.OnClickListener btnItemPress = new View.OnClickListener() {

        public void onClick(View v) {

            Product ProductPressed = getProduct((Integer) v.getTag());

            List<OrderProducts> orderProduct = OrderProducts.find(OrderProducts.class, "extid = ? and productid = ?", "0", String.valueOf(ProductPressed.getId()));
            if (orderProduct.size()==0){
                OrderProducts orderProducts = new OrderProducts(0, Integer.valueOf(String.valueOf(ProductPressed.getId())), 1, 0);
                orderProducts.save();
            }else{
                OrderProducts orderProducts = orderProduct.get(0);
                orderProducts.amount++;
                orderProducts.save();
            }

            Snackbar mSnackbar = Snackbar.make(v, "Добавлен товар: " + ProductPressed.name, Snackbar.LENGTH_SHORT);
            View snackbarView = mSnackbar.getView();
            snackbarView.setBackgroundResource(R.color.SnackbarBg);
            mSnackbar.show();
        }
    };

}
