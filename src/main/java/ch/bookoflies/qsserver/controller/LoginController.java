package ch.bookoflies.qsserver.controller;

import ch.bookoflies.qsserver.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    private final UserService userService;

    LoginController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String register( // TODO improve local login
    ) {
        return "<div>\n" +
                "    <h2>Please Login</h2>\n" +
                "    <br/>\n" +
                "</div>\n" +
                "<div>\n" +
                "    <h4><a th:href=\"/@{/oauth2/authorization/google}\">Login with Google</a></h4>   \n" +
                "</div>\n" +
                "<div><p>OR</p></div>\n" +
                "     \n" +
                "<form th:action=\"@{/login}\" method=\"post\" style=\"max-width: 400px; margin: 0 auto;\">\n" +
                "<div class=\"border border-secondary rounded p-3\">\n" +
                "    <div th:if=\"${param.error}\">\n" +
                "        <p class=\"text-danger\">Invalid username or password.</p>\n" +
                "    </div>\n" +
                "    <div th:if=\"${param.logout}\">\n" +
                "        <p class=\"text-warning\">You have been logged out.</p>\n" +
                "    </div>\n" +
                "    <div>\n" +
                "        <p><input type=\"email\" name=\"email\" required class=\"form-control\" placeholder=\"E-mail\" /></p>\n" +
                "    </div>\n" +
                "    <div>\n" +
                "        <p><input type=\"password\" name=\"pass\" required class=\"form-control\" placeholder=\"Password\" /></p>\n" +
                "    </div>\n" +
                "    <div>\n" +
                "        <p><input type=\"submit\" value=\"Login\" class=\"btn btn-primary\" /></p>\n" +
                "    </div>\n" +
                "</div>\n" +
                "</form>";
    }
    
}
