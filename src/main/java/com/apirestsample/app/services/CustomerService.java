package com.apirestsample.app.services;

import com.apirestsample.app.entities.CustomerDTO;
import com.apirestsample.app.entities.CustomerEntity;
import com.apirestsample.app.repositories.CustomerRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public ResponseEntity<CustomerDTO> save(CustomerEntity customer) {
        CustomerEntity customerSave = customerRepository.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(CustomerDTO.mapperCustomerDTO(customerSave));
    }

    public ResponseEntity<?> findAll() {
        List<CustomerEntity> listAll = customerRepository.findAll();
        JSONObject customersDTO = CustomerDTO.mapperAllCustomerDTO(listAll);
        return ResponseEntity.status(HttpStatus.OK).body(customersDTO);
    }

    public ResponseEntity<CustomerDTO> findById(String customer_id) {
        return customerRepository.findById(customer_id)
            .map(record -> ResponseEntity.ok().body(CustomerDTO.mapperCustomerDTO(record)))
            .orElse(ResponseEntity.notFound().build());
    }
}
