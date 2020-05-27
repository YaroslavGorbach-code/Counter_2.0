package com.yaroslavgorbach.counter;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Counter.class, CounterHistory.class },  version = 21)
public abstract class CounterDatabase extends RoomDatabase {

    private static CounterDatabase sInstance;

    public abstract CounterDao counterDao();
    public abstract CounterHistoryDao CounterHistoryDao();


    public static synchronized CounterDatabase getInstance(Context context){

        if (sInstance == null){

            sInstance = Room.databaseBuilder(context.getApplicationContext(),CounterDatabase.class, "counter.db")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return sInstance;

    }
}
