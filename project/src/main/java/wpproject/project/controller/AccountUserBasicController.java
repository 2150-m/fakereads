package wpproject.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import wpproject.project.service.AccountUserService;

@Controller
public class AccountUserBasicController {
    @Autowired
    private AccountUserService accountUserService;

    @GetMapping("/home")
    public String home() {
        return "index.html";
    }

    @GetMapping("/")
    public String nothing() {
        return "redirect:/home";
    }
}
