package stu.cn.ua.rgr.model.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MeteoriteDao {

    @Query("SELECT * FROM meteorites ORDER BY name COLLATE NOCASE")
    List<MeteoriteDbEntity> getMeteorites();

    @Query("SELECT * FROM meteorites WHERE id = :id")
    MeteoriteDbEntity getById(long id);

    @Insert
    void insertMeteorites(List<MeteoriteDbEntity> meteorites);

    @Query("DELETE FROM meteorites")
    void deleteMeteorites();

    default void updateMeteorites(List<MeteoriteDbEntity> entities) {
        deleteMeteorites();
        insertMeteorites(entities);
    }
}
