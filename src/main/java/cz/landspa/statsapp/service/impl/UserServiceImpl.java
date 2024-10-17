package cz.landspa.statsapp.service.impl;

import cz.landspa.statsapp.exception.UniqueKeyConstraintViolationException;
import cz.landspa.statsapp.model.Team;
import cz.landspa.statsapp.model.User;
import cz.landspa.statsapp.model.UserSetting;
import cz.landspa.statsapp.model.VerificationToken;
import cz.landspa.statsapp.repository.TeamRepository;
import cz.landspa.statsapp.repository.UserRepository;
import cz.landspa.statsapp.repository.UserSettingRepository;
import cz.landspa.statsapp.repository.VerificationTokenRepository;
import cz.landspa.statsapp.service.TeamService;
import cz.landspa.statsapp.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {



    private final TeamService teamService;
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserSettingRepository userSettingRepository;
    UserRepository userRepository;

    PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, TeamService teamService, PasswordEncoder passwordEncoder, VerificationTokenRepository verificationTokenRepository, UserSettingRepository userSettingRepository) {
        this.userRepository = userRepository;
        this.teamService = teamService;
        this.passwordEncoder = passwordEncoder;
        this.verificationTokenRepository = verificationTokenRepository;
        this.userSettingRepository = userSettingRepository;
    }

    @Override
    public User saveUser(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User createdUser = userRepository.save(user);
            UserSetting setting = new UserSetting();
            setting.setUser(createdUser);
            userSettingRepository.save(setting);
            verificationTokenRepository.save(new VerificationToken(createdUser));
            return createdUser;
        } catch (DataIntegrityViolationException e){
            throw new UniqueKeyConstraintViolationException();
        }
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }


    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        List<Team> userTeams = teamService.getUserTeams(userRepository.findById(id).get());
        for(Team t: userTeams){
            teamService.deleteTeam(t.getId());
        }
        userSettingRepository.deleteById(id);
        userRepository.deleteById(id);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User updateUser(User user, Long id) {
        User foundUser = userRepository.findById(id).orElse(null);

        if(foundUser != null){
            if(user.getUsername().isEmpty()){
                foundUser.setUsername(user.getUsername());
            }
            if(!user.getEmail().isEmpty()){
                foundUser.setEmail(user.getEmail());
            }
            if(!user.getPassword().isEmpty()){
                foundUser.setPassword(user.getPassword());
            }
            return userRepository.save(foundUser);
        } else {
            return null;
        }
    }

    @Override
    public boolean activateUser(String token) {
        if(userRepository.findByToken(token).isPresent()){
            User user = userRepository.findByToken(token).get();
            user.setEnabled(true);
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }
}
