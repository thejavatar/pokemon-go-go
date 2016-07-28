package com.thejavatar.pokemongogo;

import com.pokegoapi.auth.GoogleAutoCredentialProvider;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
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
        model.addAttribute("pokemons", apiFacade.getPokemons());
        return "pages/index";
    }

    @RequestMapping("/getPokemons")
    @ResponseBody
    public List<PokemonDecorator> getPokemons() {
        LOG.debug("getPokemons()");
        return apiFacade.getPokemons();
    }

    private void authenticateWithPokemonGo(String username, String password) throws LoginFailedException, RemoteServerException {
        apiFacade.setAuthentication(new GoogleAutoCredentialProvider(new OkHttpClient(), username, password));
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
}
