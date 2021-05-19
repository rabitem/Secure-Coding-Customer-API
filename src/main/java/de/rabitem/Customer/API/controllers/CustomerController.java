package de.rabitem.Customer.API.controllers;

import de.rabitem.Customer.API.entities.Customer;
import de.rabitem.Customer.API.services.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(final CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(value = "/getCustomerMailById")
    public ResponseEntity<String> getCustomerMailById(@RequestParam(name = "id", required = true) int id) {
        Optional<Customer> customer = this.customerService.getCustomerById(id);
        return customer.map(value -> ResponseEntity.ok(value.getEmail())).
                orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error while getting Customer with Id "
                        + id + "!"));
    }

    @DeleteMapping(value = "/deleteCustomerById", consumes = MediaType.APPLICATION_JSON_VALUE)
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

    @PostMapping(value = "/addCustomer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Customer> addCustomer(@Valid @RequestBody(required = true) final Customer customer) {
        this.customerService.addCustomer(customer);
        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(final MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
