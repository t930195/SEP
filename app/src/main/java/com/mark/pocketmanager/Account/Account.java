package com.mark.pocketmanager.Account;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;

@Entity
public class Account {
    @PrimaryKey(autoGenerate = true)
    private int Id;
    private String Asset;
    private String Type;
    private long Amount;
    private String Category;
    private String SubCategory;

    private long Time;
    private int Year;
    private int Month;
    private int Day;
    private int Hour;
    private int Minute;

    private String Note;

    public Account(String asset, String type, long amount, String category, String subCategory, Calendar time, String note) {
        Asset = asset;
        Type = type;
        Amount = amount;
        Category = category;
        SubCategory = subCategory;
        Time = time.getTimeInMillis();
        Year = time.get(Calendar.YEAR);
        Month = time.get(Calendar.MONTH);
        Day = time.get(Calendar.DAY_OF_MONTH);
        Hour = time.get(Calendar.HOUR_OF_DAY);
        Minute = time.get(Calendar.MINUTE);
        Note = note;
    }

    public Account(int id, String asset, String type, long amount, String category, String subCategory, Calendar time, String note) {
        Id = id;
        Asset = asset;
        Type = type;
        Amount = amount;
        Category = category;
        SubCategory = subCategory;
        Time = time.getTimeInMillis();
        Year = time.get(Calendar.YEAR);
        Month = time.get(Calendar.MONTH);
        Day = time.get(Calendar.DAY_OF_MONTH);
        Hour = time.get(Calendar.HOUR_OF_DAY);
        Minute = time.get(Calendar.MINUTE);
        Note = note;
    }

    public Account(int id) {
        Id = id;
    }

    public Account() {}

    public int getId() {
        return Id;
    }
    public String getAsset() {
        return Asset;
    }
    public String getType() {
        return Type;
    }
    public long getAmount() {
        return Amount;
    }
    public String getCategory() {
        return Category;
    }
    public String getSubCategory() {
        return SubCategory;
    }
    public String getNote() {
        return Note;
    }
    public long getTime() { return Time; }
    public int getYear() { return Year; }
    public int getMonth() { return Month; }
    public int getDay() { return Day; }
    public int getHour() { return Hour; }
    public int getMinute() { return Minute; }

    public void setAsset(String asset) {
        Asset = asset;
    }
    public void setId(int id) {
        this.Id = id;
    }
    public void setType(String type) {
        Type = type;
    }
    public void setAmount(long amount) {
        Amount = amount;
    }
    public void setCategory(String category) {
        Category = category;
    }
    public void setSubCategory(String subCategory) {
        SubCategory = subCategory;
    }
    public void setNote(String note) {
        Note = note;
    }
    public void setTime(long time) { Time = time; }
    public void setYear(int year) { Year = year; }
    public void setMonth(int month) { Month = month; }
    public void setDay(int day) { Day = day; }
    public void setHour(int hour) { Hour = hour; }
    public void setMinute(int minute) { Minute = minute; }
}
