package com.example.mpesa.repository;

import com.example.mpesa.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostgresTransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("{ 'amount' : { $gt: ?0 } }")
    List<Transaction> findTransactionsWithAmountGreaterThan(double amount);

    @Query("{ 'phoneNumber' : ?0 }")
    List<Transaction> findByPhoneNumber(String phoneNumber);
}

