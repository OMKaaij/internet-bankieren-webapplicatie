package nl.hva.miw.internetbanking.data.dao;

import nl.hva.miw.internetbanking.data.mapper.EmployeeRowMapper;
import nl.hva.miw.internetbanking.data.mapper.LegalPersonRowMapper;
import nl.hva.miw.internetbanking.data.mapper.TransactionRowMapper;
import nl.hva.miw.internetbanking.model.Transaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TransactionDAO implements DAO<Transaction, Long> {

    private final JdbcTemplate jdbcTemplate;

    public TransactionDAO(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(Transaction transaction) {
        String sql = "INSERT INTO TRANSACTION (debetAccount, creditAccount, amount, description, dateTime) " +
                "VALUES(?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"transactionID"});
            ps.setString(1, transaction.getDebetAccount());
            ps.setString(2, transaction.getCreditAccount());
            ps.setDouble(3, transaction.getAmount());
            ps.setString(4, transaction.getDescription());
            ps.setDate(5, Date.valueOf(transaction.getDate()));
            return ps;
        }, keyHolder);
        long id = Objects.requireNonNull(keyHolder.getKey().longValue());
        transaction.setTransactionID(id);
    }

    @Override
    public Optional<Transaction> read(Long transactionID) {
        String sql = "SELECT * FROM transaction WHERE transactionID = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new TransactionRowMapper(), transactionID));
    }

    @Override
    public void update(Transaction transaction) {
        String sql = "UPDATE transaction SET debetAccount = ?, creditAccount = ?, amount = ?" +
                ", description = ?, date = ?" + "WHERE transactionID = ?";
        jdbcTemplate.update(sql, transaction.getDebetAccount(),
                transaction.getCreditAccount(),
                transaction.getAmount(),
                transaction.getDescription(),
                transaction.getDate());
    }

    @Override
    public void delete(Transaction transaction) {
        jdbcTemplate.update("DELETE FROM transaction WHERE transactionID = ?",
                transaction.getTransactionID());
    }

    @Override
    public void deleteById(Long transactionID) {
        jdbcTemplate.update("DELETE FROM transaction WHERE transactionID = ?", transactionID);
    }

    @Override
    public Optional<List<Transaction>> list() {
        String sql = "SELECT * FROM transaction";
        return Optional.of(jdbcTemplate.query(sql, new TransactionRowMapper()));
    }
}
