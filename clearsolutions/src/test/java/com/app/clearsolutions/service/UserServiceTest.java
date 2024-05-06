package com.app.clearsolutions.service;

import com.app.clearsolutions.exception.InvalidInputFormatException;
import com.app.clearsolutions.helper.UpdateHelper;
import com.app.clearsolutions.model.User;
import com.app.clearsolutions.model.dto.DataDTO;
import com.app.clearsolutions.model.dto.UserDTO;
import com.app.clearsolutions.repository.UserRepository;
import com.app.clearsolutions.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private Environment environment;
    @Mock
    private UpdateHelper updateHelper;

    private final List<User> users;

    {
        users = new ArrayList<>();
        users.add(new User("pavlo@gmail.com", "Pavlo", "Biruk",
                LocalDate.of(2000, 5, 10)));
        users.add(new User("artem@gmail.com", "Artem", "Sobko",
                LocalDate.of(2003, 9, 15)));
        users.add(new User("oksana@gmail.com", "Oksana", "Mulkina",
                LocalDate.of(1997, 10, 10)));
        users.add(new User("yuriy@gmail.com", "Yuriy", "Gerero",
                LocalDate.of(1990, 1, 1)));
    }

    @Test
    public void testCreateUserWithProperDataAndAge_thenHttpStatusIsCreated() throws InvalidInputFormatException {
        User initUser = users.get(0);

        when(userRepository.save(any(User.class))).thenReturn(initUser);
        when(environment.getProperty(anyString())).thenReturn("18");

        ResponseEntity<Void> resp = userService.createUser(UserDTO.getDTOFromUser(initUser),
                UriComponentsBuilder.newInstance());

        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
    }

    @Test
    public void testCreateUserWithAgeLessThan18_thenThrowsIllegalArgumentException() {
        User initUser = users.get(0);
        initUser.setBirthDate(LocalDate.of(2020, 9, 15));

        when(environment.getProperty(anyString())).thenReturn("18");

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(UserDTO.getDTOFromUser(initUser),
                UriComponentsBuilder.newInstance()));
    }

    @Test
    public void testCreateUserWithWrongDataFormat_thenThrowsInvalidInputFormatException() {
        User initUser = users.get(0);
        initUser.setEmail("pavel_gaga");

        assertThrows(InvalidInputFormatException.class, () -> userService.createUser(UserDTO.getDTOFromUser(initUser),
                UriComponentsBuilder.newInstance()));
    }

    @Test
    public void testGetAllUsersWithoutFiltering_thenHttpStatusIsOkAndRespHasSameNumberOfElements() {
        Page<User> initUsersPage = new PageImpl<>(users);

        when(userRepository.findUsersByIsActiveIsTrue(any(Pageable.class))).thenReturn(initUsersPage);

        ResponseEntity<DataDTO> resp = userService.getUsers("", "", Pageable.unpaged());

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(initUsersPage.getTotalElements(), Objects.requireNonNull(resp.getBody()).getData().size());
    }

    @Test
    public void testGetAllUsersWithFromAndToFiltering_thenHttpStatusIsOkAndRespHasSameNumberOfElements() {
        Page<User> initUsersPage = new PageImpl<>(users);

        when(userRepository.findUsersByBirthDateBetweenAndIsActiveIsTrue(any(LocalDate.class),
                any(LocalDate.class), any(Pageable.class))).thenReturn(initUsersPage);

        ResponseEntity<DataDTO> resp = userService.getUsers("1980-01-01", "2020-01-01", Pageable.unpaged());

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(initUsersPage.getTotalElements(), Objects.requireNonNull(resp.getBody()).getData().size());
    }

    @Test
    public void testGetAllUsersWithOnlyFromFiltering_thenHttpStatusIsOkAndRespHasSameNumberOfElements() {
        Page<User> initUsersPage = new PageImpl<>(users);

        when(userRepository.findUsersByBirthDateAfterAndIsActiveIsTrue(any(LocalDate.class),
                any(Pageable.class))).thenReturn(initUsersPage);

        ResponseEntity<DataDTO> resp = userService.getUsers("1980-01-01", "", Pageable.unpaged());

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(initUsersPage.getTotalElements(), Objects.requireNonNull(resp.getBody()).getData().size());
    }

    @Test
    public void testGetAllUsersWithOnlyToFiltering_thenHttpStatusIsOkAndRespHasSameNumberOfElements() {
        Page<User> initUsersPage = new PageImpl<>(users);

        when(userRepository.findUsersByBirthDateBeforeAndIsActiveIsTrue(any(LocalDate.class),
                any(Pageable.class))).thenReturn(initUsersPage);

        ResponseEntity<DataDTO> resp = userService.getUsers("", "1980-01-01", Pageable.unpaged());

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(initUsersPage.getTotalElements(), Objects.requireNonNull(resp.getBody()).getData().size());
    }

    @Test
    public void testGetAllUsersWithWrongDateFormat_thenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.getUsers("1999", "1987", Pageable.unpaged()));
    }

    @Test
    public void testGetAllUsersWithWrongChronology_thenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.getUsers("2010-01-01", "1987-01-01", Pageable.unpaged()));
    }

    @Test
    public void testGetUserById_thenHttpStatusIsOkAndRespContainsOneElement() {
        User initUser = users.get(0);

        when(userRepository.findUserById(anyLong())).thenReturn(initUser);

        ResponseEntity<DataDTO> resp = userService.getUserById(1);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(1, Objects.requireNonNull(resp.getBody()).getData().size());
    }

    @Test
    public void testGetDeletedUserById_thenThrowsNullPointerException() {
        User initUser = users.get(0);
        initUser.setActive(false);

        when(userRepository.findUserById(anyLong())).thenReturn(initUser);

        assertThrows(NullPointerException.class, () -> userService.getUserById(1));
    }

    @Test
    public void testDeleteUser_thenHttpStatusIsOk() {
        User initUser = users.get(0);
        initUser.setActive(true);

        when(userRepository.findUserById(anyLong())).thenReturn(initUser);
        when(userRepository.save(any(User.class))).thenReturn(initUser);

        ResponseEntity<Void> resp = userService.deleteUser(1);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertFalse(initUser.getIsActive());
    }

    @Test
    public void testDeleteNonExistentUser_thenThrowsNullPointerException() {
        when(userRepository.findUserById(anyLong())).thenReturn(null);

        assertThrows(NullPointerException.class, () -> userService.deleteUser(1));
    }

    @Test
    public void testUpdateAllUserFields_thenHttpStatusIsOk() throws IllegalAccessException, InvalidInputFormatException {
        User initUser = users.get(0);
        initUser.setAddress("Kharkiv city, Naberezhna St. 1");
        initUser.setPhoneNumber("+380998887766");

        when(userRepository.findUserById(anyLong())).thenReturn(initUser);
        when(updateHelper.checkIfFieldsAreNonNull(any(UserDTO.class))).thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(initUser);
        when(environment.getProperty(anyString())).thenReturn("18");

        ResponseEntity<Void> resp = userService.updateAllUserFields(UserDTO.getDTOFromUser(initUser), 1);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
    }

    @Test
    public void testUpdateAllUserFieldsWithAgeLessThan18_thenThrowsIllegalArgumentException(){
        User initUser = users.get(0);
        initUser.setBirthDate(LocalDate.of(2020, 1, 1));
        initUser.setAddress("Kharkiv city, Naberezhna St. 1");
        initUser.setPhoneNumber("+380998887766");

        when(environment.getProperty(anyString())).thenReturn("18");

        assertThrows(IllegalArgumentException.class, () -> userService.updateAllUserFields(
                UserDTO.getDTOFromUser(initUser), 1));
    }

    @Test
    public void testUpdateAllUserFieldsWithWrongDataFormat_thenThrowsInvalidInputFormatException(){
        User initUser = users.get(0);
        initUser.setEmail("pavel_gaga");
        initUser.setAddress("Kharkiv city, Naberezhna St. 1");
        initUser.setPhoneNumber("+380998887766");

        assertThrows(InvalidInputFormatException.class, () -> userService.updateAllUserFields(
                UserDTO.getDTOFromUser(initUser), 1));
    }

    @Test
    public void testUpdateAllUserFieldsWithNullFields_thenThrowsIllegalArgumentException() throws IllegalAccessException {
        User initUser = users.get(0);

        when(userRepository.findUserById(anyLong())).thenReturn(initUser);
        when(updateHelper.checkIfFieldsAreNonNull(any(UserDTO.class))).thenReturn(false);
        when(environment.getProperty(anyString())).thenReturn("18");

        assertThrows(IllegalArgumentException.class, () -> userService.updateAllUserFields(
                UserDTO.getDTOFromUser(initUser), 1));
    }

    @Test
    public void testPartialUpdateUser_thenHttpStatusIsOk()
            throws NoSuchFieldException, IllegalAccessException, InvalidInputFormatException {
        User initUser = users.get(0);

        when(userRepository.findUserById(anyLong())).thenReturn(initUser);
        doNothing().when(updateHelper).userPatcher(any(User.class), any(UserDTO.class));
        when(userRepository.save(any(User.class))).thenReturn(initUser);
        when(environment.getProperty(anyString())).thenReturn("18");

        ResponseEntity<Void> resp = userService.partialUpdateUser(UserDTO.getDTOFromUser(initUser), 2);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
    }

    @Test
    public void testPartialUpdateUserWithAgeLessThan18_thenThrowsIllegalArgumentException() {
        User initUser = users.get(0);
        initUser.setBirthDate(LocalDate.of(2020, 1, 1));

        when(environment.getProperty(anyString())).thenReturn("18");

        assertThrows(IllegalArgumentException.class,
                () -> userService.partialUpdateUser(UserDTO.getDTOFromUser(initUser), 2));
    }

    @Test
    public void testPartialUpdateUserWithWrongDataFormat_thenThrowsInvalidInputFormatException() {
        User initUser = users.get(0);
        initUser.setEmail("pavel_gaga");

        when(environment.getProperty(anyString())).thenReturn("18");

        assertThrows(InvalidInputFormatException.class,
                () -> userService.partialUpdateUser(UserDTO.getDTOFromUser(initUser), 2));
    }

    @Test
    public void testPartialUpdateDeletedUser_thenThrowsNullPointerException() {
        User initUser = users.get(0);
        initUser.setActive(false);

        when(environment.getProperty(anyString())).thenReturn("18");
        when(userRepository.findUserById(anyLong())).thenReturn(initUser);

        assertThrows(NullPointerException.class,
                () -> userService.partialUpdateUser(UserDTO.getDTOFromUser(initUser), 2));
    }

}
