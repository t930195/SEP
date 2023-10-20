package com.mark.pocketmanager.Category;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class CategoryRepository {
    private final CategoryDao categoryDao;
    public CategoryRepository(Context context) {
        CategoryDatabase categoryDatabase = CategoryDatabase.getDatabase(context.getApplicationContext());
        categoryDao = categoryDatabase.getCategoryDao();
    }

    public LiveData<List<Category>> getCategoriesLive(String type) {
        return categoryDao.getCategoriesLive(type);
    }

    public List<String> getCategoriesList(String type) {
        return categoryDao.getCategoriesList(type);
    }

    public List<String> getCategory(String type, String category) {
        return categoryDao.getCategory(type, category);
    }


    void insertCategories(Category... categories) {
        new InsertAsyncTask(categoryDao).execute(categories);
    }

    void updateCategories(Category... categories) {
        new UpdateAsyncTask(categoryDao).execute(categories);
    }

    void deleteCategories(Category... categories) {
        new DeleteAsyncTask(categoryDao).execute(categories);
    }

    static class InsertAsyncTask extends AsyncTask<Category,Void,Void> {
        private final CategoryDao categoryDao;

        public InsertAsyncTask(CategoryDao categoryDao) {
            this.categoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            categoryDao.insertCategories(categories);
            return null;
        }
    }

    static class UpdateAsyncTask extends AsyncTask<Category,Void,Void> {
        private final CategoryDao categoryDao;

        public UpdateAsyncTask(CategoryDao categoryDao) {
            this.categoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            categoryDao.updateCategories(categories);
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<Category,Void,Void> {
        private final CategoryDao categoryDao;

        public DeleteAsyncTask(CategoryDao categoryDao) {
            this.categoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            categoryDao.deleteCategories(categories);
            return null;
        }
    }
}
