package com.thejavatar.pokemongogo;

import com.pokegoapi.api.pokemon.Pokemon;

/**
 * Created by theJavatar.com on 31/07/16.
 */
public class Evolution {

    private final Pokemon pokemon;
    private final int pokemonAmount;

    public Evolution(Pokemon pokemon, int pokemonAmount) {
        this.pokemon = pokemon;
        this.pokemonAmount = pokemonAmount;
    }

    public Integer getNumberOfCandies() {
        return pokemon.getCandy();
    }

    public Integer getNumberOfEvolutions() {
        return Math.min(calculateEvolutionsBasedOnCandies(), pokemonAmount);
    }

    private Integer calculateEvolutionsBasedOnCandies() {
        int candies = pokemon.getCandy();
        int candiesToEvolve = pokemon.getCandiesToEvolve();
        if(candiesToEvolve != 0) {
            return Double.valueOf(Math.floor(candies / candiesToEvolve)).intValue();
        } else {
            return 0;
        }
    }

    public String getPokemon() {
        return pokemon.getPokemonId().name();
    }
}
