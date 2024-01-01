package com.fs.springbootapi.customer;

import com.fs.springbootapi.exception.DuplicateResourceException;
import com.fs.springbootapi.exception.RequestValidationException;
import com.fs.springbootapi.exception.ResourceNotFound;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    final private CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Integer customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFound("Customer with id [%s] not found".formatted(customerId)));
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
                customerRegitrationRequest.age(),
                customerRegitrationRequest.gender(),
                customerRegitrationRequest.password());
        customerRepository.save(customer);
    }

    public void deleteCustomerById(Integer customerId) {
        if(!customerRepository.existsById(customerId)){
            throw new ResourceNotFound("customer with id [%s] not found");
        }
        customerRepository.deleteById(customerId);
    }

    public void editCustomerById(Integer customerId, CustomerUpdateRequest customerUpdateRequest) {
        Customer customer = this.getCustomerById(customerId);
        boolean changes = false;

        if(customerUpdateRequest.name() != null && !customerUpdateRequest.name().equals(customer.getName())){
            customer.setName(customerUpdateRequest.name());
            changes = true;
        }
        if(customerUpdateRequest.email() != null && !customerUpdateRequest.email().equals(customer.getEmail())){
            if (customerRepository.existsCustomerByEmail(customerUpdateRequest.email())){
                throw new DuplicateResourceException("email already taken");
            }
            customer.setEmail(customerUpdateRequest.email());
            changes = true;
        }
        if(customerUpdateRequest.age() != null && !customerUpdateRequest.age().equals(customer.getAge())){
            customer.setAge(customerUpdateRequest.age());
            changes = true;
        }
        if (!changes){
            throw new RequestValidationException("no data to change");
        }

        customerRepository.save(customer);
    }
}
