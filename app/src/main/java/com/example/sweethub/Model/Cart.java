package com.example.sweethub.Model;

import java.io.Serializable;

public class Cart  implements Serializable {
    private String _id,name,price,describe,image,quantity,id_category,id_product,createdAt,updatedAt;
    Product product;
    Category category;

    public Cart(String _id, String name, String price, String describe, String image, String quantity, String id_category, String id_product, String createdAt, String updatedAt, Product product, Category category) {
        this._id = _id;
        this.name = name;
        this.price = price;
        this.describe = describe;
        this.image = image;
        this.quantity = quantity;
        this.id_category = id_category;
        this.id_product = id_product;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.product = product;
        this.category = category;
    }

    public Cart() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getId_category() {
        return id_category;
    }

    public void setId_category(String id_category) {
        this.id_category = id_category;
    }

    public String getId_product() {
        return id_product;
    }

    public void setId_product(String id_product) {
        this.id_product = id_product;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
