package com.apirestsample.app.services;

import com.apirestsample.app.entities.CustomerDTO;
import com.apirestsample.app.entities.CustomerEntity;
import com.apirestsample.app.repositories.CustomerRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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

    public static Integer getEntityFields() {
        return CustomerEntity.class.getDeclaredFields().length;
    }

    public ResponseEntity<?> save(HttpServletRequest headers, CustomerEntity customer) {

        if (!AccessControlService.authorization(headers)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

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

    public ResponseEntity<?> findAll(HttpServletRequest headers) {

        if (!AccessControlService.authorization(headers)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

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

    public ResponseEntity<?> findById(HttpServletRequest headers, String customer_id) {

        if (!AccessControlService.authorization(headers)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

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

    public ResponseEntity<?> updateCustomer(HttpServletRequest headers, String customer_id, JSONObject customer_up) {

        if (!AccessControlService.authorization(headers)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        if (customer_up == null || customer_up.size() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing body request");
        }

        /*(getEntityFields()-1) - indicate that the data field [id] from Entity can be ignored*/
        if (!(customer_up.size() >= getEntityFields()) && !(customer_up.size() >= (getEntityFields()-1))) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Update is not correct, because it should be total data update");
        }

        try {
            Optional<CustomerEntity> customer = customerRepository.findById(customer_id);

            if (customer.stream().findAny().isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
            } else {
                return customerRepository.findById(customer_id)
                    .map( record -> {
                        record.setName(customer_up.getAsString("name"));
                        record.setActive(customer_up.getAsString("active"));
                        CustomerEntity updated = customerRepository.save(record);
                        return ResponseEntity.ok().body(CustomerDTO.mapperCustomerDTO(updated));
                    }).orElse(ResponseEntity.notFound().build());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Occurs an error not expected in the server");
        }
    }

    public ResponseEntity<?> deleteCustomer(HttpServletRequest headers, String customer_id) {

        if (!AccessControlService.authorization(headers)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

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

    public ResponseEntity<?> patchCustomer(HttpServletRequest headers, String customer_id, JSONObject customer_patch) {

        if (!AccessControlService.authorization(headers)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        if (customer_patch == null || customer_patch.size() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing body request");
        }

        if (!(customer_patch.size() < getEntityFields())) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Patch is not correct, because it should be partial update");
        }

        try {
            Optional<CustomerEntity> customer = customerRepository.findById(customer_id);

            if (customer.stream().findAny().isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
            } else {
                return customerRepository.findById(customer_id)
                    .map( record -> {
                        if (customer_patch.getAsString("name") != null) record.setName(customer_patch.getAsString("name"));
                        if (customer_patch.getAsString("active") != null) record.setActive(customer_patch.getAsString("active"));
                        CustomerEntity patcher = customerRepository.save(record);
                        return ResponseEntity.ok().body(CustomerDTO.mapperCustomerDTO(patcher));
                    }).orElse(ResponseEntity.notFound().build());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Occurs an error not expected in the server");
        }
    }

    public ResponseEntity<?> requestReject() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("Wrong request");
    }

}
