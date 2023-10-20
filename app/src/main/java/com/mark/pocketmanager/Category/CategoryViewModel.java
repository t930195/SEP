package com.mark.pocketmanager.Category;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {
    private final CategoryRepository categoryRepository;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        categoryRepository = new CategoryRepository(application);
    }

    public LiveData<List<Category>> getCategoriesLive(String type) {
        return categoryRepository.getCategoriesLive(type);
    }

    public List<String> getCategoriesList(String type) {
        return categoryRepository.getCategoriesList(type);
    }

    public List<String> getCategory(String type, String category) {
        return categoryRepository.getCategory(type, category);
    }

    public void insertCategories(Category... categories) {
        categoryRepository.insertCategories(categories);
    }

    public void updateCategories(Category... categories) {
        categoryRepository.updateCategories(categories);
    }

    public void deleteCategories(Category... categories) {
        categoryRepository.deleteCategories(categories);
    }

}
