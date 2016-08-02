package com.thejavatar.pokemongogo.apifacade;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.auth.GoogleAutoCredentialProvider;
import com.pokegoapi.util.SystemTimeImpl;
import okhttp3.OkHttpClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created by theJavatar.com on 30/07/16.
 */
@Component
public class CachedPokemonApiImpl implements CachedPokemonApi {

    @Override
    @Cacheable(value = "api", key="#username")
    synchronized public PokemonGo getApi(GoogleAutoCredentialProvider auth, String username) {
        try {
            OkHttpClient httpClient = new OkHttpClient.Builder().readTimeout(2, TimeUnit.MINUTES).writeTimeout(2, TimeUnit.MINUTES).build();
            return new PokemonGo(auth, httpClient, new SystemTimeImpl());
        } catch (Exception e) {
            throw new PokemonApiFacadeException("Could not create PokemonAPI object.", e);
        }
    }

}
