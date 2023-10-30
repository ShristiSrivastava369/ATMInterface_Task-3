package com.atm;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class UserService{
	
	@Autowired
	public UserRepository userRepository;
	
	//Find All Users
	public List<BankAccount> allUser() {
		return (List<BankAccount>) this.userRepository.findAll();
	}
	
	// Find User
	public BankAccount getUserById(int id) {
	    return this.userRepository.findById(id);
	}
	
	//Finding user using account number
	public BankAccount getUserByAccountNumber(String Accno) {
	    for (BankAccount b : this.userRepository.findAll()) {
	        if (b.accountNumber.equals(Accno)) {
	            return b;
	        }
	    }
	    return null; // Move this line outside the loop
	}

	
	//Save new user
	public void save(BankAccount a) {
		userRepository.save(a);
	}
	
	//Delete User 
	public void deleteUser(int id) {
		this.userRepository.deleteById(id);
		
	}
	
	//Update User
	public void updateUser(BankAccount user, int userId) {
		BankAccount existingUser = this.userRepository.findById(userId);
	    if (existingUser != null) {
	        existingUser.setAccountNumber(user.accountNumber);
	        existingUser.setBalance(user.balance);
	        this.userRepository.save(existingUser);
	    }
	}
	//Deposit
    public boolean deposit(BankAccount account, double amount) {
    	if(userRepository.existsById(account.id)) {
        	if(account.getBalance()==0 || account.getBalance()<0) {
        		return false;
        	}
            account.setBalance(account.getBalance() + amount);
            return true;	
    	}
    	return false;
    }
    
    //Balance
    public double checkBalance(BankAccount b) {
    	if(userRepository.existsById(b.id)) {
    	return 0;	
    	}
    	return b.getBalance();
    }
    
    //Withdraw
    public boolean withdraw(BankAccount account, double amount) {
    	if(userRepository.existsById(account.id)) {
    	if(account.getBalance()==0 || account.getBalance()<0) {
    		return false;
    	}
        if (account.getBalance() >= amount) {
            account.setBalance(account.getBalance() - amount);
            return true; 
        }
    }
    	return false;
    }
    
}