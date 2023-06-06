package wpproject.project.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wpproject.project.dto.*;
import wpproject.project.model.*;
import wpproject.project.service.Service_Account;
import wpproject.project.service.Service_AccountActivationRequest;
import wpproject.project.service.Service_AccountAuthor;
import wpproject.project.service.Service_BookGenre;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class Controller_Rest_AccountAdmin {

    @Autowired
    private Service_Account serviceAccount;
    @Autowired
    private Service_AccountAuthor serviceAccountAuthor;
    @Autowired
    private Service_AccountActivationRequest serviceAccountActivationRequest;
    @Autowired
    private Service_BookGenre serviceBookGenre;

    private Boolean isAdmin(HttpSession session) {
        // must be logged in
        Account user = (Account) session.getAttribute("user");
        if (user == null) { System.err.println("[x] you have to be logged in"); return false; }

        // must be admin
        user = serviceAccount.findOne(user.getId());
        if (user.getAccountRole() != Account_Role.ADMINISTRATOR) { System.err.println("[x] not admin: " + user); return false; }

        return true;
    }

    @PostMapping("/api/user/admin/addauthor")
    public AccountAuthor admin_addAuthor(@RequestBody DTO_Post_AccountAuthor DTOAccountAuthorNew, HttpSession session){

        // check if admin
        if (!isAdmin(session)) { return null; }

        try {
            // check if exists by mail/username
            if (serviceAccount.findOneByMailAddress(DTOAccountAuthorNew.getMailAddress()) != null) { System.err.println("[x] can't add new user (author), mail exists:"     + DTOAccountAuthorNew.getMailAddress()); return null; }
            if (serviceAccount.findOneByUsername(DTOAccountAuthorNew.getUsername()) != null)       { System.err.println("[x] can't add new user (author), username exists:" + DTOAccountAuthorNew.getUsername());    return null; }

            AccountAuthor newAuthor = new AccountAuthor(new Account(
                    DTOAccountAuthorNew.getFirstName(),
                    DTOAccountAuthorNew.getLastName(),
                    DTOAccountAuthorNew.getUsername(),
                    DTOAccountAuthorNew.getMailAddress(),
                    DTOAccountAuthorNew.getPassword()
            ));

            newAuthor.setAccountRole(Account_Role.AUTHOR);

            serviceAccount.save(newAuthor);
            serviceAccountAuthor.save(newAuthor);

            return newAuthor;
        } catch (Exception e) {
            System.err.println("[x] failed to add new user (author): " + e.getMessage());
            return null;
        }
    }

    @GetMapping("/api/user/admin/activations")
    public ResponseEntity<List<DTO_View_AccountActivationRequest>> getActivationRequests(HttpSession session) {

        // check if admin
        if (!isAdmin(session)) { return null; }

        List<AccountActivationRequest> userList = serviceAccountActivationRequest.findAll();

        List<DTO_View_AccountActivationRequest> dtos = new ArrayList<>();
        for (AccountActivationRequest u : userList) {
            DTO_View_AccountActivationRequest dto = new DTO_View_AccountActivationRequest(u);
            dtos.add(dto);
        }

        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/api/user/admin/activation/{id}/accept")
    public ResponseEntity<String> acceptActivationRequest(@PathVariable(name = "id") Long id, @RequestBody DTO_Post_MailMessage dtoPostMailMessage, HttpSession session) {

        // check if admin
        if (!isAdmin(session)) { return null; }

        AccountActivationRequest aar = serviceAccountActivationRequest.findById(id);
        if (aar == null) { return ResponseEntity.badRequest().body("request doesn't exist"); }

        // save as approved
        aar.setStatus(AccountActivationRequest_Status.APPROVED);
        serviceAccountActivationRequest.save(aar);

        // set is activated for author
        AccountAuthor accountAuthor = aar.getAuthor();
        accountAuthor.setAccountActivated(true);
        serviceAccountAuthor.save(accountAuthor);

        // TODO: send mail that account has been activated --- send password
        System.out.println("sent mail message from admin: " + dtoPostMailMessage.getMessage());

        return ResponseEntity.ok().body("accepted activation request");
    }

    @PostMapping("/api/user/admin/activation/{id}/reject")
    public ResponseEntity<String> rejectActivationRequest(@PathVariable(name = "id") Long id, @RequestBody DTO_Post_MailMessage dtoPostMailMessage, HttpSession session) {

        // check if admin
        if (!isAdmin(session)) { return null; }

        AccountActivationRequest aar = serviceAccountActivationRequest.findById(id);
        if (aar == null) { return ResponseEntity.badRequest().body("request doesn't exist"); }

        // save as rejected
        aar.setStatus(AccountActivationRequest_Status.REJECTED);
        serviceAccountActivationRequest.save(aar);

        // set account as not activated
        AccountAuthor accountAuthor = aar.getAuthor();
        accountAuthor.setAccountActivated(false);
        serviceAccountAuthor.save(accountAuthor);

        // TODO: send mail -> reason for rejection
        System.out.println("sent mail message from admin: " + dtoPostMailMessage.getMessage());

        return ResponseEntity.ok().body("rejected activation request");
    }

    @PostMapping("/api/user/admin/addgenre")
    public ResponseEntity<String> addGenre(@RequestBody DTO_Post_BookGenre dtoPostBookGenre, HttpSession session) {

        // check if admin
        if (!isAdmin(session)) { return null; }

        try {
            BookGenre bookGenre = serviceBookGenre.findOne(dtoPostBookGenre.getName());
            if (bookGenre != null) { return ResponseEntity.badRequest().body("book genre exists"); }

            BookGenre genre = new BookGenre(dtoPostBookGenre.getName());
            serviceBookGenre.save(genre);

            return ResponseEntity.ok().body("added new genre");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("failed to add genre: " + e.getMessage());
        }
    }

    // TODO: add new book

    // TODO: delete book (ONLY IF NO REVIEWS)

    // TODO: edit book

    // TODO: update unactivated author profile


}
