package com.example.myapplication;

public class UserInformation {

    public String lat;
    public String lon;
    public String size;
    public String id;

    public UserInformation()
    {
        this.lat="0.0";
        this.size = "45";
        this.lon="0.0";
    }


    public UserInformation(String lat, String lon, String size,String id) {
        this.lat = lat;
        this.lon = lon;
        this.size = size;
        this.id = id;
    }

    public String getLat(){
        return lat;
    }

    public String getLon(){
        return lon;
    }

    public String getsize(){
        return size;
    }

    public String getId(){return id;}

    public void setLat(String lat)
    {
        this.lat=lat;
    }
    public void setLong(String lon)
    {
        this.lon=lon;
    }
    public void setSize(String size) {this.size=size;}
    public void setId(String id ){this.id=id;}


}
