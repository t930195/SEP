package com.mark.pocketmanager.Account;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mark.pocketmanager.CustomClass.CategoryAmount;
import com.mark.pocketmanager.CustomClass.DayAmount;

import java.util.List;

public class AccountViewModel extends AndroidViewModel {
    private final AccountRepository accountRepository;

    public AccountViewModel(@NonNull Application application) {
        super(application);
        accountRepository = new AccountRepository(application);
    }

    public LiveData<List<Account>> getAllAccountsLive() {
        return accountRepository.getAllAccountsLive();
    }

    public LiveData<List<Account>> getAccountsLive(int year, int month) {
        return accountRepository.getAccountsLive(year, month);
    }

    public LiveData<List<CategoryAmount>> getCategoryAmountsLive(int year, int month, String type) {
        return accountRepository.getCategoryAmountsLive(year, month, type);
    }

    public LiveData<List<DayAmount>> getDayAmountsLive(int year, int month) {
        return accountRepository.getDayAmountsLive(year, month);
    }

    public long getDayAmount(int year, int month, int day, String type) {
        return accountRepository.getDayAmount(year, month, day, type);
    }

    public long getMonthAmount(int year, int month, String type) {
        return accountRepository.getMonthAmount(year, month, type);
    }

    public void afterCategoryEdit(String type, String old_category, String new_category) {
        accountRepository.afterCategoryEdit(type, old_category, new_category);
    }

    public List<Account> getAccountCategory(String type, String category) {
        return accountRepository.getAccountCategory(type, category);
    }

    public void insertAccounts(Account... accounts) {
        accountRepository.insertAccounts(accounts);
    }

    public void updateAccounts(Account... accounts) {
        accountRepository.updateAccounts(accounts);
    }

    public void deleteAccounts(Account... accounts) {
        accountRepository.deleteAccounts(accounts);
    }

    public void deleteAllAccounts() {
        accountRepository.deleteAllAccounts();
    }

}
