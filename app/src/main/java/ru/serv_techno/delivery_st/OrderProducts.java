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
    long orderid;

    public OrderProducts() {
    }

    public OrderProducts(int extid, int productid, int amount, long orderid) {
        this.extid = extid;
        this.productid = productid;
        this.amount = amount;
        this.orderid = orderid;
    }

    public static List<OrderProducts> getOrderProductsNew() {
        return OrderProducts.find(OrderProducts.class, "extid = ?", "0");
    }

    public void setExtid(int _extid){
        this.extid = _extid;
        this.save();
    }

    public void setOrderid(long _orderid){
        this.orderid = _orderid;
        this.save();
    }

    public static float getBoxSumm() {

        float summ = 0;

        List<OrderProducts> orderProductses = OrderProducts.find(OrderProducts.class, "extid = ?", "0");

        for (int i=0;i<orderProductses.size();i++){
            Product p = Product.getProductById(orderProductses.get(i).productid);

            if(p!=null) {
                summ = summ + orderProductses.get(i).amount * p.price;
            }
        }

        return summ;
    }
}
