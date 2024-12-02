package com.example.sweethub.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Product implements Serializable {

    private String _id;
    private String name;
    private ArrayList<String> image;
    private String describe;
    private String price;
    private String status;
    private String quantity;
    private Boolean isFavorite;

    @SerializedName("_id_category")
    private Category category;
    private Product product;
    private String createdAt,updateAt;

    public Product(String _id, String name, ArrayList<String> image, String describe, String price, String status,String quantity, Boolean isFavorite, Product product, String createdAt, String updateAt) {
        this._id = _id;
        this.name = name;
        this.image = image;
        this.describe = describe;
        this.price = price;
        this.status = status;
        this.isFavorite = isFavorite;
        this.product = product;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
        this.quantity = quantity;
    }

    public Product() {
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getImage() {
        return image;
    }



    public void setImage(ArrayList<String> image) {
        this.image = image;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
