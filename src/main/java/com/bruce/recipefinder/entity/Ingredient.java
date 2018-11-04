package com.bruce.recipefinder.entity;

public class Ingredient {
    private String item;
    private double amount;
    private String unit;

    public Ingredient() {
    }

    public Ingredient(String item, double amount, String unit) {
        this.item = item;
        this.amount = amount;
        this.unit = unit;
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
}
