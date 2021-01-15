package nl.hva.miw.internetbanking.data.dao;

import lombok.extern.slf4j.Slf4j;
import nl.hva.miw.internetbanking.data.dto.BalancePerSectorDTO;
import nl.hva.miw.internetbanking.data.dto.CompanyTransactionDTO;
import nl.hva.miw.internetbanking.data.dto.LegalPersonHasAccountDTO;
import nl.hva.miw.internetbanking.data.mapper.BalancePerSectorRowMapper;
import nl.hva.miw.internetbanking.data.mapper.CompanyTransactionRowMapper;
import nl.hva.miw.internetbanking.data.mapper.LegalPersonHasAccountRowMapper;
import nl.hva.miw.internetbanking.data.mapper.LegalPersonRowMapper;
import nl.hva.miw.internetbanking.model.LegalPerson;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class LegalPersonDAO implements DAO<LegalPerson, Long> {

    private final JdbcTemplate jdbcTemplate;

    public LegalPersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void create(LegalPerson legalPerson) {
        String sql = "INSERT INTO legalperson(companyID, companyName, kvkNumber, sector, " +
                     "vatNumber, postalCode, homeNumber, street, residence, accountmanagerID) " +
                     "VALUES" +
                     "(?,?,?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, legalPerson.getCustomerID());
            ps.setString(2, legalPerson.getCompanyName());
            ps.setLong(3, legalPerson.getKvkNumber());
            ps.setString(4, legalPerson.getSector().name());
            ps.setString(5, legalPerson.getVatNumber());
            ps.setString(6, legalPerson.getPostalCode());
            ps.setString(7, legalPerson.getHomeNumber());
            ps.setString(8, legalPerson.getStreet());
            ps.setString(9, legalPerson.getResidence());
            ps.setLong(10, legalPerson.getAccountmanagerID());
            return ps;
        });
    }

    @Override
    public Optional<LegalPerson> read(Long id) {
        try {
            String sql = "SELECT * FROM legalperson WHERE companyID = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new LegalPersonRowMapper(),
                    id));
        } catch (Exception e) {
            log.error("LegalPerson not found in database.");
        }
        return null;
    }

    @Override
    public void update(LegalPerson legalPerson) {
        String sql = "UPDATE legalperson SET companyID = ?, companyName = ?, kvkNumber = ?, " +
                     "sector = ?, vatNumber = ?, postalCode = ?, homeNumber = ?, street = ?, " +
                     "residence" +
                     " = ?, accountmanagerID = ?";
        jdbcTemplate.update(sql, legalPerson.getCustomerID(), legalPerson.getCompanyName(),
                legalPerson.getKvkNumber(), legalPerson.getSector(), legalPerson.getVatNumber(),
                legalPerson.getPostalCode(), legalPerson.getHomeNumber(), legalPerson.getStreet(),
                legalPerson.getResidence(), legalPerson.getAccountmanagerID());
    }

    @Override
    public void delete(LegalPerson legalPerson) {
        jdbcTemplate.update("DELETE FROM legalperson WHERE companyID = ?",
                            legalPerson.getCustomerID());
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM legalperson WHERE companyID = ?", id);
    }

    @Override
    public Optional<List<LegalPerson>> list() {
        String sql = "SELECT * FROM legalperson";
        return Optional.of(jdbcTemplate.query(sql, new LegalPersonRowMapper()));
    }

    public Optional<LegalPerson> readByKvkNumber(long kvkNumber) {
        String sql = "SELECT * FROM legalperson WHERE kvkNumber = ?";
        return Optional
                .ofNullable(
                        jdbcTemplate.queryForObject(sql, new LegalPersonRowMapper(), kvkNumber));
    }

    public List<BalancePerSectorDTO> getAvgBalancePerSector() {
        final String sql = "SELECT l.sector, AVG(a.balance) balance\n" +
                           "FROM legalperson l JOIN customer c ON c.customerID=l.companyID \n" +
                           "JOIN customer_has_account cha ON cha.customerID=l.companyID \n" +
                           "JOIN account a ON a.accountID=cha.accountID\n" +
                           "GROUP BY sector\n" +
                           "ORDER BY balance DESC;";
        return jdbcTemplate.query(sql, new BalancePerSectorRowMapper());
    }

    public List<CompanyTransactionDTO> getMostActiveClients() {
        final String sql = "SELECT L.companyName, COUNT(T.transactionID) numberOfTransactions, L" +
                           ".kvkNumber, \n" +
                           "CONCAT(L.street, \" \", L.homeNumber, \", \", L.residence) AS " +
                           "address, \n" +
                           "E.firstName, E.preposition, E.surName\n" +
                           "FROM transaction_has_account T JOIN account A ON T.accountID=A" +
                           ".accountID\n" +
                           "JOIN customer_has_account C ON C.accountID=T.accountID JOIN " +
                           "legalperson L\n" +
                           "ON L.companyID=C.customerID JOIN employee E ON E.employeeID=l" +
                           ".accountmanagerID\n" +
                           "GROUP BY L.companyName\n" +
                           "ORDER BY numberOfTransactions DESC LIMIT 10;";
        return jdbcTemplate.query(sql, new CompanyTransactionRowMapper());
    }

    public List<LegalPersonHasAccountDTO> getClientsWithHighestBalance() {
        final String sql = "SELECT l.companyName, a.iban, a.balance, l.kvkNumber, l.sector, CONCAT(l.street, \" \", l.homeNumber, \" \", l.residence) AS address,\n" +
                "e.firstName, e.preposition, e.surName\n" +
                "FROM customer_has_account cha JOIN customer c ON cha.customerID = c.customerID\n" +
                "JOIN account a ON cha.accountID = a.accountID JOIN legalperson l ON l.companyID = cha.customerID\n" +
                "JOIN employee e ON e.employeeID=l.accountmanagerID\n" +
                "WHERE c.customerType = 'LEGAL' ORDER BY balance DESC LIMIT 10;";
        return jdbcTemplate.query(sql, new LegalPersonHasAccountRowMapper());
    }

}
