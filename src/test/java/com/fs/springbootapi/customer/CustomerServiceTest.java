package com.fs.springbootapi.customer;

import com.fs.springbootapi.exception.DuplicateResourceException;
import com.fs.springbootapi.exception.ResourceNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    private CustomerService underTest;
    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerRepository);
    }

    @Test
    void getAllCustomers() {
        underTest.getAllCustomers();

        verify(customerRepository).findAll();
    }

    @Test
    void getCustomerById() {
        int id = 1;
        Customer customer = new Customer(id, "med", "edd", 23);
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        Customer actual = underTest.getCustomerById(id);

        assertThat(actual).isEqualTo(customer);
    }



    @Test
    void willThrowWhenGetCustomerReturnEmptyOptional() {
        // Given
        int id = 1;

        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> underTest.getCustomerById(id))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessage("Customer with id [%s] not found".formatted(id));
    }

    @Test
    void insertCustomer() {
        // Given
        String email = "edd";

        when(customerRepository.existsCustomerByEmail(email)).thenReturn(false);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest("med", email,  19);


        // When
        underTest.insertCustomer(request);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerRepository).save(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
    }
    @Test
    void willThrowWhenEmailExistsWhileAddingACustomer() {
        // Given
        String email = "edd";

        when(customerRepository.existsCustomerByEmail(email)).thenReturn(true);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest("med", email, 19);

        // When
        assertThatThrownBy(() -> underTest.insertCustomer(request)).isInstanceOf(DuplicateResourceException.class).hasMessage("email already taken");

        // Then
        verify(customerRepository, never()).save(any());
    }

    @Test
    void deleteCustomerById() {
        // Given
        int id = 10;

        when(customerRepository.existsById(id)).thenReturn(true);

        // When
        underTest.deleteCustomerById(id);
        // Then
        verify(customerRepository).deleteById(id);
    }

//    @Test
//    void willThrowDeleteCustomerByIdNotExists() {
//        // Given
//        int id = 1;
//
//        when(customerRepository.existsById(id)).thenReturn(false);
//
//        // When
//        assertThatThrownBy(() -> underTest.deleteCustomerById(id))
//                .isInstanceOf(ResourceNotFound.class)
//                .hasMessage(String.format("customer with id [%d] not found", id));
//
//        // Then
//        verify(customerRepository, never()).deleteById(id);
//    }


    @Test
    void canUpdateAllCustomersProperties() {
        // Given
        int id = 1;
        Customer customer = new Customer(id, "med", "edd", 19);
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        String newEmail = "med@gmail.com";

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("Alexandro", newEmail, 23);

        when(customerRepository.existsCustomerByEmail(newEmail)).thenReturn(false);

        // When
        underTest.editCustomerById(id, updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerRepository).save(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
    }

    @Test
    void canUpdateOnlyCustomerName() {
        // Given
        int id = 1;
        Customer customer = new Customer(id, "med", "edd", 23);
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("Alexandro", null, null);

        // When
        underTest.editCustomerById(id, updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerRepository).save(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
    }

    @Test
    void canUpdateOnlyCustomerEmail() {
        // Given
        int id = 1;
        Customer customer = new Customer(id, "med", "edd", 23);
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        String newEmail = "alexandro@amigoscode.com";

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(null, newEmail, null);

        when(customerRepository.existsCustomerByEmail(newEmail)).thenReturn(false);

        // When
        underTest.editCustomerById(id, updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerRepository).save(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
        assertThat(capturedCustomer.getEmail()).isEqualTo(newEmail);
    }

    @Test
    void canUpdateOnlyCustomerAge() {
        // Given
        int id = 1;
        Customer customer = new Customer(id, "med", "edd", 23);
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(null, null, 22);

        // When
        underTest.editCustomerById(id, updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerRepository).save(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
    }

    @Test
    void willThrowWhenTryingToUpdateCustomerEmailWhenAlreadyTaken() {
        // Given
        int id = 1;
        Customer customer = new Customer(id, "med", "edd", 23);
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        String newEmail = "alexandro@amigoscode.com";

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(null, newEmail, null);

        when(customerRepository.existsCustomerByEmail(newEmail)).thenReturn(true);

        // When
        assertThatThrownBy(() -> underTest.editCustomerById(id, updateRequest)).isInstanceOf(DuplicateResourceException.class).hasMessage("email already taken");

        // Then
        verify(customerRepository, never()).save(any());
    }

    @Test
    void willThrowWhenCustomerUpdateHasNoChanges() {
        // Given
        int id = 1;
        Customer customer = new Customer(id, "med", "edd", 23);
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(customer.getName(), customer.getEmail(), customer.getAge());

        // When
        assertThatThrownBy(() -> underTest.editCustomerById(id, updateRequest)).isInstanceOf(RequestValidationException.class).hasMessage("no data to change");

        // Then
        verify(customerRepository, never()).save(any());
    }




    @Test
    void editCustomerById() {
    }
}