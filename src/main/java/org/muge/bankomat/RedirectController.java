package org.muge.bankomat;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectController {

    @GetMapping("/")
    public String redirectToPersonList() {
        return "redirect:/person-list";
    }
}
