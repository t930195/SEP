package com.mark.pocketmanager.Category;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Category {
    @PrimaryKey(autoGenerate = true)
    private int Id;
    private String Type;
    private String Category;

    public Category(int id) {
        Id = id;
    }

    public Category(int id, String type, String category) {
        Id = id;
        Type = type;
        Category = category;
    }

    public Category(String type, String category) {
        Type = type;
        Category = category;
    }

    public Category() {}

    public int getId() { return Id; }
    public String getType() { return Type; }
    public String getCategory() { return Category; }

    public void setId(int id) { Id = id; }
    public void setType(String type) { Type = type; }
    public void setCategory(String category) { Category = category; }
}
