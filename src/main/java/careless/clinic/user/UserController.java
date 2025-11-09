package careless.clinic.user;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for user-facing pages and authentication endpoints.
 *
 * @author jay
 */
@Controller
public class UserController {

    /**
     * Displays the main index page.
     *
     * @return view name "index"
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * Displays public page one.
     *
     * @return view name "public/one"
     */
    @GetMapping("/public/one")
    public String index2() {
        return "public/one";
    }

    /**
     * Displays the login page.
     *
     * @return view name "login"
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
