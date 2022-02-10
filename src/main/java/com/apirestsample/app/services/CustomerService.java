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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.apirestsample.app.utils.Helpers.md5;

@Service
public class CustomerService extends ResponseHandler {

    private final CustomerRepository customerRepository;
    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public static List<JSONObject> getDevicesFake() {

        Random num = new Random();

        List<JSONObject> devices = new ArrayList<>();

        JSONObject deviceSms = new JSONObject();
        deviceSms.appendField("id", num.nextInt(10000));
        String[] typeSms = new String[]{"sms"};
        deviceSms.appendField("capabilities", typeSms);
        deviceSms.appendField("confirmed_at", null);
        deviceSms.appendField("number", "55129988990099");
        deviceSms.appendField("token", md5(String.valueOf(num.nextInt(10000))));
        deviceSms.appendField("otp_activated", false);

        devices.add(deviceSms);

        JSONObject deviceMail = new JSONObject();
        deviceMail.appendField("id", num.nextInt(10000));
        String[] typeMail = new String[]{"mail"};
        deviceMail.appendField("capabilities", typeMail);
        deviceMail.appendField("confirmed_at", null);
        deviceMail.appendField("email", "email@email.com");

        devices.add(deviceMail);

        return devices;
    }

    public JSONObject getProcessFake() {

        Random num = new Random();
        String token = md5(String.valueOf(num.nextInt(100000)));

        JSONObject processFake = new JSONObject();
        processFake.appendField("token", token);
        processFake.appendField("created_at", "2087-07-25T02:48:40.815Z");
        processFake.appendField("updated_at", "2087-07-25T02:48:40.815Z");
        processFake.appendField("data", null);
        processFake.appendField("passcode_attempts_left", 3);
        processFake.appendField("auth_attempts_left", 3);
        processFake.appendField("authorized", false);

        JSONObject jsonUserFake = new JSONObject();
        jsonUserFake.appendField("id", num.nextInt(10000));
        jsonUserFake.appendField("name", "Username Fake");
        jsonUserFake.appendField("username", md5(String.valueOf(num.nextInt(100000))));
        jsonUserFake.appendField("devices", getDevicesFake());

        processFake.appendField("user", jsonUserFake);

        return processFake;

    }

    public Object getConfirmedDeviceFake() {

        JSONObject fakeConfirmed = new JSONObject();
        fakeConfirmed.appendField("id", "123456");
        fakeConfirmed.appendField("capabilities", "[sms-fake]");
        fakeConfirmed.appendField("confirmed_at", "2022-01-25T04:41:59.160Z");
        fakeConfirmed.appendField("number", "129988998890");
        fakeConfirmed.appendField("token", "166f9e1987d56340e12a");
        fakeConfirmed.appendField("otp_activated", false);

        return fakeConfirmed;

    }

    public CustomerEntity findUserByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

    public static Integer getEntityFields() {
        return CustomerEntity.class.getDeclaredFields().length;
    }

    public ResponseEntity<?> createCustomer(HttpServletRequest headers, JSONObject customer) {

        if (!AccessControlService.authorization(headers)) {
            return retrieveAnyResponse(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        if (customer == null) {
            return retrieveAnyResponse(HttpStatus.BAD_REQUEST, "Missing body request");
        }

        if (findUserByUsername(customer.getAsString("username")) != null) {
            return retrieveAnyResponse(HttpStatus.FOUND, "Customer [" + customer.getAsString("username") + "] already exists");
        }

        try {

            Random num = new Random();

            CustomerEntity customerSave = new CustomerEntity(
                    ""+customer.getAsString("username"),
                    ""+customer.getAsString("name"),
                    ""+customer.getAsString("sms"),
                    ""+num.nextInt(10000),
                    ""+customer.getAsString("mail"),
                    ""+num.nextInt(10000),
                    ""+customer.getAsString("active")
            );

            customerRepository.save(customerSave);

            CustomerDTO customerDTO = new CustomerDTO(this.customerRepository);

            return retrieveSimpleSuccessResponse(
                    HttpStatus.CREATED,
                    customerDTO.mapperCustomerDTO(customerSave)
            );

        } catch (RuntimeException e) {
            return retrieveInternalServerError(e);
        }
    }

    public ResponseEntity<?> readAllCustomers(HttpServletRequest headers) {

        if (!AccessControlService.authorization(headers)) {
            return retrieveAnyResponse(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        try {
            List<CustomerEntity> listAll = customerRepository.findAll();
            CustomerDTO customerDTO = new CustomerDTO(customerRepository);
            JSONObject customersDTO = customerDTO.mapperAllCustomerDTO(listAll);
            if (customersDTO.size() > 0) {
                return retrieveSuccessResponse(HttpStatus.OK, "Customers found successfully", customersDTO);
            } else {
                return retrieveAnyResponse(HttpStatus.NOT_FOUND, "Customers not found");
            }
        } catch (RuntimeException e) {
            return retrieveInternalServerError(e);
        }
    }

    public ResponseEntity<?> readOneCustomer(HttpServletRequest headers, String customer_id) {

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
                            CustomerDTO customerDTO = new CustomerDTO(this.customerRepository);
                            return retrieveSuccessResponse(
                                    HttpStatus.OK,
                                    "Customer found successfully",
                                    customerDTO.mapperCustomerDTO(record)
                            );
                        })
                        .orElse(ResponseEntity.notFound().build());
            }
        } catch (RuntimeException e) {
            return retrieveInternalServerError(e);
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
                            CustomerDTO customerDTO = new CustomerDTO(this.customerRepository);
                            return retrieveSuccessResponse(
                                    HttpStatus.OK,
                                    "Customer updated successfully",
                                    customerDTO.mapperCustomerDTO(updated)
                            );

                        }).orElse(ResponseEntity.notFound().build());
            }
        } catch (RuntimeException e) {
            return retrieveInternalServerError(e);
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
                            CustomerDTO customerDTO = new CustomerDTO(this.customerRepository);
                            return retrieveSuccessResponse(
                                    HttpStatus.OK,
                                    "Customer deleted successfully",
                                    customerDTO.mapperCustomerDTO(record)
                            );

                        }).orElse(ResponseEntity.notFound().build());
            }
        } catch (RuntimeException e) {
            return retrieveInternalServerError(e);
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
                            CustomerDTO customerDTO = new CustomerDTO(this.customerRepository);
                            return retrieveSuccessResponse(
                                    HttpStatus.OK,
                                    "Customer patched successfully",
                                    customerDTO.mapperCustomerDTO(patcher)
                            );

                        }).orElse(ResponseEntity.notFound().build());
            }
        } catch (RuntimeException e) {
            return retrieveInternalServerError(e);
        }
    }

    public ResponseEntity<?> requestReject() {
        return retrieveAnyResponse(HttpStatus.METHOD_NOT_ALLOWED, "Wrong request");
    }
}
