package de.rabitem.Customer.API.services;

import de.rabitem.Customer.API.entities.Customer;
import de.rabitem.Customer.API.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(final CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Optional<Customer> getCustomerById(final int id) {
        return this.customerRepository.findById(id);
    }

    public void addCustomer(final Customer customer) {
        this.customerRepository.save(customer);
    }

    public void deleteCustomer(final Customer customer){
        this.customerRepository.delete(customer);
    }
}
