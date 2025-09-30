package main.java.repositories;

import java.util.List;

import main.java.models.Transaction;

public interface TransactionRepository {

    void add(Transaction transaction);                
    Transaction getById(String id);                   
    List<Transaction> getBySource(String address);   
    List<Transaction> getByDestination(String address); 
    List<Transaction> getAll();                       
    void update(Transaction transaction);             
    void delete(String id);    
    
} 