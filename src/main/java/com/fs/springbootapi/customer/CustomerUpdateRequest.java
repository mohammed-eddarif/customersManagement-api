package com.fs.springbootapi.customer;

public record CustomerUpdateRequest(
        String name,
        String email,
        Integer age
) {
}
