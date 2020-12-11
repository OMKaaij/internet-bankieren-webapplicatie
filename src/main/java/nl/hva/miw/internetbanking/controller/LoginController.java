package nl.hva.miw.internetbanking.controller;

import nl.hva.miw.internetbanking.data.dto.CustomerAccountDTO;
import nl.hva.miw.internetbanking.model.Account;
import nl.hva.miw.internetbanking.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class LoginController {

    private CustomerService customerService;

    @Autowired
    public LoginController(CustomerService customerService) { //@Richard Knol
        this.customerService = customerService;
    }

    @GetMapping("/login") //@Richard Knol
    public String showLogin() {
        return "pages/login";
    }

    @PostMapping("/login") //@Richard Knol
    public String handleLogin(@RequestParam String userName, String password, Model model) { // Gaat iets doen met ingevoerde G en W


        CustomerAccountDTO c = customerService.getCustomerByUsernameAndPassword(userName, password);
        if (c != null) {
            List<Account> accountList = c.getAccountList();
            for (Account a : accountList) {
                CustomerAccountDTO b = customerService.getCustomersByAccount(a.getAccountID());
                model.addAttribute("accountWithCustomerList", b);
                System.out.println(b.getCustomerList());
            }


//        NaturalPerson np = customerService.getNpByCustomerId(c.getCustomer().getId());
//        model.addAttribute("fullName", String
//                .format("%s %s %s", np.getFirstName(), np.getPreposition(), np.getSurName()));

            model.addAttribute("customerWithAccountOverview", c);
            return "pages/rekeningoverzicht";
        }
        return "pages/foutpagina";

    }
}
