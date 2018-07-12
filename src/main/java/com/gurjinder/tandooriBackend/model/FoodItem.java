package com.gurjinder.tandooriBackend.model;


import java.util.List;

public class FoodItem implements Cloneable {

    private int id;
    private String name;
    private double price;
    private String description;
    private List<Integer> categoryIds;
    private boolean availabily;



    public List<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public FoodItem() {
    }

    public FoodItem(int id, String name, double price, String description, boolean availabily) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.availabily = availabily;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAvailabily() {
        return availabily;
    }

    public void setAvailabily(boolean availabily) {
        this.availabily = availabily;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}