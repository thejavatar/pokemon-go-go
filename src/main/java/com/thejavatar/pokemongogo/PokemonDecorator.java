package com.thejavatar.pokemongogo;

import com.pokegoapi.api.pokemon.Pokemon;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Optional;


/**
 * Created by theJavatar.com on 24/07/16.
 */
public class PokemonDecorator {

    private final Pokemon pokemon;

    public PokemonDecorator(Pokemon pokemon) {
        this.pokemon = pokemon;
    }

    public Integer getCp() {
        return Integer.valueOf(pokemon.getCp());
    }

    public String getCombatAttributes() {
        return pokemon.getIndividualAttack() + "/" + pokemon.getIndividualDefense() + "/" + pokemon.getIndividualStamina();
    }

    public Integer getCombatAttributesDiffFromMax() {
        return 45 - (pokemon.getIndividualAttack() + pokemon.getIndividualDefense() + pokemon.getIndividualStamina());
    }

    public Integer getCandies() {
        return Integer.valueOf(pokemon.getCandy());
    }

    public Double getPerfectIv() {
        double perfectIv = (pokemon.getIndividualAttack() + pokemon.getIndividualDefense() + pokemon.getIndividualStamina()) * 100 / 45;
        return perfectIv;
    }

    public Calendar getDateCaught(){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(pokemon.getCreationTimeMs());
        return cal;
    }

    public String getName() {
        return pokemon.getPokemonId().name();
    }

    public String getNickname() {
        String nickname = pokemon.getNickname();
        if(nickname == null || nickname.equals("")) {
            nickname = getName();
        }
        return nickname;
    }
    
    public int getBattlesAttacked() {
        return pokemon.getBattlesAttacked();
    }
    
    public int getBattlesDefended() {
        return pokemon.getBattlesDefended();
    }
    
    public String getDeployedFortId() {
        return pokemon.getDeployedFortId();
    }
    
    public String getDeployedStatus() {
        if (pokemon.getDeployedFortId().equals(null) )
    		return "false";
    	else return "true";
    }
    
    public int getCandiesToEvolve() {
        return pokemon.getCandiesToEvolve();
    }
    
    /**
     * ASHORT
     * Calculate the possible evolutions for this pokemon.
     *   Based on the candies collected and needed.
     * @return
     */
    public int getEvolutionsPossible() {
    	if (pokemon.getCandy() > 0 && pokemon.getCandiesToEvolve() > 0)
        	return pokemon.getCandy() / pokemon.getCandiesToEvolve();
        else
        	return 0;
    }
    
    public String getInjured() {
        return Boolean.toString(pokemon.isInjured());
    }
    
    public String getFainted() {
        return Boolean.toString(pokemon.isFainted());
    }
    
    public String getFavorite() {
        return Boolean.toString(pokemon.isFavorite());
    }
        
    public String getIsEgg() {
        return Boolean.toString(pokemon.getIsEgg());
    }
    
    public int getOrigin() {
        return pokemon.getOrigin();
    }

    public Integer getPokedexId() {
        return pokemon.getPokemonId().getNumber();
    }

    public String getPokedexLink() {
        Optional<PokemonExceptions> exception = findException();
        if (exception.isPresent()) {
            return exception.get().url;
        } else {
            return getName().toLowerCase();
        }
    }

    private Optional<PokemonExceptions> findException() {
        return Arrays.stream(PokemonExceptions.values())
                .filter(exception -> getName().equalsIgnoreCase(exception.apiName))
                .findFirst();
    }

    private enum PokemonExceptions {

        NIDORAN_FEMALE("NIDORAN_FEMALE", "NIDORAN (F)", "nidoran-f"),
        NIDORAN_MALE("NIDORAN_MALE", "NIDORAN (M)", "nidoran-m"),
        MR_MIME("MR_MIME", "MR MIME", "mr-mime");

        final String apiName;
        final String name;
        final String url;

        PokemonExceptions(String apiName, String name, String url) {
            this.apiName = apiName;
            this.name = name;
            this.url = url;
        }

    }
}
