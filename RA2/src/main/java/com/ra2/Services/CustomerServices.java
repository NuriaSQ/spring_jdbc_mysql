package com.ra2.Services;

import java.util.List;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ra2.Model.Customer;
import com.ra2.Repository.CustomerRepository;

@Service
public class CustomerServices {

	@Autowired
    private CustomerRepository customerRepository;
	
	@Autowired
	private ObjectMapper mapper;
	
	//Funció que comprova la llargada del nom, del passwordm l'edat i si el curs es null o està buit
    public void insertCustomer(Customer customer) {
        if (customer.getName() != null && customer.getName().length() <= 50 && customer.getAge() >= 0 && customer.getAge() <= 118 && customer.getPassword() != null && customer.getPassword().length() >= 3 && customer.getCourse() != null && !customer.getCourse().isEmpty()) {

            customerRepository.insertCustomer(customer);
        } else System.out.print("No s'ha pogut insertar l'alumne");
    }

    //Funció que comprova si la llista d'estudiants està buida o no
    public List<Customer> findAll() {
    	if(customerRepository.findAll().isEmpty()) return null;
    	else return customerRepository.findAll();
    }

 // Funció que comprova si existeix la id d'un estudiant
    public Customer findById(Long id) {
        try {
            return customerRepository.findById(id);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }

    //Funció que comprova es mateixos paràmetres d'introducción de dades que "insertCustomer" però per actualitzar
    public Customer updateCustomer(Long id, Customer customer) {
        if (customer.getName() != null && customer.getName().length() <= 50 && customer.getAge() >= 0 && customer.getAge() <= 118 && customer.getPassword() != null && customer.getPassword().length() >= 3 && customer.getCourse() != null && !customer.getCourse().isEmpty()) {
            return customerRepository.updateCustomer(id, customer);
        }
        return null;
    }

    //Funció que verifica que la nova edat està dins dels paràmetres acceptables
    public Customer updateAge(Long id, int age) {
        if (age >= 0 && age <= 118) return customerRepository.updateAge(id, age);
        return null;
    }

    //Funcií que verifica si l'usuari existeix per poder esborrar-lo
    public void deleteCustomer(Long id) {
        Customer exists = customerRepository.findById(id);
        if (exists != null)customerRepository.deleteCustomer(id);
        else System.out.println("No s'ha trobat l'usuari amb aquest id");
    }
    
    //Funció que permet inserir una imatge d'un alumne a la carpeta private/images i afegir el path a la base de dades
    public String saveStudentImage(Long studentId, MultipartFile imageFile) {

        //Verifiquem si l'estudiant existeix o no
        if (!verifyStudent(studentId)) {
            System.out.println("Error: estudiante no encontrado en la base de datos.");
            return null;
        }

        try {
            Path projectRoot = Paths.get("").toAbsolutePath();
            Path folder = projectRoot.resolve("src/main/resources/private/images");
            Files.createDirectories(folder);

            String fileName = "user_" + studentId + "_" + imageFile.getOriginalFilename();
            Path filePath = folder.resolve(fileName);
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String dbPath = "private/images/" + fileName;
            customerRepository.saveCustomerImage(studentId, dbPath);

            System.out.println("Imagen guardada en: " + filePath.toString());
            return dbPath;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    //Funció que ens permet inserir 10 alumnes mitjançant un arxiu .csv
    public int insertAllCustomersByCsv(MultipartFile file) {
        int numRegInsert = 0;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String linia = br.readLine();
            int numeroLinia = 0;

            while (linia != null) {
                numeroLinia++;

                if (numeroLinia != 1) {
                    String[] camps = linia.split(",");

                    Customer customer = new Customer();
                    customer.setName(camps[0].trim());
                    customer.setDescription(camps[1].trim());
                    customer.setCourse(camps[2].trim());
                    customer.setAge(Integer.parseInt(camps[3].trim()));
                    customer.setPassword(camps[4].trim());

                    customerRepository.insertCustomer(customer);
                    numRegInsert++;
                }

                linia = br.readLine();
            }
            
            //El guardem a la carpeta resources
            Path folder = Paths.get("src/main/resources/csv_processed");
            Files.createDirectories(folder);

            Path desti = folder.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), desti, StandardCopyOption.REPLACE_EXISTING);

        } catch (FileNotFoundException e) {
            System.err.println("ERROR: Fitxer no trobat");
        } catch (IOException e) {
            System.err.println("ERROR d'accés al fitxer: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("ERROR inesperat: " + e.getMessage());
            e.printStackTrace();
        }

        return numRegInsert;
    }

    //Funció que ens permet inserir 3 alumnes mitjançant un arxiu .json
    public int insertAllCustomersByJSON(MultipartFile file) {
        int numRegInsert = 0;

        try {
            JsonNode arrel = mapper.readTree(file.getInputStream());

            JsonNode users = arrel.path("data").path("users");

            if (users.isArray()) {
                for (JsonNode node : users) {
                    Customer customer = new Customer();
                    customer.setName(node.get("name").asText());
                    customer.setDescription(node.get("description").asText());
                    customer.setCourse(node.get("course").asText());
                    customer.setAge(node.get("age").asInt());
                    customer.setPassword(node.get("password").asText());

                    customerRepository.insertCustomer(customer);
                    numRegInsert++;
                }
            }

            Path folder = Paths.get("src/main/resources/json_processed");
            Files.createDirectories(folder);

            Path desti = folder.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), desti, StandardCopyOption.REPLACE_EXISTING);

        } catch (Exception e) {
            System.err.println("ERROR importando JSON: " + e.getMessage());
        }

        return numRegInsert;
    }


    //Funció que comprova si un estudiant existeix o no
    public Boolean verifyStudent(Long studentId) {
        Customer customer = findById(studentId);
        return customer != null;
    }   
}
