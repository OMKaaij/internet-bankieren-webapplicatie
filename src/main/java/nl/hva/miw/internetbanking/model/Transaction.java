package nl.hva.miw.internetbanking.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Transaction implements Serializable {

    private long transactionID;
    private String debitAccount;
    private String creditAccount;
    private double amount;
    private String description;
    private LocalDateTime date;
    private String showDate;
    private Account account;
    private List<String> contraAccountHolderNames;


    public Transaction(String debitAccount, String creditAccount, double amount, String description, LocalDateTime date, Account account) {
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.showDate = convertToshowDate();
        this.account = account;
        this.contraAccountHolderNames = new ArrayList<>();
    }

    public Transaction(String debitAccount, String creditAccount, double amount, String description, LocalDateTime date) {
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.showDate = convertToshowDate();
        this.contraAccountHolderNames = new ArrayList<>();
    }

    public Transaction(long transactionID, String debitAccount, String creditAccount, double amount, String description, LocalDateTime date) {
        this.transactionID = transactionID;
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.showDate = convertToshowDate();
        this.contraAccountHolderNames = new ArrayList<>();
    }

    public void addTransactionToAccount (Account account) {
        this.account = account;
    }

    public Transaction(long transactionID){
        this.transactionID = transactionID;
    }

    public Transaction() {
    }

    public long getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(long transactionID) {
        this.transactionID = transactionID;
    }

    public String getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(String debetAccountNo) {
        this.debitAccount = debetAccountNo;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(String creditAccountNo) {
        this.creditAccount = creditAccountNo;
    }

    public double transactionAmount() {
        if (account.getIban().equals(debitAccount)) {
            amount = 0 - amount;
        }
        if(account.getIban().equals(creditAccount)) {

        }
        return amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getShowDate() {
        return showDate;
    }

    public void setShowDate(String showDate) {
        this.showDate = showDate;
    }

    public List<String> getContraAccountHolderNames() {
        return contraAccountHolderNames;
    }

    public void setContraAccountHolderNames(List<String> contraAccountHolderNames) {
        this.contraAccountHolderNames = contraAccountHolderNames;
    }

    public void addDebetAccountHolderName (String name) {
        contraAccountHolderNames.add(name);
    }

    public String convertToshowDate() {
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("d MMMM yyyy");
        return date.format(formatDate);
    }

    public String showDateTime() {
        DateTimeFormatter formatDateTime = DateTimeFormatter.ofPattern("d MMMM yyyy");
        return date.format(formatDateTime);
    }

    public String showContraAccount() {
        if (account.getIban().equals(debitAccount)) {
            return creditAccount;
        } else
            return debitAccount;
    }

    public String showAmount() {
        if (transactionAmount() > 0) {
            return String.format("€ +%.2f", transactionAmount());
        } else {
            return String.format("€ -%.2f", transactionAmount());
        }
    }

    public String showTransactionDetails() {
        return String.format("TransactieNo: %d\n\tTegenrekening: %s\n\tDatum/tijd: %s\n\tOmschrijving: %s",
                transactionID, showContraAccount(), showDateTime(),
                description);
    }


    @Override
    public String toString() {
        return "Transaction{" +
                "transactionID=" + transactionID +
                ", debitAccount='" + debitAccount + '\'' +
                ", creditAccount='" + creditAccount + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", showDate='" + showDate + '\'' +
                ", date=" + date +
                ", contraAccountHolderNames=" + contraAccountHolderNames +
                '}';
    }
}
