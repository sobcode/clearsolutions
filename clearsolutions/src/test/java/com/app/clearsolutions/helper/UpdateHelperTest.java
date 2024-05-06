package com.app.clearsolutions.helper;

import com.app.clearsolutions.model.User;
import com.app.clearsolutions.model.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UpdateHelperTest {
    @Autowired
    private UpdateHelper updateHelper;

    @Test
    public void testUserPatcherWithFirstNameChanges_thenEmailIsUnchangedAndFirstNameChanged()
            throws NoSuchFieldException, IllegalAccessException {
        String email = "artem@gmail.com";
        String newFirstName = "Frank";
        User user = new User(email, "Artem", "Sobko",
                LocalDate.of(2003, 9, 15));
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(newFirstName);

        updateHelper.userPatcher(user, userDTO);

        assertEquals(email, user.getEmail());
        assertEquals(newFirstName, user.getFirstName());
    }

    @Test
    public void testCheckIfFieldsAreNonNull_thenReturnsTrue() throws IllegalAccessException {
        UserDTO userDTO = new UserDTO(2, "pavlo@gmail.com", "Pavlo", "Biruk",
                LocalDate.of(2000, 5, 10), "Kharkiv", "+380667778899");

        boolean resp = updateHelper.checkIfFieldsAreNonNull(userDTO);

        assertTrue(resp);
    }

    @Test
    public void testCheckIfFieldsAreNonNullWithNullValue_thenReturnsFalse() throws IllegalAccessException {
        UserDTO userDTO = new UserDTO(2, "pavlo@gmail.com", "Pavlo", "Biruk",
                LocalDate.of(2000, 5, 10), "Kharkiv", "+380667778899");
        userDTO.setEmail(null);

        boolean resp = updateHelper.checkIfFieldsAreNonNull(userDTO);

        assertFalse(resp);
    }
}
