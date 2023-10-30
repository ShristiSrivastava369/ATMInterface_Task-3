package com.atm;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<BankAccount,Integer> {
	public BankAccount findById(int id);
	
}
