package com.thejavatar.pokemongogo.apifacade;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.auth.GoogleAutoCredentialProvider;

/**
 * Created by theJavatar.com on 30/07/16.
 */
public interface CachedPokemonApi {

    PokemonGo getApi(GoogleAutoCredentialProvider auth, String username);

}
