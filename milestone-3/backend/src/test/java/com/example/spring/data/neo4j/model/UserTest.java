package com.example.spring.data.neo4j.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User getUser() {
        return new User("test", "secret");
    }

    @Test
    void setName() {
       User u = getUser();
       u.setName("123");
       assertEquals(u.getName(), "123");
    }

    @Test
    void setPassword() {
        User u = getUser();
        u.setPassword("123");
        assertEquals(u.getPassword(), "123");
    }

    @Test
    void testToString() {
        String s = getUser().toString();
        assertEquals(s, "User [name=test, password=secret]");
    }
}