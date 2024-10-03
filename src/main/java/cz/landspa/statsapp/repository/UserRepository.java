package cz.landspa.statsapp.repository;

import cz.landspa.statsapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username = :user OR u.email = :user")
    Optional<User> findByUsername(@Param("user") String username);


    @Query("SELECT vt.user FROM VerificationToken vt WHERE vt.token = :token")
    Optional<User> findByToken(@Param("token") String token);

    Optional<User> findByEmail(String email);
}
