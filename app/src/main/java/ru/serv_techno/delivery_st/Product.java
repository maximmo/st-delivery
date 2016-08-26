package ru.serv_techno.delivery_st;

import com.orm.SugarRecord;

import java.util.List;
import java.util.StringTokenizer;

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

    public static List<Product> getProductsMainView() {
        return Product.find(Product.class, "mainview = ?", "1");
    }

    public static List<Product> getProductsCatalog1(String catalog1) {
        return Product.find(Product.class, "catalog1 = ?", catalog1);
    }

    public static List<Product> getProductsCatalog2(String catalog2) {
        return Product.find(Product.class, "catalog1 = ?", catalog2);
    }

    public static Product getProductById(int productid) {
        return Product.findById(Product.class, productid);
    }
}
