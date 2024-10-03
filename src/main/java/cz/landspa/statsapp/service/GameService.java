package cz.landspa.statsapp.service;

import cz.landspa.statsapp.model.DTO.game.GameRecord;
import cz.landspa.statsapp.model.DTO.game.GameScoresDTO;
import cz.landspa.statsapp.model.DTO.gameStats.GoalPeriodSum;
import cz.landspa.statsapp.model.Game;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GameService {
    Game createGame(Game game);

    List<Game> getTeamGames(Long teamId);

    Game getGameById(Long id);

    void deleteGame(Long id);

    void deleteTeamGames(Long teamId);

    Game updateGame(Game game, Long id);

    Long getNumberOfGamesByUser(Long userId);

    List<Game> get4MostRecentGames(Long userId);

    GameRecord getGameRecordInfo(Long id);

    void addPeriod(Long id);

    void removePeriod(Long id);

    List<GameScoresDTO> getGamesWithScores(Long teamId);

    List<GoalPeriodSum> getPeriodGoals(Long id);

    boolean isGameOwner(Long eventId);
}
