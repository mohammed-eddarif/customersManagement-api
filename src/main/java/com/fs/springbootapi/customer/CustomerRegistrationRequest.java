package com.fs.springbootapi.customer;


public record CustomerRegistrationRequest (
        String name,
        String email,
        Integer age){

}
