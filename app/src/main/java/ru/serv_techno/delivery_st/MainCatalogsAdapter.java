package ru.serv_techno.delivery_st;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Maxim on 22.08.2016.
 */
public class MainCatalogsAdapter extends BaseAdapter{

    Context ctx;
    LayoutInflater lInflater;
    List<Catalog> objects;

    MainCatalogsAdapter(Context context, List<Catalog> catalogs) {
        ctx = context;
        objects = catalogs;
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

    public Catalog getCatalog(int position) {
        return ((Catalog) getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.catalog_item, parent, false);
        }

        Catalog catalog = getCatalog(position);

        ((TextView) view.findViewById(R.id.lwCatalogItem)).setText(catalog.name);

//        Button btn = (Button) view.findViewById(R.id.ItemMainViewButton);
//        btn.setTag(position);
//        btn.setOnClickListener(btnItemPress);

        return view;

    }
}
