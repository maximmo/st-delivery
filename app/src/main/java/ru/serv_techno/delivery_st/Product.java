package ru.serv_techno.delivery_st;

import com.orm.SugarRecord;

/**
 * Created by Maxim on 18.08.2016.
 */
public class Product extends SugarRecord{
    int extid;
    int active;
    String name;
    String description;
    float price;
    String weight;
    int catalog1;
    int catalog2;
    int mainview;
    String imagelink;

    public Product() {
    }

        public Product(int extid, int active, String name, String description, float price, String weight, int catalog1, int catalog2, int mainview, String imagelink) {
        this.extid = extid;
        this.active = active;
        this.name = name;
        this.description = description;
        this.price = price;
        this.weight = weight;
        this.catalog1 = catalog1;
        this.catalog2 = catalog2;
        this.mainview = mainview;
        this.imagelink = imagelink;
    }
}
