package com.indra.InQ.controller;

import com.indra.InQ.service.userService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UserControllerWs {
    @Autowired
    UserService userService;

}
