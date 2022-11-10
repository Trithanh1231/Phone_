package com.edu.rest.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edu.model.Product;
import com.edu.model.User;
import com.edu.service.UserService;



@CrossOrigin("*")
@RestController
@RequestMapping("/rest/accounts")
public class UserRestController {
	@Autowired
	UserService userService;
	
	@GetMapping()
    public List<User> getAccount(@RequestParam("admin") Optional<Boolean> admin) {
            if(admin.orElse(false)) {
                return userService.getAdministrators();
            }
            return userService.findAll();
    }
    @PutMapping("{username}")
    public User update(@PathVariable("username") String username, @RequestBody User account) {
        return userService.save(account);
    }
    
    @GetMapping("{username}")
    public User getOne(@PathVariable("username") String username) {
        return userService.findById(username);
    }
}
