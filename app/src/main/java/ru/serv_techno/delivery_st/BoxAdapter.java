package ru.serv_techno.delivery_st;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Maxim on 27.08.2016.
 */
public class BoxAdapter extends BaseAdapter implements View.OnClickListener {

    Context ctx;
    LayoutInflater lInflater;
    List<OrderProducts> objects;

    BoxAdapter(Context context, List<OrderProducts> orderProducts) {
        ctx = context;
        objects = orderProducts;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public OrderProducts getItem(int position) {
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
            view = lInflater.inflate(R.layout.item_box, parent, false);
        }

        final OrderProducts OrderProduct = getOrderProduct(position);
        int productid = OrderProduct.productid;

        Product p = Product.getProductById(productid);

        ((TextView) view.findViewById(R.id.ItemBoxName)).setText(p.name);
        ((TextView) view.findViewById(R.id.ItemBoxPrice)).setText(String.valueOf(p.price * OrderProduct.amount) + " \u20BD");

        //кнопки увеличения количества
        Button btnMinus = (Button) view.findViewById(R.id.btnMinus);
        ((TextView) view.findViewById(R.id.textViewAmount)).setText(String.valueOf(OrderProduct.amount));
        Button btnPlus = (Button) view.findViewById(R.id.btnPlus);
        btnMinus.setOnClickListener(this);
        btnMinus.setTag(position);
        btnPlus.setOnClickListener(this);
        btnPlus.setTag(position);

        if (p.imagelink != null) {
            //new DownloadImageTask((ImageView) view.findViewById(R.id.ItemBoxImage)).execute(p.imagelink);
            Picasso.with(ctx.getApplicationContext())
                    .load(p.bigImageLink)
                    .placeholder(R.drawable.icon_snoopy)
                    .error(R.drawable.icon_snoopy)
                    .into((ImageView) view.findViewById(R.id.ItemBoxImage));
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnMinus:
                //обработка уменьшения
                OrderProducts OrderProductPressed = getItem((Integer) v.getTag());
                if (OrderProductPressed != null) {
                    if (OrderProductPressed.amount == 1) {
                        OrderProductPressed.delete();
                        objects.remove(OrderProductPressed);
                    } else {
                        OrderProductPressed.amount--;
                        OrderProductPressed.save();
                    }
                    this.notifyDataSetChanged();
                }

                ((BoxActivity)ctx).RefreshBoxSumm();

                break;
            case R.id.btnPlus:
                //обработка увеличения
                OrderProducts OrderProductPressedPlus = getItem((Integer) v.getTag());
                if (OrderProductPressedPlus != null) {
                    OrderProductPressedPlus.amount++;
                    OrderProductPressedPlus.save();
                }
                this.notifyDataSetChanged();

                ((BoxActivity)ctx).RefreshBoxSumm();

                break;
        }
    }

    OrderProducts getOrderProduct(int position) {
        return ((OrderProducts) getItem(position));
    }

    View.OnClickListener btnItemPress = new View.OnClickListener() {

        public void onClick(View v) {

            OrderProducts OrderProductPressed = getOrderProduct((Integer) v.getTag());
            Toast toast = Toast.makeText(ctx.getApplicationContext(), "Нажата запись:" + OrderProductPressed.productid, Toast.LENGTH_SHORT);
            toast.show();
        }
    };


}
