package ru.serv_techno.delivery_st;

import com.orm.SugarRecord;

/**
 * Created by Maxim on 18.08.2016.
 */
public class Order extends SugarRecord{

    int id;
    float price;
    int created_at;
    int number_person;
    int delivery;
    String orderClientName;
    String orderClientPhone;
    String orderClientAddress;

    public Order() {
    }

    public Order(int id, float price, int created_at, int number_person, int delivery, String orderClientName, String orderClientPhone, String orderClientAddress) {
        this.id = id;
        this.price = price;
        this.created_at = created_at;
        this.number_person = number_person;
        this.delivery = delivery;
        this.orderClientName = orderClientName;
        this.orderClientPhone = orderClientPhone;
        this.orderClientAddress = orderClientAddress;
    }
}
