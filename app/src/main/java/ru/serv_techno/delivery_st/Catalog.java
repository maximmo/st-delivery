package ru.serv_techno.delivery_st;

import com.orm.SugarRecord;

/**
 * Created by Maxim on 18.08.2016.
 */
public class Catalog extends SugarRecord{

    int id;
    int active;
    String name;
    int parent_id;
    int countProducts;

    public Catalog() {
    }

    public Catalog(int id, int active, String name, int parent_id, int countProducts) {
        this.id = id;
        this.active = active;
        this.name = name;
        this.parent_id = parent_id;
        this.countProducts = countProducts;
    }

}
