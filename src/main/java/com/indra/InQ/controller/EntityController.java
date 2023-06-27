package com.indra.InQ.controller;

import com.indra.InQ.modal.Entity;
import com.indra.InQ.service.EntityService;
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
    @GetMapping("/getEntityByPhoneNumber")
    public ResponseEntity<Entity> getEntityByPhoneNumber(@RequestParam("phoneNumber")String phoneNumber){
        Entity entity= entityService.findUserByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(entity);
    }

    @GetMapping("/getAllEntity")
    public ResponseEntity<List<Entity>> getAllEntity(){
        List<Entity> entityList= entityService.findAll();
        return ResponseEntity.ok(entityList);
    }
}
