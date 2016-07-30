package com.thejavatar.pokemongogo.apifacade;

import com.pokegoapi.auth.GoogleAutoCredentialProvider;
import com.thejavatar.pokemongogo.PokemonDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.Comparator;
import java.util.List;
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

}
