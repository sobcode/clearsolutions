package com.app.clearsolutions.repository;

import com.app.clearsolutions.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveUser() {
        User user = userRepository.save(new User("artem@gmail.com", "Artem", "Sobko",
                LocalDate.of(2003,9,15)));

        assertTrue(user.getId() != 0);
    }

    @Test
    public void testFindAllUsers() {
        for(int i = 0; i < 3; i++) {
            userRepository.save(new User("artem" + i + "@gmail.com", "Artem", "Sobko",
                    LocalDate.of(2003,9,15)));
        }

        assertEquals(3, userRepository.findAll().size());
    }

    @Test
    public void testFindUsersByIsActiveIsTrue() {
        User user = new User("pavlo@gmail.com", "Pavlo", "Biruk",
                LocalDate.of(2003,9,15));
        user.setActive(false);

        userRepository.save(user);
        userRepository.save(new User("artem@gmail.com", "Artem", "Sobko",
                LocalDate.of(2003,9,15)));

        assertEquals(1, userRepository.findUsersByIsActiveIsTrue(Pageable.unpaged()).getTotalElements());
    }

    @Test
    public void testFindUsersByBirthDateBetweenAndIsActiveIsTrue() {
        for(int i = 0; i < 4; i++) {
            userRepository.save(new User("artem" + i + "@gmail.com", "Artem", "Sobko",
                    LocalDate.of(2000 + i,9,15)));
        }

        LocalDate from = LocalDate.of(2001, 1,1);
        LocalDate to = LocalDate.of(2003, 1,1);

        assertEquals(2, userRepository.findUsersByBirthDateBetweenAndIsActiveIsTrue(
                from, to, Pageable.unpaged()).getSize());
    }

    @Test
    public void testFindUsersByBirthDateBeforeAndIsActiveIsTrue() {
        for(int i = 0; i < 4; i++) {
            userRepository.save(new User("artem" + i + "@gmail.com", "Artem", "Sobko",
                    LocalDate.of(2000 + i,9,15)));
        }

        LocalDate to = LocalDate.of(2003, 1,1);

        assertEquals(3, userRepository.findUsersByBirthDateBeforeAndIsActiveIsTrue(
                to, Pageable.unpaged()).getSize());
    }

    @Test
    public void testFindUsersByBirthDateAfterAndIsActiveIsTrue() {
        for(int i = 0; i < 4; i++) {
            userRepository.save(new User("artem" + i + "@gmail.com", "Artem", "Sobko",
                    LocalDate.of(2000 + i,9,15)));
        }

        LocalDate from = LocalDate.of(2001, 1,1);

        assertEquals(3, userRepository.findUsersByBirthDateAfterAndIsActiveIsTrue(
                from, Pageable.unpaged()).getSize());
    }
}
