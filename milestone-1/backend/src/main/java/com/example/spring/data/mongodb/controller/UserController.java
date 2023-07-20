package com.example.spring.data.mongodb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring.data.mongodb.model.User;
import com.example.spring.data.mongodb.repository.UserRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class UserController {

  @Autowired
  UserRepository userRepository;



  @PostMapping("/create")
  public ResponseEntity<User> createUser(@RequestBody User user) {
    try {
      System.out.println(user.getName());
      User _user = userRepository.save(new User(user.getName(), user.getPassword()));
      return new ResponseEntity<>(_user, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/login")
  public ResponseEntity<User> verifyUser(@RequestBody User user) {
    try {
      List<User> users = userRepository.findByName(user.getName());
      if (users.size()==0) throw new Exception("user not found", null);
      if(users.get(0).getPassword().equals(user.getPassword()))
        return new ResponseEntity<User>(user, HttpStatus.ACCEPTED);
      else throw new Exception("passwords not match", null);

    } catch (Exception e) {
      System.out.println(e.getMessage());
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
