package cz.landspa.statsapp.service;

import cz.landspa.statsapp.model.Player;
import cz.landspa.statsapp.repository.PlayerRepository;
import cz.landspa.statsapp.service.impl.PlayerServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class PlayerServiceTest {
    
    @InjectMocks
    PlayerServiceImpl playerService;
    
    @Mock
    PlayerRepository playerRepository;

    public PlayerServiceTest (){
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void savePlayer(){
        Player player = new Player();
        player.setName("Jméno");
        player.setSurname("Příjmení");
        player.setNumber(12);

        when(playerRepository.save(any(Player.class))).thenReturn(player);

        Player savedPlayer = playerService.createPlayer(player);

        assertThat(savedPlayer).isNotNull();
        assertThat(savedPlayer.getName()).isEqualTo("Jméno");
        assertThat(savedPlayer.getSurname()).isEqualTo("Příjmení");
        assertThat(savedPlayer.getNumber()).isEqualTo(12);
        verify(playerRepository, times(1)).save(player);
    }

    @Test
    public void getPlayerById(){
        Player player = new Player();
        player.setName("Jméno");
        player.setId(1L);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));

        Player savedPlayer = playerService.getPlayerById(1L);

        assertThat(savedPlayer).isNotNull();
        assertThat(savedPlayer.getName()).isEqualTo("Jméno");
        verify(playerRepository, times(1)).findById(1L);
    }

    @Test
    public void updatePlayer(){
        Player player = new Player();
        player.setName("Jméno");
        player.setId(1L);

        Player playerUpdt = new Player();
        playerUpdt.setName("Name");

        Player playerUpdtDB = new Player();
        playerUpdtDB.setName("Name");
        playerUpdtDB.setId(1L);

        when(playerRepository.save(any(Player.class))).thenReturn(playerUpdtDB);



        Player updt = playerService.updatePlayer(playerUpdt,1L);

        when(playerRepository.save(any(Player.class))).thenReturn(updt);

        assertThat(playerUpdt.getName()).isEqualTo("Name");
    }
    
}
