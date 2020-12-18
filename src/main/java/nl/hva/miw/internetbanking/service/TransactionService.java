package nl.hva.miw.internetbanking.service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import nl.hva.miw.internetbanking.data.dao.TransactionDAO;
import nl.hva.miw.internetbanking.model.Account;
import nl.hva.miw.internetbanking.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TransactionService {

    @Getter
    @Setter
    private TransactionDAO transactionDAO;

    @Autowired
    public TransactionService (TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
        log.warn("New TransactionService");
    }

    public List<Transaction> getTransactionsByAccountId (long accountID) {
        return transactionDAO.getTransactionsByAccountId(accountID);
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

    public void doTransaction(Account fromAccount, Account toAccount) {
//        Transaction transaction = new Transaction();
//        transactionDAO.create(transaction);
    }
}
