package cz.landspa.statsapp.repository;

import cz.landspa.statsapp.model.Player;
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

public class PlayerRepositoryTest {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    TeamRepository teamRepository;

    @Test
    public void createAndFindTeam (){


        Team team = new Team();
        team.setName("Tým 1");

        teamRepository.save(team);

        Player player = new Player();
        player.setTeam(team);
        player.setName("Jmeno");
        player.setSurname("Prijmeni");

        Player savedPlayer = playerRepository.save(player);

        assertThat(savedPlayer).isNotNull();
        assertThat(savedPlayer.getName()).isEqualTo("Jmeno");

    }


}
