package nl.hva.miw.internetbanking.service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import nl.hva.miw.internetbanking.data.dao.AccountDAO;
import nl.hva.miw.internetbanking.data.dto.AccountTransactionDTO;
import nl.hva.miw.internetbanking.model.Account;
import nl.hva.miw.internetbanking.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AccountService {

    private AccountDAO accountDao;
    @Getter
    @Setter
    private AccountTransactionDTO accountTransactionDTO;

    @Autowired
    public AccountService(AccountDAO accountDAO) {
        this.accountDao = accountDAO;
        log.warn("New AccountService.");
    }

    public List<Account> getAccountsForCustomer(Customer customer) {
        return accountDao.getAccountsForCustomer(customer);
    }

    public List<Account> getAccountsByCustomerId(long customerId) {
        return accountDao.getAccountsByCustomerId(customerId);
    }

    private Optional <Account> getAccountDetails (Optional<Account> optionalAccount) {
        if(optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            accountDao.read(account.getAccountID());
            return Optional.of(account);
        }
        return optionalAccount;
    }

    public Optional <Account> getAccountById (long accountID) {
        return getAccountDetails(accountDao.read(accountID));
    }

    public Optional<Account> getAccountByIban (String iban) {
        return getAccountByIban(String.valueOf(accountDao.read(iban)));
    }

    //@Author Veroniek
    public void saveNewAccount(Account account) {
        accountDao.create(account);
    }

}
