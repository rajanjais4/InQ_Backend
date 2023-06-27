package com.indra.InQ.repository;

import com.indra.InQ.modal.Entity;
import com.indra.InQ.modal.QueueModal;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QueueRepo extends MongoRepository<QueueModal,String> {
}
