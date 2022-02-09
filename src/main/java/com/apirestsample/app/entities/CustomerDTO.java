package com.apirestsample.app.entities;

import com.apirestsample.app.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.apirestsample.app.utils.Helpers.md5;

@AllArgsConstructor
@Getter
public class CustomerDTO {

    private Integer id;
    private String name;
    private String username;
    private List<JSONObject> devices;

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerDTO(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    private Integer getCustomerId(String username) {
        return this.customerRepository.getIdByUsername(username);
    }

    private static List<JSONObject> getDevices(CustomerEntity customer) {

        Random num = new Random();

        List<JSONObject> devices = new ArrayList<>();

        JSONObject deviceSms = new JSONObject();
        deviceSms.appendField("id", customer.getSmsDeviceId());
        String[] typeSms = new String[]{"sms"};
        deviceSms.appendField("capabilities", typeSms);
        deviceSms.appendField("confirmed_at", null);
        deviceSms.appendField("number", customer.getSms());
        deviceSms.appendField("token", md5(String.valueOf(num.nextInt(10000))));
        deviceSms.appendField("otp_activated", false);

        devices.add(deviceSms);

        JSONObject deviceMail = new JSONObject();
        deviceMail.appendField("id", customer.getMailDeviceId());
        String[] typeMail = new String[]{"mail"};
        deviceMail.appendField("capabilities", typeMail);
        deviceMail.appendField("confirmed_at", null);
        deviceMail.appendField("email", customer.getMail());

        devices.add(deviceMail);

        return devices;
    }

    public CustomerDTO mapperCustomerDTO(CustomerEntity customer) {
        return new CustomerDTO(
                getCustomerId(customer.getUsername()),
                customer.getName(),
                customer.getUsername(),
                getDevices(customer),
                null
        );
    }

    public JSONObject mapperAllCustomerDTO(List<CustomerEntity> customers) {
        JSONObject results = new JSONObject();
        int counter = 0;

        for (CustomerEntity customer : customers) {
            counter++;
            results.appendField(Integer.toString(counter), new CustomerDTO(
                    getCustomerId(customer.getUsername()),
                    customer.getName(),
                    customer.getUsername(),
                    getDevices(customer),
                    null
            ));
        }

        results.appendField("total", counter);

        return results;
    }

}
