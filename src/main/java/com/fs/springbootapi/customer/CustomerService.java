package com.fs.springbootapi.customer;

import com.fs.springbootapi.exception.ResourceNotFound;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    final private CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(Integer customerId) {
        return Optional.ofNullable(customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFound("customer with id [%s] not found".formatted(customerId))));
    }
}
