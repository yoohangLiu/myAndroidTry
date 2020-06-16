package com.example.myaethelist.NetworkModel;

import java.util.ArrayList;


public class GetCategoryResult {
    String result;
    ArrayList<Category> data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ArrayList<Category> getData() {
        return data;
    }

    public void setData(ArrayList<Category> data) {
        this.data = data;
    }
}
