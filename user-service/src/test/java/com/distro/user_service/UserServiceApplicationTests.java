package com.distro.user_service;

import com.distro.user_service.entity.User;
import com.distro.user_service.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceApplicationTests {


    @Autowired
    UserRepository userRepository;

	@Test
	void contextLoads() {
	}

    @Disabled
    @Test
    void addUser() {

        final int i1 = 200;

        for (int i = 0; i < i1; i++) {
            User user = User.builder()
                    .address(i + "Examle st")
                    .email("user" + i + "@gmail.com")
                    .name("User" + i)
                    .surname("Surname" + i)
                    .alerting(i%2 == 0)
                    .energyAlertingThreshold(1000.0 + i)
                    .build();
            userRepository.save(user);
        }

    }



}
