package cz.landspa.statsapp.service;

import cz.landspa.statsapp.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    User saveUser(User user);

    List<User> findAll();

  //  UserDetails loadUserByUsername(String username);

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    void deleteUser(Long id);

    User findById(Long id);

    User updateUser(User user, Long id);

    boolean activateUser(String token);

}
