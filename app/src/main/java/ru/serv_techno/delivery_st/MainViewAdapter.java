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
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Maxim on 22.08.2016.
 */
public class MainViewAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    List<Product> objects;

    MainViewAdapter(Context context, List<Product> products) {
        ctx = context;
        objects = products;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

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

        new DownloadImageTask((ImageView) view.findViewById(R.id.ItemMainViewImage)).execute(p.imagelink);

        Button btn = (Button) view.findViewById(R.id.ItemMainViewButton);
        btn.setTag(position);
        btn.setOnClickListener(btnItemPress);

        return view;

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    Product getProduct(int position) {
        return ((Product) getItem(position));
    }

    View.OnClickListener btnItemPress = new View.OnClickListener() {

        public void onClick(View v) {

            Product ProductPressed = getProduct((Integer) v.getTag());
            Toast toast = Toast.makeText(ctx.getApplicationContext(), "Добавлен товар: " + ProductPressed.name, Toast.LENGTH_SHORT);
            toast.show();
        }
    };

}
