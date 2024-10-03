package cz.landspa.statsapp.repository;

import cz.landspa.statsapp.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test") // Aktivace testovacího profilu
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Zachování konfigurace z application-test.properties
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void createAndFindByUsername(){

        User user = new User();
        user.setUsername("uzivatel");

        userRepository.save(user);
        User foundUser = userRepository.findByUsername("uzivatel").get();

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo("uzivatel");
    }
}
