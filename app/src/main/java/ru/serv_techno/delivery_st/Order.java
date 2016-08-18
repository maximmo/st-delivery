package ru.serv_techno.delivery_st;

/**
 * Created by Maxim on 18.08.2016.
 */
public class Order {

    int id;
    float price;
    int created_at;
    int number_person;
    int delivery;
    String orderClientName;
    String orderClientPhone;
    String orderClientAddress;

    Order(int _id, float _price, int _created_at, int _number_person, int _delivery, String _orderClientName, String _orderClientPhone, String _orderClientAddress){
        id = _id;
        price = _price;
        created_at = _created_at;
        number_person = _number_person;
        delivery = _delivery;
        orderClientName = _orderClientName;
        orderClientPhone = _orderClientPhone;
        orderClientAddress = _orderClientAddress;
    }
}
