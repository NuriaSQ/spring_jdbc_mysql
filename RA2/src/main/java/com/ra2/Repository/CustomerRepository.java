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
            customer.setPassword(rs.getString("password"));
            customer.setDataCreated(rs.getTimestamp("dataCreated"));
            customer.setDataUpdated(rs.getTimestamp("dataUpdated"));
            customer.setImagePath(rs.getString("image_path"));
            return customer;
        }
    }

    
    //Funció per insertar només 1 alumne amb RequestBody
    public void insertCustomer(Customer customer) {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        jdbcTemplate.update("INSERT INTO customers (name, description, course, age, password, dataCreated, dataUpdated) VALUES (?, ?, ?, ?, ?, ?, ?)",customer.getName(),customer.getDescription(),customer.getCourse(),customer.getAge(),customer.getPassword(),now,now);
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
        jdbcTemplate.update("UPDATE customers SET name=?, description=?, course=?, age=?, password=?, dataUpdated=? WHERE id=?", customer.getName(), customer.getDescription(), customer.getCourse(), customer.getAge(), customer.getPassword(), now, id);
        return findById(id);
    }

    //Funció que rep l'id del alumne, el nom i l'edat per fer una actualització parcial d'aquest mateix alumne a la base de dades
    public Customer updateAge(Long id, int age) {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        jdbcTemplate.update("UPDATE customers SET age=?, dataUpdated=? WHERE id=?", age, now, id);
        return findById(id);
    }

    //Funció per eliminar a un alumne de la base de dades utilitzat l'id de l'alumne
    public void deleteCustomer(Long id) {
        jdbcTemplate.update("DELETE FROM customers WHERE id=?", id);
    }
    
    // Funció per guardar la imatge d'un usuari
    public void saveCustomerImage(Long id, String imagePath) {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        jdbcTemplate.update("UPDATE customers SET image_path = ?, dataUpdated = ? WHERE id = ?", imagePath, now, id);
    }

}



