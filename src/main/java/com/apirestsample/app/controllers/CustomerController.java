package com.apirestsample.app.controllers;

import com.apirestsample.app.entities.CustomerDTO;
import com.apirestsample.app.entities.CustomerEntity;
import com.apirestsample.app.services.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@Tag(name = "Rest Api Sample")
@Schema(implementation = CustomerDTO.class)
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Add new customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer created successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerDTO.class))
            }),
            @ApiResponse(responseCode = "302", description = "Customer name already exists", content = {
                    @Content(mediaType = "text")
            }),
            @ApiResponse(responseCode = "400", description = "Missing body request", content = {
                    @Content(mediaType = "text")
            }),
            @ApiResponse(responseCode = "500", description = "Server error", content = {
                    @Content(mediaType = "text")
            })
    })
    @PostMapping(path = "/api/customers")
    public ResponseEntity<?> create(@Valid @RequestBody(required = false) CustomerEntity customer) {
        return customerService.save(customer);
    }

    @Operation(summary = "Get all customers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK, read all customers", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Customers not found", content = {
                    @Content(mediaType = "text")
            }),
            @ApiResponse(responseCode = "500", description = "Server error", content = {
                    @Content(mediaType = "text")
            })
    })
    @GetMapping(path = "/api/customers")
    public ResponseEntity<?> readAll() {
        return customerService.findAll();
    }

    @Operation(summary = "Get customer by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK, read one customer", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = {
                    @Content(mediaType = "text")
            }),
            @ApiResponse(responseCode = "500", description = "Server error", content = {
                    @Content(mediaType = "text")
            })
    })
    @GetMapping(path = "/api/customers/{customer_id}")
    public ResponseEntity<?> read(@PathVariable("customer_id") String customer_id) {
        return customerService.findById(customer_id);
    }

    @Operation(summary = "Update an customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK, Customer updated", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Missing body request", content = {
                    @Content(mediaType = "text")
            }),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = {
                    @Content(mediaType = "text")
            }),
            @ApiResponse(responseCode = "500", description = "Server error", content = {
                    @Content(mediaType = "text")
            })
    })
    @PutMapping (value = "/api/customers/{customer_id}")
    public ResponseEntity<?> update(@PathVariable("customer_id") String customer_id, @RequestBody CustomerEntity customer_data) {
        return customerService.updateCustomer(customer_id, customer_data);
    }

    @Operation(summary = "Delete an customer by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK, customer deleted", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = {
                    @Content(mediaType = "text")
            }),
            @ApiResponse(responseCode = "500", description = "Server error", content = {
                    @Content(mediaType = "text")
            })
    })
    @DeleteMapping (path = "/api/customers/{customer_id}")
    public ResponseEntity<?> delete(@PathVariable("customer_id") String customer_id) {
        return customerService.deleteCustomer(customer_id);
    }

    @Operation(summary = "Fix/Patch an customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK, Customer patched", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Missing body request", content = {
                    @Content(mediaType = "text")
            }),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = {
                    @Content(mediaType = "text")
            }),
            @ApiResponse(responseCode = "500", description = "Server error", content = {
                    @Content(mediaType = "text")
            })
    })
    @PatchMapping (path = "/api/customers/{customer_id}")
    public ResponseEntity<?> fix(@PathVariable("customer_id") String customer_id, @RequestBody CustomerEntity customer_data) {
        return customerService.patchCustomer(customer_id, customer_data);
    }

}
