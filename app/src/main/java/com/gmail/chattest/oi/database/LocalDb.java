package com.gmail.chattest.oi.database;

import android.content.Context;

import com.gmail.chattest.constant.ConstantDb;
import com.gmail.chattest.model.entity.Resources;
import com.gmail.chattest.oi.database.dao.ResourcesDao;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(
    entities = {
        Resources.class
    }
    , version = 1
    , exportSchema = false
)
public abstract class LocalDb extends RoomDatabase{
    public static volatile LocalDb INSTANCE;

    public abstract ResourcesDao resourcesDao();

    public static LocalDb getInstance(Context context){
        if ( INSTANCE == null ) {
            synchronized (LocalDb.class){
                if ( INSTANCE == null ) {
                    INSTANCE = Room
                        .databaseBuilder(context.getApplicationContext() , LocalDb.class , ConstantDb.DATABASE_NAME)
                        .build();
                }
            }
        }
        return INSTANCE;
    }
}
