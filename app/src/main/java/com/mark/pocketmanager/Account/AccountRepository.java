package com.mark.pocketmanager.Account;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.mark.pocketmanager.CustomClass.CategoryAmount;
import com.mark.pocketmanager.CustomClass.DayAmount;

import java.util.List;

public class AccountRepository {
    private final LiveData<List<Account>> allAccountsLive;
    private final AccountDao accountDao;
    public AccountRepository(Context context) {
        AccountDatabase accountDatabase = AccountDatabase.getDatabase(context.getApplicationContext());
        accountDao = accountDatabase.getAccountDao();
        allAccountsLive = accountDao.getAllAccountsLive();
    }

    public LiveData<List<Account>> getAllAccountsLive() {
        return allAccountsLive;
    }

    public LiveData<List<Account>> getAccountsLive(int year, int month) {
        return accountDao.getAccountsLive(year, month);
    }

    public LiveData<List<CategoryAmount>> getCategoryAmountsLive(int year, int month, String type) {
        return accountDao.getCategoryAmountsLive(year, month, type);
    }

    public LiveData<List<DayAmount>> getDayAmountsLive(int year, int month) {
        return accountDao.getDayAmountsLive(year, month);
    }

    public long getDayAmount(int year, int month, int day, String type) {
        return accountDao.getDayAmount(year, month, day, type);
    }

    public long getMonthAmount(int year, int month, String type) {
        return accountDao.getMonthAmount(year, month, type);
    }

    public List<Account> getAccountCategory(String type, String category) {
        return accountDao.getAccountCategory(type, category);
    }

    void insertAccounts(Account... accounts) {
        new InsertAsyncTask(accountDao).execute(accounts);
    }
    void updateAccounts(Account... accounts) {
        new UpdateAsyncTask(accountDao).execute(accounts);
    }
    void deleteAccounts(Account... accounts) {
        new DeleteAsyncTask(accountDao).execute(accounts);
    }
    void afterCategoryEdit(String type, String old_category, String new_category) {
        accountDao.afterCategoryEdit(type, old_category, new_category);
    }
    void deleteAllAccounts() {
        new DeleteAllAsyncTask(accountDao).execute();
    }

//    static class afterCategoryEditAsyncTask extends AsyncTask<String,Void,Void> {
//        private final AccountDao accountDao;
//
//        public InsertAsyncTask(AccountDao accountDao) {
//            this.accountDao = accountDao;
//        }
//
//        @Override
//        protected Void doInBackground(Account... accounts) {
//            accountDao.insertAccounts(accounts);
//            return null;
//        }
//    }

    static class InsertAsyncTask extends AsyncTask<Account,Void,Void> {
        private final AccountDao accountDao;

        public InsertAsyncTask(AccountDao accountDao) {
            this.accountDao = accountDao;
        }

        @Override
        protected Void doInBackground(Account... accounts) {
            accountDao.insertAccounts(accounts);
            return null;
        }
    }

    static class UpdateAsyncTask extends AsyncTask<Account,Void,Void> {
        private final AccountDao accountDao;

        public UpdateAsyncTask(AccountDao accountDao) {
            this.accountDao = accountDao;
        }

        @Override
        protected Void doInBackground(Account... accounts) {
            accountDao.updateAccounts(accounts);
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<Account,Void,Void> {
        private final AccountDao accountDao;

        public DeleteAsyncTask(AccountDao accountDao) {
            this.accountDao = accountDao;
        }

        @Override
        protected Void doInBackground(Account... accounts) {
            accountDao.deleteAccounts(accounts);
            return null;
        }
    }

    static class DeleteAllAsyncTask extends AsyncTask<Void,Void,Void> {
        private final AccountDao accountDao;

        public DeleteAllAsyncTask(AccountDao accountDao) {
            this.accountDao = accountDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            accountDao.deleteAllAccounts();
            return null;
        }
    }
}
