package com.apirestsample.app.controllers;

import com.apirestsample.app.entities.CustomerDTO;
import com.apirestsample.app.entities.CustomerEntity;
import com.apirestsample.app.services.CustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@Tag(name = "Rest Api Sample")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Create User
     */
    @PostMapping(path = "/api/customers")
    public ResponseEntity<CustomerDTO> save(@RequestBody CustomerEntity customer) {
        return customerService.save(customer);
    }

    /**
     * Get User
     */
    @GetMapping(path = "/api/customers")
    public ResponseEntity<?> findAll() {
        return customerService.findAll();
    }

    /**
     * Get Users: id
     */
    @GetMapping(path = "/api/customers/{customer_id}")
    public ResponseEntity<CustomerDTO> findId(@PathVariable("customer_id") String customer_id) {
        return customerService.findById(customer_id);

    }

    /**
     * Update User: id
     */

    /**
     * Delete User:id
     */

}
