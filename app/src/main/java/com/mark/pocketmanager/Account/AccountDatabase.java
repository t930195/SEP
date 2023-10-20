package com.mark.pocketmanager.Account;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

//singleton讓一個類只生成一個實例
@Database(entities = {Account.class}, version = 1, exportSchema = false)
public abstract class AccountDatabase extends RoomDatabase {
    private static AccountDatabase INSTANCE;

    //synchronized保證多個線程同時使用時不會衝突
    static synchronized AccountDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AccountDatabase.class, "account_database")
                    .allowMainThreadQueries().build();
        }//沒有創建過就創建一個新的，有創建過就直接回傳
        return INSTANCE;
    }

    public abstract AccountDao getAccountDao();
}
