package stu.cn.ua.rgr.model;

import java.util.Date;

import stu.cn.ua.rgr.model.db.MeteoriteDbEntity;

public class Meteorite {

    private final long id;
    private final String name;
    private final String fall;
    private final Date year;
    private final Geolocation geolocation;

    public Meteorite(long id, String name, String fall, Date year, Geolocation geolocation) {
        this.id = id;
        this.name = name;
        this.fall = fall;
        this.year = year;
        this.geolocation = geolocation;
    }

    public Meteorite(MeteoriteDbEntity entity) {
        this(
                entity.getId(),
                entity.getName(),
                entity.getFall(),
                entity.getYear(),
                entity.getGeolocation());
    }


    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFall() {
        return fall;
    }

    public Date getYear() {
        return year;
    }

    public Geolocation getGeolocation() {
        return geolocation;
    }
}
