package wpproject.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import wpproject.project.service.Service_Account;

@Controller
public class Controller_Basic_Account {
    @Autowired
    private Service_Account serviceAccount;

    @GetMapping("/home")
    public String home() {
        return "index.html";
    }

    @GetMapping("/")
    public String nothing() {
        return "redirect:/home";
    }
}
