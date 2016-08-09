package com.thejavatar.pokemongogo;

import com.pokegoapi.api.pokemon.Pokemon;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by theJavatar.com on 31/07/16.
 */
public class Evolution {

    private final Pokemon pokemon;
    private final List<PokemonDecorator> pokemonsToEvolve;
    private final int pokemonAmount;

    public Evolution(Pokemon pokemon, int pokemonAmount) {
        this.pokemon = pokemon;
        this.pokemonAmount = pokemonAmount;
        this.pokemonsToEvolve = new ArrayList<>();
    }

    @SuppressWarnings("unused")
    public Integer getNumberOfCandies() {
        return pokemon.getCandy();
    }

    public Integer getNumberOfPossibleEvolutions() {
        return Math.min(calculateEvolutionsBasedOnCandies(), pokemonAmount);
    }

    @SuppressWarnings("unused")
    public Integer getNumberOfEvolutions() {
        return pokemonsToEvolve.size();
    }

    private Integer calculateEvolutionsBasedOnCandies() {
        int candies = pokemon.getCandy();
        int candiesToEvolve = pokemon.getCandiesToEvolve();
        if (candiesToEvolve != 0) {
            return Double.valueOf(Math.floor(candies / candiesToEvolve)).intValue();
        } else {
            return 0;
        }
    }

    public void addPokemon(Pokemon pokemon) {
        this.pokemonsToEvolve.add(new PokemonDecorator(pokemon));
    }

    public Integer getNumberOfRemainingEvolutions() {
        return calculateEvolutionsBasedOnCandies() - this.pokemonsToEvolve.size();
    }

    public String getPokemon() {
        return pokemon.getPokemonId().name();
    }

    @SuppressWarnings("unused")
    public List<PokemonDecorator> getPokemonsToEvolve() {
        return pokemonsToEvolve;
    }
}
