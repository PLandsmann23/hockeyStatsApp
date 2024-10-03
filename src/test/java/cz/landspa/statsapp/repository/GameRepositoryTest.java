package cz.landspa.statsapp.repository;

import cz.landspa.statsapp.model.Game;
import cz.landspa.statsapp.model.Game;
import cz.landspa.statsapp.model.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test") // Aktivace testovacího profilu
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Zachování konfigurace z application-test.properties

public class GameRepositoryTest {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    TeamRepository teamRepository;
    
    @Test
    public void createAndFindGame (){


        Team team = new Team();
        team.setName("Tým 1");

        teamRepository.save(team);

        Game game = new Game();
        game.setTeam(team);
        game.setOpponent("Soupeř");
        game.setVenue("Location");

        Game savedGame = gameRepository.save(game);

        assertThat(savedGame).isNotNull();
        assertThat(savedGame.getOpponent()).isEqualTo("Soupeř");

    }
}
