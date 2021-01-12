package nl.hva.miw.internetbanking.service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import nl.hva.miw.internetbanking.data.dao.AccountDAO;
import nl.hva.miw.internetbanking.data.dao.TransactionDAO;
import nl.hva.miw.internetbanking.data.dto.DTO;
import nl.hva.miw.internetbanking.model.*;
import nl.hva.miw.internetbanking.util.DtoMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TransactionService {

    @Getter
    @Setter
    private TransactionDAO transactionDAO;
    private AccountDAO accountDAO;

    @Autowired
    public TransactionService (TransactionDAO transactionDAO, AccountDAO accountDAO) {
        this.transactionDAO = transactionDAO;
        this.accountDAO = accountDAO;
    }

    public List<Transaction> getTransactionsForAccount (Account account) {
        return transactionDAO.getTransactionsForAccount(account);
    }

    private Optional<Transaction> getTransactionDetails (Optional<Transaction> optionalTransaction) {
        if(optionalTransaction.isPresent()) {
            Transaction transaction = optionalTransaction.get();
            transactionDAO.read(transaction.getTransactionID());
            return Optional.of(transaction);
        }
        return optionalTransaction;
    }

    public Optional <Transaction> getTransactionById (long transactionID) {
        return getTransactionDetails(transactionDAO.read(transactionID));
    }

    public Transaction getCreditTransaction (String iban) {
        return transactionDAO.getCreditTransaction(iban);
    }

    public Transaction getDebitTransaction (String iban) {
        return transactionDAO.getDebitTransaction(iban);
    }

    public <T> void doTransaction(DTO<T> dto, Class<T> transactionDetails) {
        T transaction = DtoMapperUtil.mapDtoToEntity(dto, transactionDetails);
        saveTransaction(transaction);
    }

    @Transactional
    public <T> void saveTransaction(T transaction) {
        try {
            transactionDAO.create((Transaction) transaction);
            doMoneyTransaction((Transaction) transaction);
        } catch (DataAccessException e) {
            log.warn("Exception occurred while attempting to save transaction: {}", e.getMessage());
        }
    }

    public void doMoneyTransaction(Transaction t){
        // update debit account:
        Account debitAccount = accountDAO.read(t.getDebitAccount()).get();
        Double newBalanceDebAcc = debitAccount.getBalance() - t.getAmount();
        debitAccount.setBalance(newBalanceDebAcc);
        accountDAO.update(debitAccount);

        // update credit account:
        Account creditAccount = accountDAO.read(t.getCreditAccount()).get();
        Double newBalanceCredAcc = creditAccount.getBalance() + t.getAmount();
        creditAccount.setBalance(newBalanceCredAcc);
        accountDAO.update(creditAccount);
    }
}
