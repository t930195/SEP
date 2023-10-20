package com.mark.pocketmanager.Category;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert
    void insertCategories(Category... categories);

    @Update
    void updateCategories(Category... categories);

    @Delete
    void deleteCategories(Category... categories);

    @Query("SELECT * FROM Category where Type = :type")
    LiveData<List<Category>> getCategoriesLive(String type);

    @Query("SELECT Category FROM Category where Type = :type")
    List<String> getCategoriesList(String type);

    @Query("SELECT Category FROM Category where Type = :type and Category = :category")
    List<String> getCategory(String type, String category);
}
