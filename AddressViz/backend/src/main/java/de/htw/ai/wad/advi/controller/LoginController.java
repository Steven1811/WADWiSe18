package de.htw.ai.wad.advi.controller;


import java.util.concurrent.atomic.AtomicLong;

import javax.validation.Valid;

import de.htw.ai.wad.advi.Greeting;
import de.htw.ai.wad.advi.model.User;
import de.htw.ai.wad.advi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    private UserService userService;
	
    /**
    @RequestMapping(value= { "/login" }, method = RequestMethod.GET)
    public User user(@RequestParam(value="userid") String userid, @RequestParam(value="passwd") String password) {
    	return userService.findUserByUserid(userid);
    }
    **/
}
