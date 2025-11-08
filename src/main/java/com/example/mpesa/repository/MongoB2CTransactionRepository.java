package com.example.mpesa.repository;

import com.example.mpesa.model.B2CTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoB2CTransactionRepository extends MongoRepository<B2CTransaction, String> {
}
