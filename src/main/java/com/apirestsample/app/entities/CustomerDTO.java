package com.apirestsample.app.entities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minidev.json.JSONObject;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CustomerDTO {

    private String userid;
    private String username;
    private String activated;

    public static CustomerDTO mapperCustomerDTO(CustomerEntity customer) {
        return new CustomerDTO(
                customer.getId(),
                customer.getName(),
                customer.getActive()
        );
    }

    public static JSONObject mapperAllCustomerDTO(List<CustomerEntity> customers) {
        JSONObject results = new JSONObject();
        int counter = 0;

        for (CustomerEntity customer : customers) {
            counter++;
            results.appendField(Integer.toString(counter), new CustomerDTO(
                    customer.getId(),
                    customer.getName(),
                    customer.getActive()
            ));
        }

        results.appendField("total", counter);

        return results;
    }

}
