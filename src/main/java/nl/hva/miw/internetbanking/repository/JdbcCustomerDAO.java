package nl.hva.miw.internetbanking.repository;

import nl.hva.miw.internetbanking.DTO.CustomerAccountDTO;
import nl.hva.miw.internetbanking.model.Account;
import nl.hva.miw.internetbanking.model.Customer;
import nl.hva.miw.internetbanking.model.LegalPerson;
import nl.hva.miw.internetbanking.model.NaturalPerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Repository
public class JdbcCustomerDAO implements CustomerDAO {

    private JdbcTemplate jdbcTemplate;
    private JdbcAccountDAO jdbcAccountDAO;

    @Autowired
    public JdbcCustomerDAO(JdbcTemplate jdbcTemplate, JdbcAccountDAO jdbcAccountDAO) {
        super();
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcAccountDAO = jdbcAccountDAO;
    }

    @Override //@Richard Knol
    public CustomerAccountDTO getCustomerByUsernameAndPassword(String userName, String password) {
        try {
            final String sql = "SELECT * FROM customer WHERE userName = ? AND password = ?";
            Customer customer = jdbcTemplate.queryForObject(sql, new CustomerRowMapper(), userName, password);
            List<Account> accountList = null;
            if (customer != null) {
                accountList = jdbcAccountDAO.getAccountsByCustomerId(customer.getId());
            }
            return new CustomerAccountDTO(customer, accountList);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @Override //@Richard Knol
    public Customer getCustomerById(long id) {
        final String sql = "SELECT * FROM customer WHERE customerID = ?";
        return jdbcTemplate.queryForObject(sql, new CustomerRowMapper(), id);
    }

    // onderstaande methode toegevoegd door Nina 09-12-2020
    @Override
    public NaturalPerson getNpByCustomerId(long customerId) {
        final String sql = "SELECT * FROM NaturalPerson WHERE customerID = ?";
        NaturalPerson np = jdbcTemplate.queryForObject(sql, new NaturalPersonRowMapper(), customerId);
        if (!np.equals(null)) {
            return np;
        }

        //todo: if Customer result = null

        //throw new ResponseStatusException(NOT_FOUND, "Persoon met id:" + customerId + " kan niet gevonden worden");
        return null;
    }


    // onderstaande methode toegevoegd door Nina 09-12-2020
    // voor Legal Person:
//    @Override
//    public LegalPerson getLpByCustomerId(long customerId) {
//       final String sql = "SELECT * FROM LegalPerson WHERE customerID = ?";
//        LegalPerson lp = jdbcTemplate.queryForObject(sql, new LegalPersonRowMapper(), customerId);
//            if (!lp.equals(null)) {
//                return lp;
//            }
//
//        throw new ResponseStatusException(NOT_FOUND, "Persoon met id:" + customerId + " kan niet gevonden worden");
//    }



}
