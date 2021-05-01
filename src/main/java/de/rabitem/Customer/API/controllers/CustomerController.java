package de.rabitem.Customer.API.controllers;

import de.rabitem.Customer.API.entities.Customer;
import de.rabitem.Customer.API.exceptions.CustomerNotFoundException;
import de.rabitem.Customer.API.services.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(final CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/getCustomerMailById")
    public ResponseEntity<String> getCustomerMailById(@RequestParam(name = "id", required = true) int id) {
        Optional<Customer> customer = this.customerService.getCustomerById(id);
        return customer.map(value -> ResponseEntity.ok(value.getEmail())).
                orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error while getting Customer with Id "
                        + id + "!"));
    }

    @DeleteMapping("/deleteCustomerById")
    public ResponseEntity<String> deleteCustomerById(@RequestParam(name = "id", required = true) int id) {
        Optional<Customer> customer = this.customerService.getCustomerById(id);
        if (customer.isPresent()) {
            this.customerService.deleteCustomer(customer.get());
            return ResponseEntity.ok("Customer deleted successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error while deleting Customer with Id "
                    + id + "!");
        }
    }

    @PostMapping("/addCustomer")
    public ResponseEntity<Customer> addCustomer(@RequestBody(required = true) final Customer customer) {
        this.customerService.addCustomer(customer);
        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }
}
