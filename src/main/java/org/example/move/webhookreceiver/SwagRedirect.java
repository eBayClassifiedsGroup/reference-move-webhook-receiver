package org.example.move.webhookreceiver;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SwagRedirect {

    @GetMapping("/")
    public String greeting(HttpServletRequest httpServletRequest) {
        Optional<String> prefix = Optional.ofNullable(httpServletRequest.getHeader("x-forwarded-prefix"));
        return "redirect:" + prefix.orElse("") + "/swagger-ui.html";
    }

}
