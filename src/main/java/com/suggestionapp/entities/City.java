package com.suggestionapp.entities;

/**
 * This class represents the City entity.  It contains the name, country, longitude and latitude information of the city.
 * It also provides getter and setter methods for each attributes.
 */
public class City implements Comparable<City>{
    private String name;
    private String country;
    private Double latitude;
    private Double longitude;

    public City(String name, String country, Double latitude, Double longitude){
        this.name = name;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getCountry(){
        return country;
    }

    public void setCountry(String country){
        this.country = country;
    }

    public Double getLatitude(){
        return latitude;
    }

    public void setLatitude(Double latitude){
        this.latitude = latitude;
    }

    public Double getLongitude(){
        return longitude;
    }

    public void setLongitude(Double longitude){
        this.longitude = longitude;
    }

    @Override
    public String toString(){
        return "search.entities.City [name=" + name + ", country=" + country + ", latitude=" + latitude + ", longitude=" + longitude + "]";
    }

    @Override
    public int compareTo(City city){
        return name.compareTo(city.name);
    }
}
