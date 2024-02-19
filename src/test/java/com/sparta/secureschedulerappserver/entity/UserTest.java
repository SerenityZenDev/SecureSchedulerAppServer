package com.sparta.secureschedulerappserver.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {
    @Test
    @DisplayName("유저 생성 테스트")
    void createUser(){
        // given
        String username = "testname";
        String password = "testpass";

        // when
        User user = new User(username, password);

        // then
        assertNotNull(user);
        assertEquals("testname", user.getUsername());
        assertEquals("testpass", user.getPassword());
    }

}