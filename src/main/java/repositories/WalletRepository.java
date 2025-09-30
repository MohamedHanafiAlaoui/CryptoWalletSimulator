package main.java.repositories;

import java.util.List;

import main.java.models.Wallet;

public interface WalletRepository {
  void add(Wallet wallet);              
    Wallet getById(String id);            
    Wallet getByAddress(String address);  
    List<Wallet> getAll();                
    void update(Wallet wallet);           
    void delete(String id);      
    
}
