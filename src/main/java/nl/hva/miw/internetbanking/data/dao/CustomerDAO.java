package nl.hva.miw.internetbanking.data.dao;

import lombok.extern.slf4j.Slf4j;
import nl.hva.miw.internetbanking.data.mapper.CustomerRowMapper;
import nl.hva.miw.internetbanking.model.Customer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@Slf4j(topic = "CustomerDAO")
public class CustomerDAO implements DAO<Customer, Long> {

    private final JdbcTemplate jdbcTemplate;

    public CustomerDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void create(Customer customer) {
        final String sql = "INSERT INTO customer(userName, password, customerType) VALUES(?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"customerId"});
            ps.setString(1, customer.getUserName());
            ps.setString(2, customer.getPassword());
            ps.setString(3, customer.getCustomerType().name());
            return ps;
        }, keyHolder);
        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        customer.setCustomerID(id);
    }

    @Override
    public Optional<Customer> read(Long customerID) {
        final String sql = "SELECT * FROM customer WHERE customerID = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new CustomerRowMapper(), customerID));
    }

    @Override
    public void update(Customer customer) {
        final String sql = "UPDATE customer SET userName = ?, password = ? WHERE customerID = ?";
        jdbcTemplate.update(sql, customer.getUserName(), customer.getPassword(),
                            customer.getCustomerID());
    }

    @Override
    public void delete(Customer customer) {
        jdbcTemplate.update("DELETE FROM customer WHERE customerID = ?", customer.getCustomerID());
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM customer WHERE customerID = ?", id);
    }

    @Override
    public Optional<List<Customer>> list() {
        return Optional.of(jdbcTemplate.query("SELECT * FROM customer", new CustomerRowMapper()));
    }

    public Optional<Customer> read(String userName) {
        String sql = "SELECT * FROM customer WHERE userName = ?";
        return Optional
                .ofNullable(jdbcTemplate.queryForObject(sql, new CustomerRowMapper(), userName));
    }

    public boolean existsByUsername(String username) {
        final String sql = "SELECT * FROM customer WHERE userName = ?";
        return jdbcTemplate.queryForObject(sql, new CustomerRowMapper(), username) != null;
    }

    public List<Customer> getCustomerByAccountId(long accountId) {
        final String sql = "SELECT customer.customerID, customer.userName, customer.password, " +
                           "customer.customerType, account.accountID, account.iban, account" +
                           ".balance\n" +
                           "FROM customer_has_account JOIN customer ON customer_has_account" +
                           ".customerID=customer.customerID JOIN account ON\n" +
                           "account.accountID=customer_has_account.accountID WHERE account" +
                           ".accountID = ?";
        return jdbcTemplate.query(sql, new CustomerRowMapper(), accountId);
    }

    public Customer getCustomerByAccountForTransaction (long accountID) {
        final String sql = "SELECT customer.customerID, customer.userName, customer.password, " +
                           "customer.customerType, account.accountid, account.iban, account" +
                           ".balance\n" +
                           "FROM customer_has_account JOIN customer ON customer_has_account" +
                           ".customerID=customer.customerID JOIN account ON\n" +
                           "account.accountID=customer_has_account.accountID WHERE account" +
                           ".accountID = ?";
        return jdbcTemplate.queryForObject(sql, new CustomerRowMapper(), accountID);
    }

    public List<Customer> getCustomerListByIban (String iban) {
        final String sql = "SELECT customer.customerID, customer.userName, customer.password, " +
                           "customer.customerType, account.accountID, account.iban, account" +
                           ".balance\n" +
                           "FROM customer_has_account JOIN customer ON customer_has_account" +
                           ".customerID=customer.customerID JOIN account ON\n" +
                           "account.accountID=customer_has_account.accountID WHERE account" +
                           ".iban = ?";
        return jdbcTemplate.query(sql, new CustomerRowMapper(), iban);
    }

    public Customer getCustomerByIban (String iban) {
        final String sql = "SELECT customer.customerID, customer.userName, customer.password, " +
                           "customer.customerType, account.accountID, account.iban, account" +
                           ".balance\n" +
                           "FROM customer_has_account JOIN customer ON customer_has_account" +
                           ".customerID=customer.customerID JOIN account ON\n" +
                           "account.accountID=customer_has_account.accountID WHERE account" +
                           ".iban = ?";
        return jdbcTemplate.queryForObject(sql, new CustomerRowMapper(), iban);
    }

}
