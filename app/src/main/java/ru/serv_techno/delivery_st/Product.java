package ru.serv_techno.delivery_st;

import com.orm.SugarRecord;

/**
 * Created by Maxim on 18.08.2016.
 */
public class Product extends SugarRecord{
    int id;
    int active;
    String name;
    String description;
    float price;
    int weight;
    int catalog_1;
    int catalog_2;
    int main_view;
    String imageLink;

    public Product() {
    }

    public Product(int id, int active, String name, String description, float price, int weight, int catalog_1, int catalog_2, int main_view, String imageLink) {
        this.id = id;
        this.active = active;
        this.name = name;
        this.description = description;
        this.price = price;
        this.weight = weight;
        this.catalog_1 = catalog_1;
        this.catalog_2 = catalog_2;
        this.main_view = main_view;
        this.imageLink = imageLink;
    }
}
