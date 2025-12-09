package com.ra2.Controller;

import com.ra2.Model.Customer;
import com.ra2.Services.CustomerServices;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerServices customerServices;

    //ENDPOINT per poder afegir alumnes
    @PostMapping
    public ResponseEntity<String> addCustomers(@RequestBody Customer customer) {
        
        // Insertar un alumne nou utilitzant RequestBody
        customerServices.insertCustomer(customer);
        String msg = "S'ha creat l'alumne "+ customer.getName() + ".";
        return ResponseEntity.status(HttpStatus.CREATED).body(msg);
    }

    //ENDPOINT per poder mostrar per pantalla la llista de tots els alumnes de la base de dades
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerServices.findAll();
        if(customers.isEmpty()) return ResponseEntity.ok(null);
        else return ResponseEntity.ok(customers);
    }

    //ENDPOINT per mostrar un alumne mintjançant el seu ID
    @GetMapping("/{customer_id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long customer_id) {
        Customer customer = customerServices.findById(customer_id);
        if(customer == null) return ResponseEntity.ok(null);
        else return ResponseEntity.ok(customer);
    }

    //ENDPOINT per poder actualitzar les dades, qualsevols, d'un alumne mitjançant el seu ID
    @PutMapping("/{customer_id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long customer_id, @RequestBody Customer customer) {
        Customer updatedCustomer = customerServices.updateCustomer(customer_id, customer);
        if (updatedCustomer == null)return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        return ResponseEntity.ok(updatedCustomer);
    }

    //ENDPOINT per poder actualitzar l'edat d'un alumne mitjançant el seu ID
    @PatchMapping("/{customer_id}/age")
    public ResponseEntity<Customer> updateCustomerAge(@PathVariable Long customer_id, @RequestParam int age) {
        Customer updatedCustomer = customerServices.updateAge(customer_id, age);
        if (updatedCustomer == null)return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        return ResponseEntity.ok(updatedCustomer);
    }

    //ENDPOINT per poder eliminar un alumne mintjançant el seu id
    @DeleteMapping("/{customer_id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long customer_id) {
    	customerServices.deleteCustomer(customer_id);
    	String msg = "S'ha eliminat correctament l'usuari amb id " + customer_id;
        return ResponseEntity.ok(msg);
    }
    
    //ENDPOINT per poder afegir una imatge de perfil d'un alumne
    @PostMapping("/{customer_id}/image")
    public ResponseEntity<String> uploadCustomerImage(@PathVariable Long customer_id, @RequestParam MultipartFile imageFile) {
        String imageUrl = customerServices.saveStudentImage(customer_id, imageFile);
        if (imageUrl == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No s'ha trobat l'usuari amb id " + customer_id);
        return ResponseEntity.ok(imageUrl);
    }
    
  //ENDPOINT per fer una càrrega massiva d'usuaris amb un arxiu .csv
    @PostMapping("/upload-csv")
    public ResponseEntity<String> uploadCustomersCsv(@RequestParam MultipartFile csvFile){
        if (csvFile.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR: El fitxer CSV està buit.");
        int totalInserits = customerServices.insertAllCustomersByCsv(csvFile);
        return ResponseEntity.ok("Registres inserits: " + totalInserits);
    }
    
    //ENDPOINT per fer una càrrega massiva d'usuaris amb un arxiu .json
    @PostMapping("/upload-json")
    public ResponseEntity<String> uploadCustomersJSON(@RequestParam MultipartFile jsonFile){
    	if (jsonFile.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR: El fitxer CSV està buit.");
    	int totalInserits = customerServices.insertAllCustomersByJSON(jsonFile);
    	return ResponseEntity.ok("Registres inserits: " + totalInserits);
    }

}
