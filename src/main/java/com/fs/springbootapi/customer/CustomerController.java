package com.fs.springbootapi.customer;

import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class CustomerController {

    final private CustomerService customerService;
    final private DemoDataService demoDataService;

    public CustomerController(CustomerService customerService, DemoDataService demoDataService) {
        this.customerService = customerService;
        this.demoDataService = demoDataService;
    }

    @PostConstruct
    public void initializeDemoData() {
        demoDataService.insertDemoData();
    }

    @GetMapping("api/v1/customers")
    public List<Customer> getAllCustomers(){
        return customerService.getAllCustomers();
    }

    @GetMapping("api/v1/customer/{customerId}")
    public Optional<Customer> getCustomerById(@PathVariable("customerId") Integer customerId){
        return customerService.getCustomerById(customerId);
    }
}
