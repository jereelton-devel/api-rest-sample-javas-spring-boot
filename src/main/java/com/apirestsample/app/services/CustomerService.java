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
    private final JSONObject jsonResponse;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.jsonResponse = new JSONObject();
    }

    public ResponseEntity<?> retrieveInternalServerError() {
        setResponseError("Occurs an error not expected in the server");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonResponse);
    }

    public void setResponseError(String message) {
        jsonResponse.appendField("status", "error");
        jsonResponse.appendField("message", message);
    }

    public void setResponseSuccess(String message, Object data) {
        jsonResponse.appendField("status", "success");
        jsonResponse.appendField("message", message);
        jsonResponse.appendField("data", data);
    }

    public CustomerEntity findUserByName(String username) {
        return customerRepository.findByUsername(username);
    }

    public static Integer getEntityFields() {
        return CustomerEntity.class.getDeclaredFields().length;
    }

    public ResponseEntity<?> save(HttpServletRequest headers, CustomerEntity customer) {

        if (!AccessControlService.authorization(headers)) {
            setResponseError("Unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(jsonResponse);
        }

        if (customer == null) {
            setResponseError("Missing body request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse);
        }

        if (findUserByName(customer.getName()) != null) {
            setResponseError("Customer [" + customer.getName() + "] already exists");
            return ResponseEntity.status(HttpStatus.FOUND).body(jsonResponse);
        }

        try {
            CustomerEntity customerSave = customerRepository.save(customer);
            setResponseSuccess("Customer created successfully", CustomerDTO.mapperCustomerDTO(customerSave));
            return ResponseEntity.status(HttpStatus.CREATED).body(jsonResponse);
        } catch (Exception ex) {
            return retrieveInternalServerError();
        }
    }

    public ResponseEntity<?> findAll(HttpServletRequest headers) {

        if (!AccessControlService.authorization(headers)) {
            setResponseError("Unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(jsonResponse);
        }

        try {
            List<CustomerEntity> listAll = customerRepository.findAll();
            JSONObject customersDTO = CustomerDTO.mapperAllCustomerDTO(listAll);
            if (customersDTO.size() > 0) {
                setResponseSuccess("Customers found successfully", customersDTO);
                return ResponseEntity.status(HttpStatus.OK).body(jsonResponse);
            } else {
                setResponseError("Customers not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponse);
            }
        } catch (Exception e) {
            return retrieveInternalServerError();
        }
    }

    public ResponseEntity<?> findById(HttpServletRequest headers, String customer_id) {

        if (!AccessControlService.authorization(headers)) {
            setResponseError("Unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(jsonResponse);
        }

        try {
            Optional<CustomerEntity> customer = customerRepository.findById(customer_id);

            if (customer.stream().findAny().isEmpty()) {
                setResponseError("Customer not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponse);
            } else {
                return customerRepository.findById(customer_id)
                        .map(record -> {
                            setResponseSuccess("Customer found successfully", CustomerDTO.mapperCustomerDTO(record));
                            return ResponseEntity.status(HttpStatus.OK).body(jsonResponse);
                        })
                        .orElse(ResponseEntity.notFound().build());
            }
        } catch (Exception e) {
            return retrieveInternalServerError();
        }
    }

    public ResponseEntity<?> updateCustomer(HttpServletRequest headers, String customer_id, JSONObject customer_up) {

        if (!AccessControlService.authorization(headers)) {
            setResponseError("Unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(jsonResponse);
        }

        if (customer_up == null || customer_up.size() == 0) {
            setResponseError("Missing body request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse);
        }

        /*(getEntityFields()-1) - indicate that the data field [id] from Entity can be ignored*/
        if (!(customer_up.size() >= getEntityFields()) && !(customer_up.size() >= (getEntityFields() - 1))) {
            setResponseError("Update is not correct, because it should be total data update");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(jsonResponse);
        }

        try {
            Optional<CustomerEntity> customer = customerRepository.findById(customer_id);

            if (customer.stream().findAny().isEmpty()) {
                setResponseError("Customer not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponse);
            } else {
                return customerRepository.findById(customer_id)
                        .map(record -> {

                            record.setName(customer_up.getAsString("name"));
                            record.setActive(customer_up.getAsString("active"));
                            CustomerEntity updated = customerRepository.save(record);
                            setResponseSuccess("Customer updated successfully", CustomerDTO.mapperCustomerDTO(updated));
                            return ResponseEntity.status(HttpStatus.OK).body(jsonResponse);

                        }).orElse(ResponseEntity.notFound().build());
            }
        } catch (Exception e) {
            return retrieveInternalServerError();
        }
    }

    public ResponseEntity<?> deleteCustomer(HttpServletRequest headers, String customer_id) {

        if (!AccessControlService.authorization(headers)) {
            setResponseError("Unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(jsonResponse);
        }

        try {
            Optional<CustomerEntity> customer = customerRepository.findById(customer_id);

            if (customer.stream().findAny().isEmpty()) {
                setResponseError("Customer not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponse);
            } else {
                return customerRepository.findById(customer_id)
                        .map(record -> {

                            customerRepository.deleteById(customer_id);
                            setResponseSuccess("Customer deleted successfully", CustomerDTO.mapperCustomerDTO(record));
                            return ResponseEntity.status(HttpStatus.OK).body(jsonResponse);

                        }).orElse(ResponseEntity.notFound().build());
            }
        } catch (Exception e) {
            return retrieveInternalServerError();
        }
    }

    public ResponseEntity<?> patchCustomer(HttpServletRequest headers, String customer_id, JSONObject customer_patch) {

        if (!AccessControlService.authorization(headers)) {
            setResponseError("Unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(jsonResponse);
        }

        if (customer_patch == null || customer_patch.size() == 0) {
            setResponseError("Missing body request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse);
        }

        if (!(customer_patch.size() < getEntityFields())) {
            setResponseError("Patch is not correct, because it should be partial update");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(jsonResponse);
        }

        try {
            Optional<CustomerEntity> customer = customerRepository.findById(customer_id);

            if (customer.stream().findAny().isEmpty()) {
                setResponseError("Customer not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponse);
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
                                setResponseError("Patch Fail: Missing correct fields to patcher");
                                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonResponse);
                            }

                            CustomerEntity patcher = customerRepository.save(record);
                            setResponseSuccess("Customer patched successfully", CustomerDTO.mapperCustomerDTO(patcher));
                            return ResponseEntity.ok().body(jsonResponse);

                        }).orElse(ResponseEntity.notFound().build());
            }
        } catch (Exception e) {
            return retrieveInternalServerError();
        }
    }

    public ResponseEntity<?> requestReject() {
        setResponseError("Wrong request");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(jsonResponse);
    }

}
