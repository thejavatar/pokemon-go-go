package com.thejavatar.pokemongogo;

/**
 * Created by theJavatar.com on 31/07/16.
 */
public class Evolutions {

    private final String pokemon;
    private final Integer numberOfEvolutions;
    private final Integer numberOfCandies;

    public Evolutions(String pokemon, Integer numberOfEvolutions, Integer numberOfCandies) {
        this.pokemon = pokemon;
        this.numberOfEvolutions = numberOfEvolutions;
        this.numberOfCandies = numberOfCandies;
    }

    public Integer getNumberOfCandies() {
        return numberOfCandies;
    }

    public Integer getNumberOfEvolutions() {
        return numberOfEvolutions;
    }

    public String getPokemon() {
        return pokemon;
    }
}
