package com.example.coffeemanager.data;

public class Drink {
    private int id;
    private String name;
    private double price;
    private String description;
    private String imageUri;
    private String category;
    private boolean available;

    public Drink(int id, String name, double price, String description, String imageUri, String category, boolean available) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUri = imageUri;
        this.category = category;
        this.available = available;
    }

    public Drink(String name, String category, double price, String description, boolean available, String imageUri) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
        this.available = available;
        this.imageUri = imageUri;
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

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
