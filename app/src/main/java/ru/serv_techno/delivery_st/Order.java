package ru.serv_techno.delivery_st;

import com.orm.SugarRecord;

/**
 * Created by Maxim on 18.08.2016.
 */
public class Order extends SugarRecord{

    int extid;
    float price;
    int createdat;
    int numberperson;
    int delivery;
    String orderclientname;
    String orderclientphone;
    String orderclientaddress;

    public Order() {
    }

    public Order(int extid, float price, int createdat, int numberperson, int delivery, String orderclientname, String orderclientphone, String orderclientaddress) {
        this.extid = extid;
        this.price = price;
        this.createdat = createdat;
        this.numberperson = numberperson;
        this.delivery = delivery;
        this.orderclientname = orderclientname;
        this.orderclientphone = orderclientphone;
        this.orderclientaddress = orderclientaddress;
    }
}
