package com.apirestsample.app.controllers;

import com.apirestsample.app.entities.CustomerDTO;
import com.apirestsample.app.services.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Tag(name = "Rest Api Sample")
@Schema(implementation = CustomerDTO.class)
@CrossOrigin(origins = "*")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Create Customer
     */
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
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "text")
            }),
            @ApiResponse(responseCode = "500", description = "Server error", content = {
                    @Content(mediaType = "text")
            })
    })
    @PostMapping(path = "/api/customers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCustomer(HttpServletRequest headers, @Valid @RequestBody(required = false) JSONObject customer) {
        System.out.println("CREATE USER: " + customer);
        return customerService.createCustomer(headers, customer);
    }

    /**
     * Create Process
     */
    @PostMapping(path = "/api/process", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createProcess(HttpServletRequest headers, @Valid @RequestBody(required = false) JSONObject process) {
        System.out.println("CREATE PROCESS: " + process);
        return ResponseEntity.status(HttpStatus.OK).body(this.customerService.getProcessFake());
    }

    /**
     * Send Passcode
     */
    @PostMapping(path = "/api/process/{token}/send_passcode", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sendPasscode(HttpServletRequest headers, @PathVariable("token") String token, @Valid @RequestBody(required = false) JSONObject passcode) {
        System.out.println("SEND PASSCODE: " + passcode);
        System.out.println("TOKEN PASSCODE: " + token);
        return ResponseEntity.status(HttpStatus.OK).body(this.customerService.getProcessFake());
    }

    /**
     * Confirm Device
     */
    @PostMapping(path = "/api/customers/{userid}/devices/{device_id}/confirm", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> confirmDevice(
            HttpServletRequest headers,
            @PathVariable("userid") String userid,
            @PathVariable("device_id") String device_id,
            @Valid @RequestBody(required = false) JSONObject data
    ) {
        System.out.println("CONFIRM DEVICE------------------------------------------------------------");
        System.out.println("USERID: " + userid);
        System.out.println("DEVICE: " + device_id);
        System.out.println("DATA: " + data);
        return ResponseEntity.status(HttpStatus.OK).body(this.customerService.getConfirmedDeviceFake());
    }

    /**
     * Read All Customers
     */
    @Operation(summary = "Get all customers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK, read all customers", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerDTO.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "text")
            }),
            @ApiResponse(responseCode = "404", description = "Customers not found", content = {
                    @Content(mediaType = "text")
            }),
            @ApiResponse(responseCode = "500", description = "Server error", content = {
                    @Content(mediaType = "text")
            })
    })
    @GetMapping(path = "/api/customers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> readAllCustomers(HttpServletRequest headers) {
        return customerService.readAllCustomers(headers);
    }

    /**
     * Read Customer
     */
    @Operation(summary = "Get customer by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK, read one customer", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerDTO.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "text")
            }),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = {
                    @Content(mediaType = "text")
            }),
            @ApiResponse(responseCode = "500", description = "Server error", content = {
                    @Content(mediaType = "text")
            })
    })
    @GetMapping(path = "/api/customers/{customer_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> readOneCustomer(HttpServletRequest headers, @PathVariable("customer_id") String customer_id) {
        return customerService.readOneCustomer(headers, customer_id);
    }

    /**
     * Update Customer
     */
    @Operation(summary = "Update an customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK, Customer updated", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Missing body request", content = {
                    @Content(mediaType = "text")
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "text")
            }),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = {
                    @Content(mediaType = "text")
            }),
            @ApiResponse(responseCode = "406", description = "Update is not correct, because it should be total data update", content = {
                    @Content(mediaType = "text")
            }),
            @ApiResponse(responseCode = "500", description = "Server error", content = {
                    @Content(mediaType = "text")
            })
    })
    @PutMapping (value = "/api/customers/{customer_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(
            HttpServletRequest headers,
            @PathVariable("customer_id") String customer_id,
            @Valid @RequestBody(required = false) JSONObject customer_data
    ) {
        return customerService.updateCustomer(headers, customer_id, customer_data);
    }

    /**
     * Delete Customer
     */
    @Operation(summary = "Delete an customer by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK, customer deleted", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerDTO.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "text")
            }),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = {
                    @Content(mediaType = "text")
            }),
            @ApiResponse(responseCode = "500", description = "Server error", content = {
                    @Content(mediaType = "text")
            })
    })
    @DeleteMapping (path = "/api/customers/{customer_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(HttpServletRequest headers, @PathVariable("customer_id") String customer_id) {
        return customerService.deleteCustomer(headers, customer_id);
    }

    /**
     * Patch Customer
     */
    @Operation(summary = "Fix/Patch an customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK, Customer patched", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Missing body request", content = {
                    @Content(mediaType = "text")
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "text")
            }),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = {
                    @Content(mediaType = "text")
            }),
            @ApiResponse(responseCode = "406", description = "Patch is not correct, because it should be partial update", content = {
                    @Content(mediaType = "text")
            }),
            @ApiResponse(responseCode = "500", description = "Server error", content = {
                    @Content(mediaType = "text")
            })
    })
    @PatchMapping (path = "/api/customers/{customer_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> patch(
            HttpServletRequest headers,
            @PathVariable("customer_id") String customer_id,
            @Valid @RequestBody(required = false) JSONObject customer_data
    ) {
        return customerService.patchCustomer(headers, customer_id, customer_data);
    }

    /**
     * Reject Request (not POST|GET)
     */
    @Operation(summary = "Reject the Wrong request - not POST|GET")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
            @Content(mediaType = "text")
        }),
        @ApiResponse(responseCode = "405", description = "Wrong Request", content = {
            @Content(mediaType = "text")
        })
    })
    @RequestMapping(
            value = {"/", "/api", "/api/customers"},
            method = {
                    RequestMethod.PUT,
                    RequestMethod.PATCH,
                    RequestMethod.DELETE,
                    RequestMethod.HEAD,
                    RequestMethod.OPTIONS
            }
    )
    public ResponseEntity<?> reject() {
        return customerService.requestReject();
    }

    /**
     * Reject Request (only POST|GET)
     */
    @Operation(summary = "Reject the Wrong request to POST|GET")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                @Content(mediaType = "text")
        }),
        @ApiResponse(responseCode = "405", description = "Wrong Request POST|GET", content = {
            @Content(mediaType = "text")
        })
    })
    @RequestMapping(
            value = {"/", "/api"},
            method = {
                    RequestMethod.POST,
                    RequestMethod.GET
            }
    )
    public ResponseEntity<?> rejectGet() {
        return customerService.requestReject();
    }

}
