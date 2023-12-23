package com.fs.springbootapi.customer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DemoDataService {

    final private CustomerRepository customerRepository;

    public DemoDataService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public void insertDemoData() {
        Customer customer1 = new Customer("med", "edd", 23);
        Customer customer2 = new Customer("sanae", "daoudi", 25);
        Customer customer3 = new Customer("ayoub", "laghbissi", 26);

        List<Customer> customers = List.of(customer1, customer2, customer3);

        customerRepository.saveAll(customers);
    }
}
