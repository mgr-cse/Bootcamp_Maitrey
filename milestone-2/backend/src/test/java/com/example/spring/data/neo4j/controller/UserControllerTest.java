package com.example.spring.data.neo4j.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.example.spring.data.neo4j.model.User;
import com.example.spring.data.neo4j.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @InjectMocks
    UserController userController;

    @Mock
    UserRepository userRepository;

    @Test
    public void createUser() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        List<User> userResult = new ArrayList<>();
        userResult.add(new User("u1", "pass1"));
        User userToCreate = new User("n1", "pass1");

        when(userRepository.findByName("u1")).thenReturn(userResult);
        when(userRepository.findByName(userToCreate.getName())).thenReturn(new ArrayList<User>());
        when(userRepository.save(Mockito.any())).thenReturn(userToCreate);

        ResponseEntity<User> response = userController.createUser(new User("u1", "pass1"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        response = userController.createUser(userToCreate);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    }

    @Test
    public void verifyUser() {
        List<User> userList = new ArrayList<>();
        User nonExistent = new User("whoami", "secret");
        when(userRepository.findByName("whoami")).thenReturn(userList);
        ResponseEntity<User> response = userController.verifyUser(nonExistent);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        userList.add(new User("u1", "pass1"));
        when(userRepository.findByName("u1")).thenReturn(userList);
        response = userController.verifyUser(new User("u1", "incorrect"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        response = userController.verifyUser(new User("u1", "pass1"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }
}