package nl.hva.miw.internetbanking.controller;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import nl.hva.miw.internetbanking.data.dto.*;
import nl.hva.miw.internetbanking.model.Account;
import nl.hva.miw.internetbanking.model.Customer;
import nl.hva.miw.internetbanking.model.Transaction;
import nl.hva.miw.internetbanking.service.AccountService;
import nl.hva.miw.internetbanking.service.CustomerService;
import nl.hva.miw.internetbanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@SessionAttributes({"customer", "nameCurrentCus"})
@Slf4j(topic="accountDetailsOverview")
public class AccountDetailsController {

    private AccountService accountService;
    private TransactionService transactionService;
    private CustomerService customerService;


    @Autowired
    public AccountDetailsController (AccountService accountService, TransactionService transactionService, CustomerService customerService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.customerService = customerService;
    }

//    @GetMapping ("/account_details{id}")
//    public String showAccounts (@PathVariable ("id") long accountID, Model model) {
//        Optional<Account> account = accountService.getAccountById(accountID);
//        model.addAttribute(account);
//        model.addAttribute("customer");
//        return "pages/details_overview";
//    }

    @GetMapping("/account_details/{id}")
    public String AccountDetailsHandler (@PathVariable ("id") long accountID,
                                         @ModelAttribute ("customer") Customer customer, Model model) {
        Optional<Account> account = accountService.getAccountById(accountID);
        model.addAttribute("account", account.get());
        AccountHasTransactionsDTO accountHasTransactionsDTO = new AccountHasTransactionsDTO();
        accountHasTransactionsDTO.setTransactionList(transactionService.getTransactionsForAccount(account.get()));
        transactionService.setTransactionWithContraAccountNames(accountHasTransactionsDTO, account.get());
        model.addAttribute("accountWithTransactions", accountHasTransactionsDTO);
        return "pages/account_details";
    }

}

