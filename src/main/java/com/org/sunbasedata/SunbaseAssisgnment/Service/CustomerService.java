package com.org.sunbasedata.SunbaseAssisgnment.Service;

import com.org.sunbasedata.SunbaseAssisgnment.Model.Customer;
import com.org.sunbasedata.SunbaseAssisgnment.Model.CustomerRequest;
import com.org.sunbasedata.SunbaseAssisgnment.Model.LoginRequest;
import com.org.sunbasedata.SunbaseAssisgnment.Model.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class CustomerService {

    @Value("${customer.api.url}")
    private String apiUrl;

    @Value("${authentication.api.url}")
    private String authenticationApiUrl;

    private String bearerToken = null;

    @Autowired
     private final RestTemplate restTemplate;



    public CustomerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String login(LoginRequest loginRequest) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LoginRequest> requestEntity = new HttpEntity<>(loginRequest, headers);

        ResponseEntity<LoginResponse> response = restTemplate.exchange(authenticationApiUrl, HttpMethod.POST, requestEntity, LoginResponse.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            LoginResponse loginResponse = response.getBody();
            if (loginResponse != null) {
                bearerToken = loginResponse.getToken();
                return bearerToken;
            }
        }

        return null; // Invalid login
    }


//    public ResponseEntity<String> createCustomer(CustomerRequest customerRequest) {
//        // Send POST request to create a new customer
//        if (bearerToken == null) {
//            // You should handle the case where authentication hasn't been performed yet.
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required");
//        }
//
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Authorization", "Bearer " + bearerToken);
//
//        HttpEntity<CustomerRequest> requestEntity = new HttpEntity<>(customerRequest, headers);
//
//        return restTemplate.exchange(apiUrl + "?cmd=create", HttpMethod.POST, requestEntity, String.class);
//    }

    public boolean createCustomer(String authToken, CustomerRequest customerRequest) {

        if (authToken == null) {
            return false; // Authentication required
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + authToken);

        HttpEntity<CustomerRequest> requestEntity = new HttpEntity<>(customerRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl + "?cmd=create", HttpMethod.POST, requestEntity, String.class);

        return response.getStatusCode() == HttpStatus.CREATED;
    }



    public List<Customer> getCustomers(String authToken) {

        if (authToken == null) {
            return Collections.emptyList(); // Authentication required
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + authToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<Customer>> response = restTemplate.exchange(apiUrl + "?cmd=get_customer_list", HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<Customer>>() {});

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            return Collections.emptyList(); // Handle the case when the request is not successful
        }
    }


    public boolean deleteCustomer(String authToken, String uuid) {

        if (authToken == null) {
            return false; // Authentication required
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + authToken);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl + "?cmd=delete&uuid=" + uuid, HttpMethod.POST, null, String.class);

        return response.getStatusCode() == HttpStatus.OK;
    }


    public boolean updateCustomer(String authToken, String uuid, CustomerRequest customerRequest) {

        if (authToken == null) {
            return false; // Authentication required
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + authToken);

        HttpEntity<CustomerRequest> requestEntity = new HttpEntity<>(customerRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl + "?cmd=update&uuid=" + uuid, HttpMethod.POST, requestEntity, String.class);

        return response.getStatusCode() == HttpStatus.OK;
    }



}
