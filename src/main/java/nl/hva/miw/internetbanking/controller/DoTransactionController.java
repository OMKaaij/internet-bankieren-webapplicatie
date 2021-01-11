package nl.hva.miw.internetbanking.controller;

import lombok.extern.slf4j.Slf4j;
import nl.hva.miw.internetbanking.data.dto.CustomerHasAccountsDTO;
import nl.hva.miw.internetbanking.data.dto.TransactionDetailsDTO;
import nl.hva.miw.internetbanking.model.Account;
import nl.hva.miw.internetbanking.model.Customer;
import nl.hva.miw.internetbanking.model.NaturalPerson;
import nl.hva.miw.internetbanking.model.Transaction;
import nl.hva.miw.internetbanking.service.AccountService;
import nl.hva.miw.internetbanking.service.CustomerService;
import nl.hva.miw.internetbanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@Slf4j
@SessionAttributes({"customer", "nameCurrentCus"})
public class DoTransactionController {

    private TransactionService transactionService;
    private CustomerService customerService;
    private AccountService accountService;

    @Autowired
    public DoTransactionController(TransactionService transactionService, CustomerService customerService, AccountService accountService) {
        this.transactionService = transactionService;
        this.customerService = customerService;
        this.accountService = accountService;
    }

    @GetMapping("/do-transaction/")
    public String doTransactionHandler(@ModelAttribute("customer") Customer c, @ModelAttribute("nameCurrentCus") String currentCusName, Model model){
        CustomerHasAccountsDTO customerDto = new CustomerHasAccountsDTO(c);
        customerDto.setAccountList(accountService.getAccountsForCustomer(c));
        customerService.setCustomerWithAccounts(customerDto);
        model.addAttribute("nameCurrentCus", currentCusName);
        model.addAttribute("customerWithAccountOverview", customerDto);
        model.addAttribute("transactionDTO", new TransactionDetailsDTO());
        return "pages/do-transaction";
    }

    @PostMapping("/do-transaction/")
    public String postDoTransactionHandler(@ModelAttribute("customer") Customer c, @ModelAttribute("customerWithAccountOverview") CustomerHasAccountsDTO customerDto, @ModelAttribute("transactionDTO") TransactionDetailsDTO tDto, @ModelAttribute("nameCurrentCus") String currentCusName, Model model){
        customerDto.setAccountList(accountService.getAccountsForCustomer(c));
        customerService.setCustomerWithAccounts(customerDto);
        model.addAttribute("customerWithAccountOverview", customerDto);
        model.addAttribute("nameCurrentCus", currentCusName);
        model.addAttribute("transactionDTO", tDto);
        System.out.println("@@@@@@@@@@@@@@@@@ postDoTransactionHandler:  " + customerDto);
//        System.out.println("????????????????? " + currentCusName);
        System.out.println("!!!!!!!!!!!!!!!!! postDoTransactionHandler:" + tDto);
        return "pages/transaction-confirmation";
    }

    @PostMapping("/transaction-confirmed/")
    public String transactionConfirmationHandler(@ModelAttribute("customer") Customer c, @ModelAttribute("customerWithAccountOverview") CustomerHasAccountsDTO customerDto, @ModelAttribute("transactionConfirmedDTO") TransactionDetailsDTO tDto, Model model){
        customerDto.setAccountList(accountService.getAccountsForCustomer(c));
        customerService.setCustomerWithAccounts(customerDto);

        model.addAttribute("customerWithAccountOverview", customerDto);
        model.addAttribute("transactionDTO", tDto);
        model.addAttribute("customer", c);
        System.out.println("!!!!!!! transactionConfirmationHandler: " + tDto);

        transactionService.doTransaction(tDto, Transaction.class);
        return "pages/account-overview";
    }



}
