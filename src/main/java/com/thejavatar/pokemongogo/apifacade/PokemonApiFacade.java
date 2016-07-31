package com.thejavatar.pokemongogo.apifacade;

import POGOProtos.Enums.PokemonFamilyIdOuterClass;
import com.pokegoapi.api.inventory.PokeBank;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.auth.GoogleAutoCredentialProvider;
import com.thejavatar.pokemongogo.Evolution;
import com.thejavatar.pokemongogo.PokemonDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by theJavatar.com on 24/07/16.
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PokemonApiFacade {

    @Autowired
    private CachedPokemonApi api;

    private GoogleAutoCredentialProvider auth;
    private String username;

    public boolean isReady() {
        return auth != null && username != null;
    }

    public void setAuthentication(GoogleAutoCredentialProvider auth, String username) {
        this.auth = auth;
        this.username = username;
    }

    public List<PokemonDecorator> getPokemons(Comparator<PokemonDecorator> comparator) {
        try {
            return api.getApi(auth, username).getInventories().getPokebank().getPokemons().stream()
                    .map(pokemon -> new PokemonDecorator(pokemon))
                    .sorted(comparator)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new PokemonApiFacadeException(e.getMessage(), e);
        }
    }

    public Collection<Evolution> getEvolutions() {
        try {
            Map<PokemonFamilyIdOuterClass.PokemonFamilyId, Evolution> evolutions = new HashMap<>();
            PokeBank pokebank = api.getApi(auth, username).getInventories().getPokebank();
            List<Pokemon> pokemons = pokebank.getPokemons();
            pokemons.forEach(pokemon -> {
                int totalNumberOfPokemonOfThisType = pokebank.getPokemonByPokemonId(pokemon.getPokemonId()).size();
                Evolution evolution = createEvolution(pokemon, totalNumberOfPokemonOfThisType);
                PokemonFamilyIdOuterClass.PokemonFamilyId family = pokemon.getPokemonFamily();
                if (evolution.getNumberOfEvolutions() > 0) {
                    if (evolutions.containsKey(family)) {
                        Integer prev = evolutions.get(family).getNumberOfEvolutions();
                        if (evolution.getNumberOfEvolutions() > prev) {
                            evolutions.put(family, evolution);
                        }
                    } else {
                        evolutions.put(family, evolution);
                    }
                }
            });
            return evolutions.values();
        } catch (Exception e) {
            throw new PokemonApiFacadeException(e.getMessage(), e);
        }
    }

    private Evolution createEvolution(Pokemon pokemon, int pokemonAmount) {
        return new Evolution(pokemon, pokemonAmount);
    }
}
