package com.example.myaethelist.NetworkModel;

import java.util.ArrayList;

public class Data {
    private  String category_name;
    private  Integer id;
    private ArrayList<Item> items;
     Data(){

     }
    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }


}
