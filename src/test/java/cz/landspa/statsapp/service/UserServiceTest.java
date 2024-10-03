package cz.landspa.statsapp.service;

import cz.landspa.statsapp.model.User;
import cz.landspa.statsapp.repository.UserRepository;
import cz.landspa.statsapp.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    public UserServiceTest (){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addUser(){
        User user = new User();

        user.setUsername("uzivatel");
        user.setEmail("mail@mail.com");
        user.setPassword("12345678");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.saveUser(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("uzivatel");
        verify(userRepository, times(1)).save(user);

    }

    @Test
    public void findUserByUsername (){
        User user = new User();

        user.setUsername("uzivatel");
        user.setEmail("mail@mail.com");
        user.setPassword("12345678");

        when(userRepository.findByUsername("uzivatel")).thenReturn(Optional.of(user));

        User savedUser = userService.getUserByUsername("uzivatel");

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("uzivatel");
        verify(userRepository, times(1)).findByUsername("uzivatel");

    }
}
