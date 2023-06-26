package com.queue.queue_client.repository;

import com.queue.queue_client.models.Entity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EntityRepository extends MongoRepository<Entity, Integer> {
}
