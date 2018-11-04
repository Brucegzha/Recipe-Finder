package com.bruce.recipefinder.entity;

import java.util.ArrayList;

public class Recipe {
    private int id;
    private String name;
    private ArrayList<Ingredient> ingredients;

    public Recipe() {
    }

    public Recipe(int id, String name, ArrayList<Ingredient> ingredients) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
