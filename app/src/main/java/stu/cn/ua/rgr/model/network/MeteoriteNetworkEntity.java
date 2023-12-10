package stu.cn.ua.rgr.model.network;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

import stu.cn.ua.rgr.model.Geolocation;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MeteoriteNetworkEntity {
    private long id;
    private String name;
    private String fall;
    private Date year;
    private Geolocation geolocation;

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
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
}
