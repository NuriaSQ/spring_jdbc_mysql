package com.ra2.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ra2.Model.Customer;

@Repository
public class CustomerRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final class CustomerRowMapper implements RowMapper<Customer> {
    	
        @Override
        public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
            Customer customer = new Customer();
            customer.setId(rs.getLong("id"));
            customer.setName(rs.getString("name"));
            customer.setDescription(rs.getString("description"));
            customer.setCourse(rs.getString("course"));
            customer.setAge(rs.getInt("age"));
            customer.setDataCreated(rs.getTimestamp("dataCreated"));
            customer.setDataUpdated(rs.getTimestamp("dataUpdated"));
            return customer;
        }
    }

    //Funció per insertar nous alumnes a la base de dades
    public String insertCustomers() {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        jdbcTemplate.update("INSERT INTO customers (name, description, course, age, dataCreated, dataUpdated) VALUES (?, ?, ?, ?, ?, ?)", "Alice Martin", " ", "DAM1", 20, now, now);
        jdbcTemplate.update("INSERT INTO customers (name, description, course, age, dataCreated, dataUpdated) VALUES (?, ?, ?, ?, ?, ?)", "Brian Lopez", " ", "DAW2", 49, now, now);
        jdbcTemplate.update("INSERT INTO customers (name, description, course, age, dataCreated, dataUpdated) VALUES (?, ?, ?, ?, ?, ?)", "Carlos Gomez", " ", "ASIX1", 21, now, now);
        jdbcTemplate.update("INSERT INTO customers (name, description, course, age, dataCreated, dataUpdated) VALUES (?, ?, ?, ?, ?, ?)", "Diana Perez", " ", "DAM2", 23, now, now);
        jdbcTemplate.update("INSERT INTO customers (name, description, course, age, dataCreated, dataUpdated) VALUES (?, ?, ?, ?, ?, ?)", "Elena Sanchez", " ", "DAW1", 19, now, now);
        jdbcTemplate.update("INSERT INTO customers (name, description, course, age, dataCreated, dataUpdated) VALUES (?, ?, ?, ?, ?, ?)", "Felipe Garcia", " ", "ASIX2", 24, now, now);
        jdbcTemplate.update("INSERT INTO customers (name, description, course, age, dataCreated, dataUpdated) VALUES (?, ?, ?, ?, ?, ?)", "Gloria Ruiz", " ", "DAM1", 34, now, now);
        jdbcTemplate.update("INSERT INTO customers (name, description, course, age, dataCreated, dataUpdated) VALUES (?, ?, ?, ?, ?, ?)", "Hugo Morales", " ", "DAW2", 22, now, now);
        jdbcTemplate.update("INSERT INTO customers (name, description, course, age, dataCreated, dataUpdated) VALUES (?, ?, ?, ?, ?, ?)", "Isabel Torres", " ", "ASIX1", 21, now, now);
        jdbcTemplate.update("INSERT INTO customers (name, description, course, age, dataCreated, dataUpdated) VALUES (?, ?, ?, ?, ?, ?)", "Javier Diaz", " ", "DAM2", 23, now, now);
        return "S'han inserit correctament els 10 alumnes.";
    }

    //Funció per mostrar tots els alumnes de la base de dades
    public List<Customer> findAll() {
        return jdbcTemplate.query("SELECT * FROM customers", new CustomerRowMapper());
    }

    //Funció que rep per paràmetre l'id d'un alumne i el mostra per pantalla
    public Customer findById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM customers WHERE id = ?", new CustomerRowMapper(), id);
    }

    //Funció que rep l'id del alumne del que vols actualizar la informació i també les dades de l'alumne per poder actualizar-les amb la consulta SQL
    public Customer updateCustomer(Long id, Customer customer) {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        jdbcTemplate.update("UPDATE customers SET name=?, description=?, course=?, age=?, dataUpdated=? WHERE id=?", customer.getName(), customer.getDescription(), customer.getCourse(), customer.getAge(), now, id);
        return findById(id);
    }

    //Funció que reo l'id del alumne, el nom i l'edat per fer una actualització parcial d'aquest mateix alumne a la base de dades
    public Customer updateNameAge(Long id, String name, int age) {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        jdbcTemplate.update("UPDATE customers SET name=?, age=?, dataUpdated=? WHERE id=?",name, age, now, id);
        return findById(id);
    }

    //Funció per eliminar a un alumne de la base de dades utilitzat l'id de l'alumne
    public String deleteCustomer(Long id) {
        jdbcTemplate.update("DELETE FROM customers WHERE id=?", id);
        return "S'ha eliminat correctament l'alumne amb id " + id + ".";
    } 
}


