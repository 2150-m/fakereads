package wpproject.project.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wpproject.project.dto.DTO_AccountAuthorNew;
import wpproject.project.model.Account;
import wpproject.project.model.Account_Role;
import wpproject.project.service.Service_Account;

@RestController
public class Controller_Rest_AccountAdmin {

    @Autowired
    private Service_Account serviceAccount;

    @PostMapping("/api/user/admin/addauthor")
    public Account admin_addAuthor(@RequestBody DTO_AccountAuthorNew DTOAccountAuthorNew, HttpSession session){

        // must be logged in
        Account user = (Account) session.getAttribute("user");
        if (user == null) { System.err.println("[x] you have to be logged in"); return null; }

        // must be admin
        user = serviceAccount.findOne(user.getId());
        if (user.getAccountRole() != Account_Role.ADMINISTRATOR) { System.err.println("[x] not admin: " + user); return null; }

        try {
            // check if exists by mail/username
            if (serviceAccount.findOneByMailAddress(DTOAccountAuthorNew.getMailAddress()) != null) { System.err.println("[x] can't add new user (author), mail exists:"     + DTOAccountAuthorNew.getMailAddress()); return null; }
            if (serviceAccount.findOneByUsername(DTOAccountAuthorNew.getUsername()) != null)       { System.err.println("[x] can't add new user (author), username exists:" + DTOAccountAuthorNew.getUsername());    return null; }

            Account newAccount = new Account(
                    DTOAccountAuthorNew.getFirstName(),
                    DTOAccountAuthorNew.getLastName(),
                    DTOAccountAuthorNew.getUsername(),
                    DTOAccountAuthorNew.getMailAddress(),
                    DTOAccountAuthorNew.getPassword()
            );

            newAccount.setAccountRole(Account_Role.AUTHOR);

            serviceAccount.save(newAccount);

            return newAccount;
        } catch (Exception e) {
            System.err.println("[x] failed to add new user (author): " + e.getMessage());
            return null;
        }
    }
}
