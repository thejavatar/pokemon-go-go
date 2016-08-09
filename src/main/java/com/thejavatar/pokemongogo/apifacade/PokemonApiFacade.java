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
            pokemons.sort(PokemonOrderBy.BY_IV.getComparator());
            pokemons.forEach(pokemon -> {
                Evolution evolution;
                PokemonFamilyIdOuterClass.PokemonFamilyId family = pokemon.getPokemonFamily();
                if(evolutions.containsKey(family)) {
                    evolution = evolutions.get(family);
                } else {
                    evolution = createEvolution(pokemon, pokebank.getPokemonByPokemonId(pokemon.getPokemonId()).size());
                    if(evolution.getNumberOfPossibleEvolutions() > 0) {
                        evolutions.put(family, evolution);
                    }
                }
                if (evolution.getNumberOfRemainingEvolutions() > 0 && canEvolve(pokemon)) {
                    evolution.addPokemon(pokemon);
                }
            });

            return evolutions.values().stream()
                    .filter(evolution -> evolution.getNumberOfEvolutions() > 0)
                    .sorted(EvolutionOrderBy.BY_EVOLUTIONS.getComparator())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new PokemonApiFacadeException(e.getMessage(), e);
        }
    }

    private Evolution createEvolution(Pokemon pokemon, int pokemonAmount) {
        return new Evolution(pokemon, pokemonAmount);
    }

    private Boolean canEvolve(Pokemon pokemon) {
        switch (pokemon.getPokemonId().getNumber()) {
            case 3:
            case 6:
            case 9:
            case 12:
            case 15:
            case 18:
            case 20:
            case 22:
            case 24:
            case 26:
            case 28:
            case 31:
            case 34:
            case 36:
            case 38:
            case 40:
            case 42:
            case 45:
            case 47:
            case 49:
            case 51:
            case 53:
            case 55:
            case 57:
            case 59:
            case 62:
            case 65:
            case 68:
            case 71:
            case 73:
            case 76:
            case 78:
            case 80:
            case 82:
            case 83:
            case 85:
            case 87:
            case 89:
            case 91:
            case 94:
            case 95:
            case 97:
            case 99:
            case 101:
            case 103:
            case 105:
            case 107:
            case 108:
            case 110:
            case 112:
            case 113:
            case 114:
            case 115:
            case 117:
            case 119:
            case 121:
            case 122:
            case 123:
            case 124:
            case 125:
            case 126:
            case 127:
            case 128:
            case 130:
            case 131:
            case 132:
            case 134:
            case 135:
            case 136:
            case 137:
            case 139:
            case 141:
            case 142:
            case 143:
            case 144:
            case 145:
            case 146:
            case 149:
            case 150:
            case 151: return false;
            default: return true;
        }
    }

    private enum PokemonOrderBy {
        BY_IV((pokemon1, pokemon2) -> getPerfectIv(pokemon2).compareTo(getPerfectIv(pokemon1)));

        private final Comparator<Pokemon> comparator;

        PokemonOrderBy(Comparator<Pokemon> comparator) {
            this.comparator = comparator;
        }

        public Comparator<Pokemon> getComparator() {
            return comparator;
        }

        private static Double getPerfectIv(Pokemon pokemon) {
            double perfectIv = (pokemon.getIndividualAttack() + pokemon.getIndividualDefense() + pokemon.getIndividualStamina()) * 100 / 45;
            return perfectIv;
        }
    }

    private enum EvolutionOrderBy {
        BY_EVOLUTIONS((pokemon1, pokemon2) -> pokemon2.getNumberOfEvolutions().compareTo(pokemon1.getNumberOfEvolutions()));

        private final Comparator<Evolution> comparator;

        EvolutionOrderBy(Comparator<Evolution> comparator) {
            this.comparator = comparator;
        }

        public Comparator<Evolution> getComparator() {
            return comparator;
        }
    }
}
