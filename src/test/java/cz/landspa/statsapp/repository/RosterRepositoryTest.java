package cz.landspa.statsapp.repository;


import cz.landspa.statsapp.model.Game;
import cz.landspa.statsapp.model.Player;
import cz.landspa.statsapp.model.Roster;
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
public class RosterRepositoryTest {
    @Autowired
    RosterRepository rosterRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    GameRepository gameRepository;

    @Test
    public void createAndFindGame (){


        Team team = new Team();
        team.setName("Tým 1");

        teamRepository.save(team);

        Game game = new Game();
        game.setTeam(team);
        game.setOpponent("Soupeř");
        game.setVenue("Location");

        gameRepository.save(game);

        Player player = new Player();
        player.setTeam(team);
        player.setName("Jmeno");
        player.setSurname("Prijmeni");
        player.setNumber(12);

        playerRepository.save(player);

        Roster roster = new Roster();
        roster.setGame(game);
        roster.setPlayer(player);

        Roster savedRoster = rosterRepository.save(roster);

        assertThat(savedRoster).isNotNull();
        assertThat(savedRoster.getPlayer().getSurname()).isEqualTo("Prijmeni");
        assertThat(savedRoster.getGame().getOpponent()).isEqualTo("Soupeř");
    }
}
