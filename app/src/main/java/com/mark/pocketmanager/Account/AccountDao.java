package com.mark.pocketmanager.Account;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mark.pocketmanager.CustomClass.CategoryAmount;
import com.mark.pocketmanager.CustomClass.DayAmount;

import java.util.List;

@Dao
public interface AccountDao {
    @Insert
    void insertAccounts(Account... accounts);

    @Update
    void updateAccounts(Account... accounts);

    @Delete
    void deleteAccounts(Account... accounts);

    @Query("DELETE FROM Account")
    void deleteAllAccounts();

    @Query("UPDATE Account SET Category = :new_category WHERE Type = :type and Category = :old_category")
    void afterCategoryEdit(String type, String old_category, String new_category);

    @Query("SELECT * FROM Account ORDER BY Id DESC")
    LiveData<List<Account>> getAllAccountsLive();

    @Query("SELECT * FROM Account WHERE Year = :year AND Month = :month ORDER BY Time DESC")
    LiveData<List<Account>> getAccountsLive(int year, int month);

    @Query("SELECT SUM(Amount) FROM Account WHERE Year = :year AND Month = :month AND Day = :day AND Type = :type")
    long getDayAmount(int year, int month, int day, String type);

    @Query("SELECT SUM(Amount) FROM Account WHERE Year = :year AND Month = :month AND Type = :type")
    long getMonthAmount(int year, int month, String type);

    @Query("SELECT Category, SUM(Amount) as Amount FROM Account WHERE Year = :year AND Month = :month AND Type = :type GROUP BY Category")
    LiveData<List<CategoryAmount>> getCategoryAmountsLive(int year, int month, String type);

    @Query("SELECT Day, SUM(CASE WHEN Type = '收入' then Amount else - Amount end) as Amount FROM Account WHERE Year = :year AND Month = :month GROUP BY Day")
    LiveData<List<DayAmount>> getDayAmountsLive(int year, int month);

    @Query("select * from Account where Type = :type and Category = :category")
    List<Account> getAccountCategory(String type, String category);
}
