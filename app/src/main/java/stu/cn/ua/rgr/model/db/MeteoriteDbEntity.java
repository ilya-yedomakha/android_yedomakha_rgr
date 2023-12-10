package stu.cn.ua.rgr.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

import stu.cn.ua.rgr.model.Geolocation;
import stu.cn.ua.rgr.model.network.MeteoriteNetworkEntity;


@Entity(tableName = "meteorites")
@TypeConverters(DateTypeConverter.class)
public class MeteoriteDbEntity {

    @PrimaryKey
    private long id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "fall")
    private String fall;

    @ColumnInfo(name = "year")
    private Date year;

    @Embedded
    private Geolocation geolocation;

    public MeteoriteDbEntity() {
    }

    public MeteoriteDbEntity(MeteoriteNetworkEntity meteoriteNetworkEntity) {
        this.fall = meteoriteNetworkEntity.getFall();
        this.year = meteoriteNetworkEntity.getYear();
        this.id = meteoriteNetworkEntity.getId();
        this.name = meteoriteNetworkEntity.getName();
        this.geolocation = meteoriteNetworkEntity.getGeolocation();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFall() {
        return fall;
    }

    public Date getYear() {
        return year;
    }

    public void setFall(String fall) {
        this.fall = fall;
    }

    public void setYear(Date year) {
        this.year = year;
    }

    public Geolocation getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(Geolocation geolocation) {
        this.geolocation = geolocation;
    }
}
