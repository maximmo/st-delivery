package ru.serv_techno.delivery_st;

import com.orm.SugarRecord;

/**
 * Created by Maxim on 18.08.2016.
 */
public class OrderProducts extends SugarRecord{
    int id;
    int product_id;
    float amount;
    int order_id;

    public OrderProducts() {
    }

    public OrderProducts(int id, int product_id, float amount, int order_id) {
        this.id = id;
        this.product_id = product_id;
        this.amount = amount;
        this.order_id = order_id;
    }
}
