package com.indra.InQ.repository;

import com.indra.InQ.modal.Entity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface EntityRepo extends MongoRepository<Entity,String> {
    Optional<Entity> findByPhoneNumber(Long phoneNumber);

    Optional<Entity> findByEmail(String email);
}
