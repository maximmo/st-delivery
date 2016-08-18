package ru.serv_techno.delivery_st;

/**
 * Created by Maxim on 18.08.2016.
 */
public class Catalog {

    int id;
    int active;
    String name;
    int parent_id;
    int countProducts;

    Catalog(int _id, int _active, String _name, int _parent_id, int _countProducts){
        id = _id;
        active = _active;
        name = _name;
        parent_id = _parent_id;
        countProducts = _countProducts;
    }

}
