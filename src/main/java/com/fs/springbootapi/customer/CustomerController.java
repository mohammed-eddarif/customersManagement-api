package com.fs.springbootapi.customer;

import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/customers")
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

    @GetMapping
    public List<Customer> getAllCustomers(){
        return customerService.getAllCustomers();
    }

    @GetMapping("{customerId}")
    public Optional<Customer> getCustomerById(@PathVariable("customerId") Integer customerId){
        return customerService.getCustomerById(customerId);
    }

    @PostMapping
    public void insertCustomer(@RequestBody CustomerRegistrationRequest customerRegistrationRequest){
        customerService.insertCustomer(customerRegistrationRequest);
    }
}
