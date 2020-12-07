package nl.hva.miw.internetbanking.model;

import java.time.LocalDateTime;

public class Transaction {

    private int transactionId;
    private String debetAccountNo;
    private String creditAccountNo;
    private double amount;
    private String description;
    private LocalDateTime dateTime;

    public Transaction(int transactionId, String debetAccountNo, String creditAccountNo, double amount, String description, LocalDateTime dateTime) {
        this.transactionId = transactionId;
        this.debetAccountNo = debetAccountNo;
        this.creditAccountNo = creditAccountNo;
        this.amount = amount;
        this.description = description;
        this.dateTime = dateTime;
    }

    public Transaction() {
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getDebetAccountNo() {
        return debetAccountNo;
    }

    public void setDebetAccountNo(String debetAccountNo) {
        this.debetAccountNo = debetAccountNo;
    }

    public String getCreditAccountNo() {
        return creditAccountNo;
    }

    public void setCreditAccountNo(String creditAccountNo) {
        this.creditAccountNo = creditAccountNo;
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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", debetAccountNo='" + debetAccountNo + '\'' +
                ", creditAccountNo='" + creditAccountNo + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }
}
