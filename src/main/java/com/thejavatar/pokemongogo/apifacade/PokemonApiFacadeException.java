package com.thejavatar.pokemongogo.apifacade;

/**
 * Created by theJavatar.com on 24/07/16.
 */
public class PokemonApiFacadeException extends RuntimeException {
    public PokemonApiFacadeException(String message, Exception e) {
        super(message, e);
    }
}
