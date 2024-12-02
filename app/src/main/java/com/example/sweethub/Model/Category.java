package com.example.sweethub.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Category implements Serializable {
    @SerializedName("_id")
    private String _id;
    private String name;

    public Category(String id, String name) {
        this._id = id;
        this.name = name;
    }

    public Category() {
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
}
