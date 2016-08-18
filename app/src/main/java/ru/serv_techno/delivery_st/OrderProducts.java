package ru.serv_techno.delivery_st;

/**
 * Created by Maxim on 18.08.2016.
 */
public class OrderProducts {
    int id;
    int product_id;
    float amount;
    int order_id;

    OrderProducts(int _id, int _product_id, float _amount, int _order_id) {
        id = _id;
        product_id = _product_id;
        amount = _amount;
        order_id = _order_id;
    }
}
