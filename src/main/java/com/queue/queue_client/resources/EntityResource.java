
package com.queue.queue_client.resources;

import com.queue.queue_client.models.Entity;
import com.queue.queue_client.repository.EntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EntityResource {

    @Autowired
    private EntityRepository repository;

    @PostMapping("/addEntity")
    public String saveEntity(@RequestBody Entity entity) {
        repository.save(entity);
        return "Added Entity with id :" + entity.getId();
    }

    @GetMapping("/getAllEntities")
    public List<Entity> getEntities() {
        return repository.findAll();
    }

    @GetMapping("/findEntity/{id}")
    public Entity getEntity(@PathVariable int id) {
        return repository.findById(id).orElse(new Entity(99, "b", "c", "d"));

    }

    @DeleteMapping("/delete/{id}")
    public String deleteEntity(@PathVariable int id) {
        repository.deleteById(id);
        return "Deleted Entity with id :" + id;
    }
}
