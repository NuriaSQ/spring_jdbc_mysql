package com.ra2.Model;

import java.sql.Timestamp;

public class Customer {
    private long id;
    private String name;
    private String description;
    private String course;
    private String password;
    private int age;
    private Timestamp dataCreated;
    private Timestamp dataUpdated;

    public Customer() {
    	
    }

    public Customer(long id, String name, String description, String course, String password, int age, Timestamp dataCreated, Timestamp dataUpdated) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.course = course;
        this.password = password;
        this.age = age;
        this.dataCreated = dataCreated;
        this.dataUpdated = dataUpdated;
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Timestamp getDataCreated() {
		return dataCreated;
	}

	public void setDataCreated(Timestamp dataCreated) {
		this.dataCreated = dataCreated;
	}

	public Timestamp getDataUpdated() {
		return dataUpdated;
	}

	public void setDataUpdated(Timestamp dataUpdated) {
		this.dataUpdated = dataUpdated;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", name=" + name + ", description=" + description + ", course=" + course
				+ ", password=" + password + ", age=" + age + ", dataCreated=" + dataCreated + ", dataUpdated="
				+ dataUpdated + "]";
	}
}
