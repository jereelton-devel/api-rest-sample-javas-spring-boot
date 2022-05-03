package com.apirestsample.app.controllers;

import net.minidev.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("${api.prefix}")
public class ApiDemoController {

    @PostMapping(path = "/transaction/start")
    public ResponseEntity<String> createCustomer() {
        return ResponseEntity.ok().body("1111-0000-2222-4444-55555-7777-8888");
    }

    @PostMapping(path = "/retrieve/attributes")
    public ResponseEntity<Map<?,?>> createCustomer(@RequestBody JSONObject data) {
        HashMap<String, String> sharedData = new HashMap<>();
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.appendField("field1", "123456");
        jsonResponse.appendField("field2", "Username Tester");
        HashMap<Object, Object> response = new HashMap<>();
        response.put("sharedData", sharedData);
        response.put("jsonResponse", jsonResponse);
        response.put("cardNumber", "123456789");
        return ResponseEntity.ok().body(response);
    }

}
