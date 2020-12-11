package nl.hva.miw.internetbanking.data.dao;

import nl.hva.miw.internetbanking.model.Account;

import java.util.List;

// aangemaakt door Nina 09-12-2020

public interface AccountDAO {
    List<Account> getAccountsByCustomerId(long customerId);
    void saveAccount(Account account);
}
