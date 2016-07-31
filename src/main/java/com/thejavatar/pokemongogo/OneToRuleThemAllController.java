package com.thejavatar.pokemongogo;

import com.pokegoapi.auth.GoogleAutoCredentialProvider;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.thejavatar.pokemongogo.apifacade.PokemonApiFacade;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.springframework.web.bind.annotation.RequestMethod.POST;


/**
 * Created by theJavatar.com on 23/07/16.
 */
@Controller
public class OneToRuleThemAllController {

    private static final Logger LOG = LoggerFactory.getLogger(OneToRuleThemAllController.class);

    @Autowired
    private PokemonApiFacade apiFacade;

    @RequestMapping("/")
    public String home(Model model) {
        LOG.debug("home()");
        if (apiFacade.isReady()) {
            return showPage(model);
        } else {
            return "pages/authentication-form";
        }
    }

    @RequestMapping(value = "/", method = POST)
    public String processAuthentication(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            Model model
    ) throws LoginFailedException, RemoteServerException, IOException, ExecutionException, InterruptedException {
        LOG.debug("processAuthentication()");
        authenticateWithPokemonGo(username, password);
        return "redirect:/";
    }

    private String showPage(Model model) {
        return "pages/index";
    }

    @RequestMapping("/getPokemons/{orderBy}")
    @ResponseBody
    public List<PokemonDecorator> getPokemons(@PathVariable OrderBy orderBy) {
        LOG.debug("getPokemons() with ordering: " + orderBy);
        return apiFacade.getPokemons(orderBy.getComparator());
    }

    @RequestMapping("/getEvolutions")
    @ResponseBody
    public List<Evolutions> getEvolutions() {
        LOG.debug("getEvolutions()");
        return apiFacade.getEvolutions();
    }

    private void authenticateWithPokemonGo(String username, String password) throws LoginFailedException, RemoteServerException {
        apiFacade.setAuthentication(new GoogleAutoCredentialProvider(new OkHttpClient(), username, password), username);
    }

    @ExceptionHandler
    public String handleLoginFailedException(LoginFailedException e, Model model) {
        return prepareViewForException(model, e, "Cannot log in. Bad credentials maybe?");
    }

    @ExceptionHandler
    public String handleGeneralException(Exception e, Model model) {
        return prepareViewForException(model, e, e.getMessage());
    }

    private String prepareViewForException(Model model, Exception e, String message) {
        LOG.error(e.getMessage(), e);
        model.addAttribute("error", message);
        return "pages/error";
    }

    private enum OrderBy {
        BY_NAME_AND_IV(
                (pokemon1, pokemon2) -> {
                    if (pokemon1.getName().equals(pokemon2.getName())) {
                        return pokemon2.getPerfectIv().compareTo(pokemon1.getPerfectIv());
                    } else {
                        return pokemon1.getName().compareTo(pokemon2.getName());
                    }
                }
        ),
        BY_DATE_CAUGHT((pokemon1, pokemon2) -> pokemon2.getDateCaught().compareTo(pokemon1.getDateCaught())),
        BY_IV((pokemon1, pokemon2) -> pokemon2.getPerfectIv().compareTo(pokemon1.getPerfectIv()));

        private final Comparator<PokemonDecorator> comparator;

        OrderBy(Comparator<PokemonDecorator> comparator) {
            this.comparator = comparator;
        }

        public Comparator<PokemonDecorator> getComparator() {
            return comparator;
        }
    }

}
