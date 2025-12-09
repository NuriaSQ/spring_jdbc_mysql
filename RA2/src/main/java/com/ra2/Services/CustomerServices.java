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
import com.ra2.Logging.CustomLogging;
import com.ra2.Model.Customer;
import com.ra2.Repository.CustomerRepository;


@Service
public class CustomerServices {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomLogging customLogging;

    @Autowired
    private ObjectMapper mapper;

    //Funció que comprova la llargada del nom, del passwordm l'edat i si el curs es null o està buit
    public void insertCustomer(Customer customer) {
        customLogging.logInfo("CustomerServices", "insertCustomer", "Creant un estudiant");
        if (customer.getName() != null && customer.getName().length() <= 50 && customer.getAge() >= 0 && customer.getAge() <= 118 && customer.getPassword() != null && customer.getPassword().length() >= 3 && customer.getCourse() != null && !customer.getCourse().isEmpty()) {

            customerRepository.insertCustomer(customer);
            customLogging.logInfo("CustomerServices", "insertCustomer", "Estudiant creat correctament");
        } else {
            customLogging.logError("CustomerServices", "insertCustomer", "L'estudiant no s'ha creat correctament", new Exception("Dades incorrectes"));
        }
    }

    //Funció que comprova si la llista d'estudiants està buida o no
    public List<Customer> findAll() {
        customLogging.logInfo("CustomerServices", "findAll", "Consultant tots els estudiants");
        if(customerRepository.findAll().isEmpty()) return null;
        else return customerRepository.findAll();
    }

    // Funció que comprova si existeix la id d'un estudiant
    public Customer findById(Long id) {
        customLogging.logInfo("CustomerServices", "findById", "Consultant l'estudiant amb id: " + id);
        try {
            Customer c = customerRepository.findById(id);
            if (c == null) customLogging.logError("CustomerServices", "findById", "L'estudiant amb id: " + id + " no existeix", new Exception("ID inexistent"));
            return c;
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            customLogging.logError("CustomerServices", "findById", "L'estudiant amb id: " + id + " no existeix", e);
            return null;
        }
    }

    //Funció que comprova es mateixos paràmetres d'introducción de dades que "insertCustomer" però per actualitzar
    public Customer updateCustomer(Long id, Customer customer) {
        customLogging.logInfo("CustomerServices", "updateCustomer", "Modificant l'estudiant amb id: " + id);
        if (customer.getName() != null && customer.getName().length() <= 50 && customer.getAge() >= 0 && customer.getAge() <= 118 && customer.getPassword() != null && customer.getPassword().length() >= 3 && customer.getCourse() != null && !customer.getCourse().isEmpty()) {

            Customer updated = customerRepository.updateCustomer(id, customer);
            customLogging.logInfo("CustomerServices", "updateCustomer", "Estudiant modificat correctament");
            return updated;
        } else {
            customLogging.logError("CustomerServices", "updateCustomer", "L'estudiant no s'ha modificat correctament", new Exception("Dades incorrectes"));
            return null;
        }
    }

    //Funció que verifica que la nova edat està dins dels paràmetres acceptables
    public Customer updateAge(Long id, int age) {
        customLogging.logInfo("CustomerServices", "updateAge", "Modificant l'edat de l'estudiant amb id: " + id);
        if (age >= 0 && age <= 118) {
            Customer updated = customerRepository.updateAge(id, age);
            customLogging.logInfo("CustomerServices", "updateAge", "Estudiant modificat correctament");
            return updated;
        } else {
            customLogging.logError("CustomerServices", "updateAge", "Edat fora de rang", new Exception("Edat incorrecta"));
            return null;
        }
    }

    //Funció que verifica si l'usuari existeix per poder esborrar-lo
    public void deleteCustomer(Long id) {
        customLogging.logInfo("CustomerServices", "deleteCustomer", "Borrant l'estudiant amb id: " + id);
        Customer exists = customerRepository.findById(id);
        if (exists != null) {
            customerRepository.deleteCustomer(id);
            customLogging.logInfo("CustomerServices", "deleteCustomer", "L'estudiant amb id: " + id + " s'ha borrat correctament");
        } else {
            customLogging.logError("CustomerServices", "deleteCustomer", "L'estudiant amb id: " + id + " no existeix", new Exception("ID inexistent"));
        }
    }

    //Funció que permet inserir una imatge d'un alumne a la carpeta private/images i afegir el path a la base de dades
    public String saveStudentImage(Long studentId, MultipartFile imageFile) {
        customLogging.logInfo("CustomerServices", "saveStudentImage", "Afegint la imatge " + imageFile.getOriginalFilename() + " per a l'estudiant amb id: " + studentId);

        if (!verifyStudent(studentId)) {
            customLogging.logError("CustomerServices", "saveStudentImage", "L'estudiant amb id: " + studentId + " no existeix", new Exception("ID inexistent"));
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

            return dbPath;
        } catch (IOException e) {
            customLogging.logError("CustomerServices", "saveStudentImage", "Error guardant imatge", e);
            return null;
        }
    }

    //Funció que ens permet inserir 10 alumnes mitjançant un arxiu .csv
    public int insertAllCustomersByCsv(MultipartFile file) {
        customLogging.logInfo("CustomerServices", "insertAllCustomersByCsv", "Carregant la informació del fitxer " + file.getOriginalFilename());
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

            Path folder = Paths.get("src/main/resources/csv_processed");
            Files.createDirectories(folder);

            Path desti = folder.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), desti, StandardCopyOption.REPLACE_EXISTING);

            customLogging.logInfo("CustomerServices", "insertAllCustomersByCsv", "S'han guardat correctament " + numRegInsert + " registres");
        } catch (FileNotFoundException e) {
            customLogging.logError("CustomerServices", "insertAllCustomersByCsv", "Fitxer no trobat", e);
        } catch (IOException e) {
            customLogging.logError("CustomerServices", "insertAllCustomersByCsv", "Error d'accés al fitxer", e);
        } catch (Exception e) {
            customLogging.logError("CustomerServices", "insertAllCustomersByCsv", "Error inesperat", e);
        }

        return numRegInsert;
    }

    //Funció que ens permet inserir 3 alumnes mitjançant un arxiu .json
    public int insertAllCustomersByJSON(MultipartFile file) {
        customLogging.logInfo("CustomerServices", "insertAllCustomersByJSON", "Carregant la informació del fitxer " + file.getOriginalFilename());
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

            customLogging.logInfo("CustomerServices", "insertAllCustomersByJSON", "S'han guardat correctament " + numRegInsert + " registres");
        } catch (Exception e) {
            customLogging.logError("CustomerServices", "insertAllCustomersByJSON", "Error important JSON", e);
        }

        return numRegInsert;
    }

    //Funció que comprova si un estudiant existeix o no
    public Boolean verifyStudent(Long studentId) {
        customLogging.logInfo("CustomerServices", "verifyStudent", "Consultant si existeix l'estudiant amb id: " + studentId);
        Customer customer = findById(studentId);
        return customer != null;
    }   
}
