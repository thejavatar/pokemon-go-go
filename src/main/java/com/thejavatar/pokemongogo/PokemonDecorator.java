package com.thejavatar.pokemongogo;

import POGOProtos.Enums.PokemonFamilyIdOuterClass.PokemonFamilyId;
import POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId;

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
    
    public String getPokeball() {
        if (ItemId.ITEM_MASTER_BALL == pokemon.getPokeball())
        	return "Master Ball";
        else if (ItemId.ITEM_ULTRA_BALL == pokemon.getPokeball())
        	return "Ultra Ball";
        else if (ItemId.ITEM_GREAT_BALL == pokemon.getPokeball())
        	return "Great Ball";
        else if (ItemId.ITEM_POKE_BALL == pokemon.getPokeball())
        	return "Poke Ball";
        else
        	return "Hatched";
    }

    public String getPokeballImage() {
    	if  ( pokemon.getIsEgg() ) {
    		return "http://cdn.bulbagarden.net/upload/d/de/GO_Egg.png";
        }
    	else
    	{
	        if (ItemId.ITEM_MASTER_BALL == pokemon.getPokeball()) {
	        	return "http://cdn.bulbagarden.net/upload//6/6d/Bag_Master_Ball_Sprite.png";  
	        }
	        else if (ItemId.ITEM_ULTRA_BALL == pokemon.getPokeball()) {
	        	return "http://cdn.bulbagarden.net/upload//0/03/Bag_Ultra_Ball_Sprite.png";
	        }
	        else if (ItemId.ITEM_GREAT_BALL == pokemon.getPokeball()) {
	        	return "http://cdn.bulbagarden.net/upload//c/ca/Bag_Great_Ball_Sprite.png";
	        }
	        else if (ItemId.ITEM_POKE_BALL == pokemon.getPokeball()) {
	        	return "http://cdn.bulbagarden.net/upload//9/93/Bag_Pok%C3%A9_Ball_Sprite.png";
	        }
	        else {
	        	return "http://cdn.bulbagarden.net/upload/d/de/GO_Egg.png";
	        }
    	}
    }
        
    public String getFavoriteIcon() {
        if (pokemon.isFavorite() == true) {
        	return "http://findicons.com/files/icons/166/shiny/128/star.png";
        }
        else
        	return "";
    }
    
    public String getDeployedFortIcon() {
        if (!pokemon.getDeployedFortId().equals(null) && !pokemon.getDeployedFortId().equals(""))
        	return "http://i.imgur.com/o6F6yNi.png";
        else
        	return "";
    }
    
    
    
    public String getInjured() {
        return Boolean.toString(pokemon.isInjured());
    }
    
    public String getInjuredIcon() {
    	if (pokemon.isInjured() == true)
    		return "https://upload.wikimedia.org/wikipedia/commons/thumb/6/61/Injury_icon_2.svg/240px-Injury_icon_2.svg.png";
    	else
    		return "";
    }
    
    public String getFainted() {
        return Boolean.toString(pokemon.isFainted());
    }
    
    public String getFaintedIcon() {
    	if (pokemon.isFainted() == true)
    		return "http://education.lamar.edu/_files/images/health-kinesiology/icon-heartchart.png";
    	else
    		return "";
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
