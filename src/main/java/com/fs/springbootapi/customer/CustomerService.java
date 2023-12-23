package com.fs.springbootapi.customer;

import com.fs.springbootapi.exception.DuplicateResourceException;
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

    public void insertCustomer(CustomerRegistrationRequest customerRegitrationRequest) {

        // check if customer already exist
       // boolean b = customerRepository.findAll().stream().anyMatch(customer1 -> customer1.equals(customerRegitrationRequest.email()));
        if (customerRepository.existsCustomerByEmail(customerRegitrationRequest.email())){
            throw new DuplicateResourceException("email already taken");
        }
        // add
        Customer customer = new Customer(customerRegitrationRequest.name(),
                customerRegitrationRequest.email(),
                customerRegitrationRequest.age());
        customerRepository.save(customer);
    }

    public void deleteCustomerById(Integer customerId) {
        if(!customerRepository.existsById(customerId)){
            throw new ResourceNotFound("customer with id [%s] not found");
        }
        customerRepository.deleteById(customerId);
    }
}
