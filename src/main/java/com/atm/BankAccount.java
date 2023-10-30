package com.atm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class BankAccount {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name="id")
	    public int id;
	    @Column(name="Account_Number",unique=true)
	    public String accountNumber;
	    @Column(name="Balance")
	    public double balance;


		// Constructors both default and parameter
	    public BankAccount(String accountNumber, double initialBalance) {
	        this.accountNumber = accountNumber;
	        this.balance = initialBalance;
	    }
	    
	    public BankAccount() {
	        this.accountNumber = "123456789"; 
	        this.balance = 0.0; 
	    }


	    public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		
	    public String getAccountNumber() {
	        return accountNumber;
	    }

	    
	    public double getBalance() {
	        return balance;
	    }

	    public void setAccountNumber(String accountNumber) {
			this.accountNumber = accountNumber;
		}

		public void setBalance(double balance) {
			this.balance = balance;
		}

		
	    public void deposit(double amount) {
	        if (amount > 0) {
	            balance += amount;
	        }
	    }

	    
	    public boolean withdraw(double amount) {
	        if (amount > 0 && balance >= amount) {
	            balance -= amount;
	            return true; 
	        }
	        return false; 
	    }
	}

