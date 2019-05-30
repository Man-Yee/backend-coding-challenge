package com.suggestionapp.utilities;

// quick and dirty implementation of Pair - could not import javafx in heroku (buildpack)

public class Pair<City,Double> {

    private final City city;
    private final Double score;

    public Pair(City city, Double score) {
        this.city = city;
        this.score = score;
    }

    public City getKey() { return city; }
    public Double getValue() { return score; }

    @Override
    public int hashCode() { return city.hashCode() ^ score.hashCode(); }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair)) return false;
        Pair pairo = (Pair) o;
        if (this.city != null ? !this.city.equals(pairo.getKey()) : pairo.getKey() != null) return false;
        if (this.score != null ? !this.score.equals(pairo.getValue()) : pairo.getValue() != null) return false;
        return true;
    }

}