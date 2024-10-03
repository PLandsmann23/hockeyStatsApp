package cz.landspa.statsapp.service;

import cz.landspa.statsapp.model.Game;
import cz.landspa.statsapp.repository.GameRepository;
import cz.landspa.statsapp.service.impl.GameServiceImpl;
import cz.landspa.statsapp.service.impl.GameServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GameServiceTest {

    @InjectMocks
    GameServiceImpl gameService;

    @Mock
    GameRepository gameRepository;

    public GameServiceTest(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void saveGame(){
        Game game = new Game();
        game.setOpponent("Tým 1");

        when(gameRepository.save(any(Game.class))).thenReturn(game);

        Game savedGame = gameService.createGame(game);

        assertThat(savedGame).isNotNull();
        assertThat(savedGame.getOpponent()).isEqualTo("Tým 1");
        verify(gameRepository, times(1)).save(game);
    }



    @Test
    public void updateGame(){
        Game game = new Game();
        game.setOpponent("Tým 1");
        game.setId(1L);

        Game gameUpdt = new Game();
        gameUpdt.setOpponent("Tým 1 updt");

        Game gameUpdtDB = new Game();
        gameUpdtDB.setOpponent("Tým 1 updt");
        gameUpdtDB.setId(1L);

        when(gameRepository.save(any(Game.class))).thenReturn(gameUpdtDB);



        Game updt = gameService.updateGame(gameUpdt,1L);

        when(gameRepository.save(any(Game.class))).thenReturn(updt);

        assertThat(gameUpdt.getOpponent()).isEqualTo("Tým 1 updt");
    }
}
