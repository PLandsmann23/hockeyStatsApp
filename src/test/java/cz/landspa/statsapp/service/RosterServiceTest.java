package cz.landspa.statsapp.service;

import cz.landspa.statsapp.model.Game;
import cz.landspa.statsapp.model.Player;
import cz.landspa.statsapp.model.Roster;
import cz.landspa.statsapp.model.Team;
import cz.landspa.statsapp.repository.PlayerRepository;
import cz.landspa.statsapp.repository.RosterRepository;
import cz.landspa.statsapp.repository.TeamRepository;
import cz.landspa.statsapp.service.impl.RosterServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class RosterServiceTest {

    @InjectMocks
    RosterServiceImpl rosterService;

    @Mock
    RosterRepository rosterRepository;

    @Mock
    PlayerRepository playerRepository;

    @Mock
    TeamRepository teamRepository;

    public RosterServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void savePlayer(){
        Team team = new Team();
        team.setName("Tým 1");
        team.setId(1L);
        Player player = new Player();
        player.setName("Jméno");
        player.setSurname("Příjmení");
        player.setNumber(12);
        player.setId(1L);
        player.setTeam(team);
        Game game = new Game();
        game.setOpponent("Tým 1");
        game.setId(1L);
        game.setTeam(team);

        Roster roster = new Roster();
        roster.setPlayer(player);
        roster.setGame(game);

        when(playerRepository.findById(player.getId())).thenReturn(Optional.of(player));
        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));

        when(rosterRepository.save(any(Roster.class))).thenReturn(roster);

        Roster savedPlayer = rosterService.addPlayerToRoster(roster);

        assertThat(savedPlayer).isNotNull();
        assertThat(savedPlayer.getPlayer().getName()).isEqualTo("Jméno");
        assertThat(savedPlayer.getPlayer().getSurname()).isEqualTo("Příjmení");
        assertThat(savedPlayer.getGame().getOpponent()).isEqualTo("Tým 1");
        verify(rosterRepository, times(1)).save(roster);
    }



    @Test
    public void updatePlayer(){
        Player player = new Player();
        player.setName("Jméno");

        Roster roster = new Roster();
        roster.setPlayer(player);
        roster.setId(1L);

        Player playerUpdt = new Player();
        playerUpdt.setName("Name");

        Roster rosterupdt = new Roster();
        rosterupdt.setPlayer(playerUpdt);

        Player playerUpdtDB = new Player();
        playerUpdtDB.setName("Name");
        Roster rosterupdtDB = new Roster();
        rosterupdtDB.setPlayer(playerUpdtDB);
        rosterupdtDB.setId(1L);

        when(rosterRepository.save(any(Roster.class))).thenReturn(rosterupdtDB);



        Roster updt = rosterService.updateRoster(rosterupdt,1L);

        when(rosterRepository.save(any(Roster.class))).thenReturn(updt);

        assertThat(rosterupdt.getPlayer().getName()).isEqualTo("Name");
    }
}
