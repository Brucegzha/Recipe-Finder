package com.bruce.recipefinder.entity;

import java.util.Date;

public class Fridge {
    private int id;
    private String item;
    private double amount;
    private String unit;

    public Fridge() {
    }

    public Fridge(int id, String item, double amount, String unit, Date useBy) {
        this.id = id;
        this.item = item;
        this.amount = amount;
        this.unit = unit;
        this.useBy = useBy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Date getUseBy() {
        return useBy;
    }

    public void setUseBy(Date useBy) {
        this.useBy = useBy;
    }

    private Date useBy;
}