package com.org.sunbasedata.SunbaseAssisgnment.Controller;

import com.org.sunbasedata.SunbaseAssisgnment.Model.Customer;
import com.org.sunbasedata.SunbaseAssisgnment.Model.CustomerRequest;
import com.org.sunbasedata.SunbaseAssisgnment.Model.LoginRequest;
import com.org.sunbasedata.SunbaseAssisgnment.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
//
//        String authToken = customerService.login(loginRequest);
//        return new ResponseEntity<>(authToken, HttpStatus.OK);
//    }
@PostMapping("/login")
public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {

    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setLoginId(username);
    loginRequest.setPassword(password);


    String authToken = customerService.login(loginRequest);

    if (authToken != null) {
        return new ResponseEntity<>(authToken, HttpStatus.OK);
    } else {
        return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
    }
}

    @PostMapping("/customers")
    public ResponseEntity<String> createCustomer(@RequestHeader("Authorization") String authToken, @RequestBody CustomerRequest customerRequest) {
        // Create a new customer
        boolean isCreated = customerService.createCustomer(authToken, customerRequest);
        if (isCreated) {
            return new ResponseEntity<>("Successfully Created", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("First Name or Last Name is missing", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getCustomers(@RequestHeader("Authorization") String authToken) {
        // Get a list of customers
        List<Customer> customers = customerService.getCustomers(authToken);
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @PostMapping("/customers/{uuid}/delete")
    public ResponseEntity<String> deleteCustomer(@RequestHeader("Authorization") String authToken, @PathVariable("uuid") String uuid) {
        // Delete a customer
        boolean isDeleted = customerService.deleteCustomer(authToken, uuid);
        if (isDeleted) {
            return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("UUID not found", HttpStatus.BAD_REQUEST);
        }
    }



    @PostMapping("/customers/{uuid}/update")
    public ResponseEntity<String> updateCustomer(@RequestHeader("Authorization") String authToken, @PathVariable("uuid") String uuid, @RequestBody CustomerRequest customerRequest) {
        // Update a customer
        boolean isUpdated = customerService.updateCustomer(authToken, uuid, customerRequest);
        if (isUpdated) {
            return new ResponseEntity<>("Successfully updated", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("UUID not found or body is empty", HttpStatus.BAD_REQUEST);
        }
    }


}

