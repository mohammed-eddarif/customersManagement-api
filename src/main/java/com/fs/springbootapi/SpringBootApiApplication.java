package com.fs.springbootapi;

import com.fs.springbootapi.customer.Customer;
import com.fs.springbootapi.customer.CustomerRepository;
import com.fs.springbootapi.customer.Gender;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;


@SpringBootApplication
@EntityScan(basePackages = "com.fs.springbootapi.customer")
public class SpringBootApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootApiApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(CustomerRepository customerRepository) {
		return args -> {
			var faker = new Faker();
			Random random = new Random();
			Name name = faker.name();
			String firstName = name.firstName();
			String lastName = name.lastName();
			int age = random.nextInt(16, 99);
			Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
			String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@gmail.com";
			Customer customer = new Customer(
					firstName +  " " + lastName,
					email,
					age,
					gender,
					"123"
			);
			customerRepository.save(customer);
			System.out.println(email);
		};
	}


}
