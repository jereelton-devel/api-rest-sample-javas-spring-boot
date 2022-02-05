package com.apirestsample.app.services;

import com.apirestsample.app.entities.CustomerDTO;
import com.apirestsample.app.entities.CustomerEntity;
import com.apirestsample.app.repositories.CustomerRepository;
import com.apirestsample.app.utils.ResponseHandler;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService extends ResponseHandler {

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
            return retrieveAnyResponse(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        if (customer == null) {
            return retrieveAnyResponse(HttpStatus.BAD_REQUEST, "Missing body request");
        }

        if (findUserByName(customer.getName()) != null) {
            return retrieveAnyResponse(HttpStatus.FOUND, "Customer [" + customer.getName() + "] already exists");
        }

        try {
            CustomerEntity customerSave = customerRepository.save(customer);
            return retrieveSuccessResponse(
                    HttpStatus.CREATED,
                    "Customer created successfully",
                    CustomerDTO.mapperCustomerDTO(customerSave)
            );
        } catch (Exception ex) {
            return retrieveInternalServerError();
        }
    }

    public ResponseEntity<?> findAll(HttpServletRequest headers) {

        if (!AccessControlService.authorization(headers)) {
            return retrieveAnyResponse(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        try {
            List<CustomerEntity> listAll = customerRepository.findAll();
            JSONObject customersDTO = CustomerDTO.mapperAllCustomerDTO(listAll);
            if (customersDTO.size() > 0) {
                return retrieveSuccessResponse(HttpStatus.OK, "Customers found successfully", customersDTO);
            } else {
                return retrieveAnyResponse(HttpStatus.NOT_FOUND, "Customers not found");
            }
        } catch (Exception e) {
            return retrieveInternalServerError();
        }
    }

    public ResponseEntity<?> findById(HttpServletRequest headers, String customer_id) {

        if (!AccessControlService.authorization(headers)) {
            return retrieveAnyResponse(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        try {
            Optional<CustomerEntity> customer = customerRepository.findById(customer_id);

            if (customer.stream().findAny().isEmpty()) {
                return retrieveAnyResponse(HttpStatus.NOT_FOUND, "Customer not found");
            } else {
                return customerRepository.findById(customer_id)
                        .map(record -> {
                            CustomerDTO customerDTO = CustomerDTO.mapperCustomerDTO(record);
                            return retrieveSuccessResponse(
                                    HttpStatus.OK,
                                    "Customer found successfully",
                                    customerDTO
                            );
                        })
                        .orElse(ResponseEntity.notFound().build());
            }
        } catch (Exception e) {
            return retrieveInternalServerError();
        }
    }

    public ResponseEntity<?> updateCustomer(HttpServletRequest headers, String customer_id, JSONObject customer_up) {

        if (!AccessControlService.authorization(headers)) {
            return retrieveAnyResponse(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        if (customer_up == null || customer_up.size() == 0) {
            return retrieveAnyResponse(HttpStatus.BAD_REQUEST, "Missing body request");
        }

        /*(getEntityFields()-1) - indicate that the data field [id] from Entity can be ignored*/
        if (!(customer_up.size() >= getEntityFields()) && !(customer_up.size() >= (getEntityFields() - 1))) {
            return retrieveAnyResponse(
                    HttpStatus.NOT_ACCEPTABLE,
                    "Update is not correct, because it should be total data update"
            );
        }

        try {
            Optional<CustomerEntity> customer = customerRepository.findById(customer_id);

            if (customer.stream().findAny().isEmpty()) {
                return retrieveAnyResponse(HttpStatus.NOT_FOUND, "Customer not found");
            } else {
                return customerRepository.findById(customer_id)
                        .map(record -> {

                            record.setName(customer_up.getAsString("name"));
                            record.setActive(customer_up.getAsString("active"));
                            CustomerEntity updated = customerRepository.save(record);
                            CustomerDTO customerDTO = CustomerDTO.mapperCustomerDTO(updated);
                            return retrieveSuccessResponse(
                                    HttpStatus.OK,
                                    "Customer updated successfully",
                                    customerDTO
                            );

                        }).orElse(ResponseEntity.notFound().build());
            }
        } catch (Exception e) {
            return retrieveInternalServerError();
        }
    }

    public ResponseEntity<?> deleteCustomer(HttpServletRequest headers, String customer_id) {

        if (!AccessControlService.authorization(headers)) {
            return retrieveAnyResponse(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        try {
            Optional<CustomerEntity> customer = customerRepository.findById(customer_id);

            if (customer.stream().findAny().isEmpty()) {
                return retrieveAnyResponse(HttpStatus.NOT_FOUND, "Customer not found");
            } else {
                return customerRepository.findById(customer_id)
                        .map(record -> {

                            customerRepository.deleteById(customer_id);
                            CustomerDTO customerDTO = CustomerDTO.mapperCustomerDTO(record);
                            return retrieveSuccessResponse(
                                    HttpStatus.OK,
                                    "Customer deleted successfully",
                                    customerDTO
                            );

                        }).orElse(ResponseEntity.notFound().build());
            }
        } catch (Exception e) {
            return retrieveInternalServerError();
        }
    }

    public ResponseEntity<?> patchCustomer(HttpServletRequest headers, String customer_id, JSONObject customer_patch) {

        if (!AccessControlService.authorization(headers)) {
            return retrieveAnyResponse(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        if (customer_patch == null || customer_patch.size() == 0) {
            return retrieveAnyResponse(HttpStatus.BAD_REQUEST, "Missing body request");
        }

        if (!(customer_patch.size() < getEntityFields())) {
            return retrieveAnyResponse(
                    HttpStatus.NOT_ACCEPTABLE,
                    "Patch is not correct, because it should be partial update"
            );
        }

        try {
            Optional<CustomerEntity> customer = customerRepository.findById(customer_id);

            if (customer.stream().findAny().isEmpty()) {
                return retrieveAnyResponse(HttpStatus.NOT_FOUND, "Customer not found");
            } else {
                return customerRepository.findById(customer_id)
                        .map(record -> {
                            boolean patched = false;
                            if (customer_patch.getAsString("name") != null) {
                                record.setName(customer_patch.getAsString("name"));
                                patched = true;
                            }
                            if (customer_patch.getAsString("active") != null) {
                                record.setActive(customer_patch.getAsString("active"));
                                patched = true;
                            }
                            /*force error*/
                            if (!patched) {
                                return retrieveAnyResponse(
                                        HttpStatus.BAD_REQUEST,
                                        "Patch Fail: Missing correct fields to patcher"
                                );
                            }

                            CustomerEntity patcher = customerRepository.save(record);
                            CustomerDTO customerDTO = CustomerDTO.mapperCustomerDTO(patcher);
                            return retrieveSuccessResponse(
                                    HttpStatus.OK,
                                    "Customer patched successfully",
                                    customerDTO
                            );

                        }).orElse(ResponseEntity.notFound().build());
            }
        } catch (Exception e) {
            return retrieveInternalServerError();
        }
    }

    public ResponseEntity<?> requestReject() {
        return retrieveAnyResponse(HttpStatus.METHOD_NOT_ALLOWED, "Wrong request");
    }

}
