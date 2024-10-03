package cz.landspa.statsapp.repository;

import cz.landspa.statsapp.model.Team;
import cz.landspa.statsapp.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test") // Aktivace testovacího profilu
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Zachování konfigurace z application-test.properties

public class TeamRepositoryTest {

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    public void createAndFindTeam (){

        Team team = new Team();
        team.setName("Tým 1");

        teamRepository.save(team);
        Team savedTeam = teamRepository.findById(team.getId()).orElse(null);

        assertThat(savedTeam).isNotNull();
        assertThat(savedTeam.getName()).isEqualTo("Tým 1");

    }

    @Test
    public void findByUser(){
        User user = new User();
        user.setUsername("uzivatel");

        userRepository.save(user);

        Team team = new Team();
        team.setName("Tým 1");
        team.setOwner(user);

        teamRepository.save(team);
        List<Team> savedTeam = teamRepository.findAllByOwner(user);

        assertThat(savedTeam).isNotNull();
        assertThat(savedTeam).hasSize(1);
        assertThat(savedTeam.get(0).getOwner().getUsername()).isEqualTo("uzivatel");



    }
}
