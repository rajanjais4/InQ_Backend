package com.indra.InQ.repository;

import com.indra.InQ.modal.user.UserDb;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepo extends MongoRepository<UserDb,String> {
    Optional<UserDb> findByPhoneNumber(Long phoneNumber);
}
