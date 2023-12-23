package com.fs.springbootapi.customer;



public record CustomerDTO(
        Integer id,
        String name,
        String email,
        Integer age

) {

}
