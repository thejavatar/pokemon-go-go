package com.thejavatar.pokemongogo;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.auth.GoogleAutoCredentialProvider;
import com.pokegoapi.util.SystemTimeImpl;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * Created by theJavatar.com on 24/07/16.
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PokemonApiFacade {

    private GoogleAutoCredentialProvider auth;

    public boolean isReady() {
        return auth != null;
    }

    public void setAuthentication(GoogleAutoCredentialProvider auth) {
        this.auth = auth;
    }

    public List<PokemonDecorator> getPokemons() {
        try {
            OkHttpClient httpClient = new OkHttpClient.Builder().readTimeout(2, TimeUnit.MINUTES).writeTimeout(2, TimeUnit.MINUTES).build();
            PokemonGo api = new PokemonGo(auth, httpClient, new SystemTimeImpl());
            return api.getInventories().getPokebank().getPokemons().stream()
                    .map(pokemon -> new PokemonDecorator(pokemon))
                    .sorted((pokemon1, pokemon2) -> {
                        if (pokemon1.getName().equals(pokemon2.getName())) {
                            return pokemon2.getPerfectIv().compareTo(pokemon1.getPerfectIv());
                        } else {
                            return pokemon1.getName().compareTo(pokemon2.getName());
                        }
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new PokemonApiFacadeException(e.getMessage(), e);
        }
    }

}
