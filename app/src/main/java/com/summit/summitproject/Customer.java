package com.summit.summitproject;

import java.util.ArrayList;

public class Customer {

    private String firstName;
    private String lastName;
    private double totalBalance;
    private ArrayList<Store> allStores;

    public Customer(String f, String l, double t, ArrayList<Store> a){
        firstName = f;
        lastName = l;
        totalBalance = t;
        allStores = a;
    }
}
