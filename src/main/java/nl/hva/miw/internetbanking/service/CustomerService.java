package nl.hva.miw.internetbanking.service;

import nl.hva.miw.internetbanking.model.Customer;
import nl.hva.miw.internetbanking.model.LegalPerson;
import nl.hva.miw.internetbanking.model.NaturalPerson;
import nl.hva.miw.internetbanking.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService implements GenericService {

    CustomerRepository customerRepository;
    private Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        logger.warn("New CustomerService.");
    }

    @Override
    public void saveCustomer(Customer customer) {
        this.customerRepository.save(customer);
    }

    @Override
    public void saveNaturalPerson(NaturalPerson naturalPerson) {
        this.customerRepository.save(naturalPerson);
    }

    @Override
    public void saveLegalPerson(LegalPerson legalPerson) {
        this.customerRepository.save(legalPerson);
    }

    @Override
    public Customer getCustomerById(long id) {
        Optional<Customer> optional = customerRepository.findById(id);
        Customer employee = null;
        if (optional.isPresent()) {
            employee = optional.get();
        }
        else {
            throw new RuntimeException("Employee not found for id :: " + id);
        }
        return employee;
    }
}
