package ru.serv_techno.delivery_st;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Maxim on 18.08.2016.
 */
public class OrderProducts extends SugarRecord{
    int extid;
    int productid;
    int amount;
    int orderid;

    public OrderProducts() {
    }

    public OrderProducts(int extid, int productid, int amount, int orderid) {
        this.extid = extid;
        this.productid = productid;
        this.amount = amount;
        this.orderid = orderid;
    }

    public static List<OrderProducts> getOrderProductsNew() {
        return OrderProducts.find(OrderProducts.class, "extid = ?", "0");
    }
}
