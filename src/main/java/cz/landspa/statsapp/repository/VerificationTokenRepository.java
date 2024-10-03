package cz.landspa.statsapp.repository;

import cz.landspa.statsapp.model.User;
import cz.landspa.statsapp.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByUser(User user);
}
