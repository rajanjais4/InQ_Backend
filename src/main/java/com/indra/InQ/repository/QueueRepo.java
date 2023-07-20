package com.indra.InQ.repository;

import com.indra.InQ.modal.queue.QueueModal;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QueueRepo extends MongoRepository<QueueModal,String> {
}
