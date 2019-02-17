package com.gmail.chattest.oi.database.dao;

import com.gmail.chattest.constant.ConstantDb;
import com.gmail.chattest.model.entity.Resources;
import com.google.android.gms.common.data.ConcatenatedDataBuffer;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Observable;

@Dao
public interface ResourcesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Resources item);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Resources> items);

    @Delete
    void delete(Resources item);

    @Update
    void update(Resources item);

    @Query("SELECT * from " + ConstantDb.TABLE_RESOURCES + " ORDER BY id DESC")
    Observable<List<Resources>> getAlltoObservable();

    @Query("SELECT * FROM " + ConstantDb.TABLE_RESOURCES + " ORDER BY id DESC")
    List<Resources> getAlltoList();

}
