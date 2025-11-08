package com.example.mpesa.repository;

import com.example.mpesa.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface MongoTransactionRepository extends MongoRepository<Transaction, String> {

//    @Query("{ 'amount' : { $gt: ?0 } }")
//    List<Transaction> findTransactionsWithAmountGreaterThan(double amount);
//
//    @Query("{ 'phoneNumber' : ?0 }")
//    List<Transaction> findByPhoneNumber(String phoneNumber);
//
//    @Query("{ 'status' : ?0 }")
//    List<Transaction> findByStatus(String status);
}
