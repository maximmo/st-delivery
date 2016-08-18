package ru.serv_techno.delivery_st;

/**
 * Created by Maxim on 18.08.2016.
 */
public class Product {
    int id;
    int active;
    String name;
    String description;
    float price;
    int weight;
    int catalog_1;
    int catalog_2;
    int main_view;
    String imageLink;

    Product(int _id, int _active, String _name, String _description, float _price, int _weight, int _catalog_1, int _catalog_2, int _main_view, String _imageLink){
        id = _id;
        active = _active;
        name = _name;
        description = _description;
        price = _price;
        weight = _weight;
        catalog_1 = _catalog_1;
        catalog_2 = _catalog_2;
        main_view = _main_view;
        imageLink = _imageLink;
    }
}
