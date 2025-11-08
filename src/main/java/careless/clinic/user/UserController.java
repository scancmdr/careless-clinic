package careless.clinic.user;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/public/one")
    public String index2() {
        return "public/one";
    }

    @GetMapping("/page-two")
    public String pageTwo(Model model) {
        // In production with real OAuth2 credentials, the SecurityFilterChain
        // will ensure only authenticated users can access this page
        model.addAttribute("name", "Test User");
        model.addAttribute("email", "test@example.com");
        model.addAttribute("picture", "https://via.placeholder.com/150");
        return "page-two";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
