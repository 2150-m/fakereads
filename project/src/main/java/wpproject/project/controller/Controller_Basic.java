package wpproject.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import wpproject.project.service.Service_Account;

@Controller
public class Controller_Basic {
    @Autowired
    private Service_Account serviceAccount;

    @GetMapping("/home")
    public String home() { return "index.html"; }

    @GetMapping("/")
    public String root() {
        return "redirect:/home";
    }


    @GetMapping("/items")
    public String items() { return "items.html"; }

    @GetMapping("/items/{id}")
    public String getBook(@PathVariable(name = "id") Long id, Model model) {
        model.addAttribute("item_id", id);
        return "item.html";
    }

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @GetMapping("/myaccount")
    public String myaccount() {
        return "myaccount.html";
    }

    @GetMapping("/register")
    public String register() {
        return "register.html";
    }

    @GetMapping("/logout")
    public String logout() {
        return "logout.html";
    }

    @GetMapping("/users")
    public String users() {
        return "users.html";
    }

}
