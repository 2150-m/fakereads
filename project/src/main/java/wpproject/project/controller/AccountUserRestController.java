package wpproject.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import wpproject.project.service.AccountUserService;

@RestController
public class AccountUserRestController {
    @Autowired
    private AccountUserService accountUserService;

    @GetMapping("/api")
    public String welcome() {
        return "Hello from api";
    }
}
