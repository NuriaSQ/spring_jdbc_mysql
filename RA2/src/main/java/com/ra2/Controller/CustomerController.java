package com.ra2.Controller;

import com.ra2.Model.Customer;
import com.ra2.Repository.CustomerRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    //ENDPOINT per poder afegir alumnes
    @PostMapping("/addUsers")
    public String addUsers() {
        return customerRepository.insertCustomers();
    }

    //ENDPOINT per poder mostrar per pantalla la llista de tots els alumnes de la base de dades
    @GetMapping("/showUsers")
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    //ENDPOINT per mostrar un alumne mintjançant el seu ID
    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable Long id) {
        return customerRepository.findById(id);
    }

    //ENDPOINT per poder actualitzar les dades, qualsevols, d'un alumne mitjançant el seu ID
    @PutMapping("/{id}")
    public Customer updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        return customerRepository.updateCustomer(id, customer);
    }

    //ENDPOINT per poder actualitzar parcialment les dades d'un alumne mitjançant el seu ID
    @PatchMapping("/{id}")
    public Customer updateNameAge(@PathVariable Long id, @RequestBody Customer partialCustomer) {
        return customerRepository.updateNameAge(id, partialCustomer.getName(), partialCustomer.getAge());
    }

    //ENDPOINT per poder eliminar un alumne mintjançant el seu id
    @DeleteMapping("/{id}")
    public String deleteCustomer(@PathVariable Long id) {
        return customerRepository.deleteCustomer(id);
    }
}
