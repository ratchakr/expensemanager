package com.chakrar.expensemanager.repo;

import java.io.Serializable;

import org.springframework.data.couchbase.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.couchbase.client.java.repository.annotation.Id;

@Document
@JsonDeserialize(using = UserDeserializer.class)
public class User implements Serializable, Cloneable {

	public User(String username, String email, String address, String password, String phone, Integer age,
			String cardNumber, Integer zipcode) {
		this.username = username;
		this.email = email;
		this.address = address;
		this.password = password;
		this.phone = phone;
		this.age = age;
		this.cardNumber = cardNumber;
		this.zipcode = zipcode;
	}

	@Id
	private String username;
	
	private String email;
	
	private String address;
	
	private String password;
	
	private String phone;
	
	private Integer age;
	
	private String cardNumber;
	
	private Integer zipcode;
	
	@JsonIgnore
	private String type;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public User () {
		
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the age
	 */
	public Integer getAge() {
		return age;
	}

	/**
	 * @param age the age to set
	 */
	public void setAge(Integer age) {
		this.age = age;
	}

	/**
	 * @return the cardNumber
	 */
	public String getCardNumber() {
		return cardNumber;
	}

	/**
	 * @param cardNumber the cardNumber to set
	 */
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	/**
	 * @return the zipCode
	 */
	public Integer getZipcode() {
		return zipcode;
	}

	/**
	 * @param zipCode the zipCode to set
	 */
	public void setZipCode(Integer zipCode) {
		this.zipcode = zipCode;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [username=" + username + ", address=" + address + ", password=" + password + ", phone=" + phone
				+ ", age=" + age + ", cardNumber=" + cardNumber + ", zipCode=" + zipcode + "]";
	}
	
	
}
