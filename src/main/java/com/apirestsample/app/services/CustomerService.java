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
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerEntity findUserByName(String username) {
        return customerRepository.findByUsername(username);
    }

    public ResponseEntity<?> save(CustomerEntity customer) {

        if (customer == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing body request");
        }

        if (findUserByName(customer.getName()) != null) {
            return ResponseEntity.status(HttpStatus.FOUND)
                .body("Customer ["+customer.getName()+"] already exists");
        }

        try {
            CustomerEntity customerSave = customerRepository.save(customer);
            return ResponseEntity.status(HttpStatus.CREATED).body(CustomerDTO.mapperCustomerDTO(customerSave));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Occurs an error not expected in the server");
        }
    }

    public ResponseEntity<?> findAll() {
        try {
            List<CustomerEntity> listAll = customerRepository.findAll();
            JSONObject customersDTO = CustomerDTO.mapperAllCustomerDTO(listAll);
            if (customersDTO.size() > 0) {
                return ResponseEntity.status(HttpStatus.OK).body(customersDTO);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customers not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Occurs an error not expected in the server");
        }
    }

    public ResponseEntity<?> findById(String customer_id) {
        try {
            Optional<CustomerEntity> customer = customerRepository.findById(customer_id);

            if (customer.stream().findAny().isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
            } else {
                return customerRepository.findById(customer_id)
                    .map(record -> ResponseEntity.ok().body(CustomerDTO.mapperCustomerDTO(record)))
                    .orElse(ResponseEntity.notFound().build());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Occurs an error not expected in the server");
        }
    }

    public ResponseEntity<?> updateCustomer(String customer_id, CustomerEntity customer_data) {

        if (customer_data == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing body request");
        }

        try {
            Optional<CustomerEntity> customer = customerRepository.findById(customer_id);

            if (customer.stream().findAny().isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
            } else {
                return customerRepository.findById(customer_id)
                    .map( record -> {
                        record.setName(customer_data.getName());
                        record.setActive(customer_data.getActive());
                        CustomerEntity updated = customerRepository.save(record);
                        return ResponseEntity.ok().body(CustomerDTO.mapperCustomerDTO(updated));
                    }).orElse(ResponseEntity.notFound().build());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Occurs an error not expected in the server");
        }
    }

    public ResponseEntity<?> deleteCustomer(String customer_id) {
        try {
            Optional<CustomerEntity> customer = customerRepository.findById(customer_id);

            if (customer.stream().findAny().isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
            } else {
                return customerRepository.findById(customer_id)
                        .map(record -> {
                            customerRepository.deleteById(customer_id);
                            return ResponseEntity.ok().body(CustomerDTO.mapperCustomerDTO(record));
                        }).orElse(ResponseEntity.notFound().build());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Occurs an error not expected in the server");
        }
    }

    public ResponseEntity<?> patchCustomer(String customer_id, CustomerEntity customer_patch) {

        if (customer_patch == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing body request");
        }

        try {
            Optional<CustomerEntity> customer = customerRepository.findById(customer_id);

            if (customer.stream().findAny().isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
            } else {
                return customerRepository.findById(customer_id)
                        .map( record -> {
                            if (customer_patch.getName() != null) record.setName(customer_patch.getName());
                            if (customer_patch.getActive() != null) record.setActive(customer_patch.getActive());
                            CustomerEntity patcher = customerRepository.save(record);
                            return ResponseEntity.ok().body(CustomerDTO.mapperCustomerDTO(patcher));
                        }).orElse(ResponseEntity.notFound().build());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Occurs an error not expected in the server");
        }
    }
}
