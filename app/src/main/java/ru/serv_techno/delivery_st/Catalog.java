package ru.serv_techno.delivery_st;

import com.orm.SugarRecord;

/**
 * Created by Maxim on 18.08.2016.
 */
public class Catalog extends SugarRecord{

    int extid;
    int active;
    String name;
    int parentid;
    int countproducts;

    public Catalog() {
    }

    public Catalog(int extid, int active, String name, int parentid, int countproducts) {
        this.extid = extid;
        this.active = active;
        this.name = name;
        this.parentid = parentid;
        this.countproducts = countproducts;
    }

}
