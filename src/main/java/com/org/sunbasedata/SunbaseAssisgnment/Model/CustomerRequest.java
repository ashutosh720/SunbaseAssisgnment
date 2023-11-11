package com.org.sunbasedata.SunbaseAssisgnment.Model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {

        private String firstName;
        private String lastName;
        private String street;
        private String address;
        private String city;
        private String state;
        private String email;
        private String phone;

        // getters and setters

}
