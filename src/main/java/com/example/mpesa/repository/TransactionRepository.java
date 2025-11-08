package com.example.mpesa.repository;

import com.example.mpesa.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

//    @Query("SELECT t from Transactions ")
    Transaction findByIdd(Long id);
}
