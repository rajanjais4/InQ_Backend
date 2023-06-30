package com.indra.InQ.controller;

import com.indra.InQ.exception.ApiRequestException;
import com.indra.InQ.modal.Entity;
import com.indra.InQ.modal.EntityQueueModal;
import com.indra.InQ.service.EntityService;
import com.indra.InQ.service.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
public class EntityController {
    @Autowired
    EntityService entityService;

    @Autowired
    QueueService queueService;
    @ApiIgnore
    @RequestMapping(value="/")
    public void redirect(HttpServletResponse response) throws IOException {

        response.sendRedirect("/swagger-ui.html");
    }
    @PostMapping("/addNewEntity")
    public ResponseEntity<Entity> addNewEntity(@RequestBody Entity entity){
        entityService.saveNewUser(entity);
        return ResponseEntity.ok(entity);
    }
    @PostMapping("/updateEntity")
    public ResponseEntity<Entity> updateEntity(@RequestBody Entity entity){

        return ResponseEntity.ok(entityService.updateEntity(entity));
    }
    @GetMapping("/getEntityByPhoneNumber")
    public ResponseEntity<Entity> getEntityByPhoneNumber(@RequestParam("phoneNumber")Long phoneNumber){
        Entity entity= entityService.findUserByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(entity);
    }
    @GetMapping("/logInEntityByPhoneNumber")
    public ResponseEntity<EntityQueueModal> logInEntityQueueByPhoneNumber(@RequestParam("phoneNumber")Long phoneNumber,
                                                                     @RequestParam("password")String password){
        return ResponseEntity.ok(entityService.logInEntityQueueByPhoneNumber(phoneNumber,password));
    }

    @GetMapping("/getAllEntity")
    public ResponseEntity<List<Entity>> getAllEntity(){
        List<Entity> entityList= entityService.findAll();
        return ResponseEntity.ok(entityList);
    }
}
