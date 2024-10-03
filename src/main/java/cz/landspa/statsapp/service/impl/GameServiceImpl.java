package cz.landspa.statsapp.service.impl;

import cz.landspa.statsapp.model.DTO.game.GameRecord;
import cz.landspa.statsapp.model.DTO.game.GameScoresDTO;
import cz.landspa.statsapp.model.DTO.gameStats.GoalPeriodSum;
import cz.landspa.statsapp.model.DTO.gameStats.PenaltyPeriodSum;
import cz.landspa.statsapp.model.Game;
import cz.landspa.statsapp.model.Team;
import cz.landspa.statsapp.repository.EventRepository;
import cz.landspa.statsapp.repository.GameRepository;
import cz.landspa.statsapp.service.GameService;
import cz.landspa.statsapp.service.RosterService;
import cz.landspa.statsapp.util.SecurityUtil;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class GameServiceImpl implements GameService {


    private final RosterService rosterService;
    private final GameRepository gameRepository;
    private final EventRepository eventRepository;
    private final SecurityUtil securityUtil;

    public GameServiceImpl(GameRepository gameRepository, RosterService rosterService, EventRepository eventRepository, SecurityUtil securityUtil) {
        this.gameRepository = gameRepository;
        this.rosterService = rosterService;
        this.eventRepository = eventRepository;
        this.securityUtil = securityUtil;
    }

    @Override
    public Game createGame(Game game) {
        Game gameSaved = gameRepository.save(game);
        return gameSaved;
    }

    @Override
    public GameRecord getGameRecordInfo(Long id) {
        GameRecord gameRecord = new GameRecord();
        gameRecord.setGame(gameRepository.findById(id).orElse(null));
        gameRecord.setEvents(eventRepository.findAllByGameIdOrderByTimeAsc(id));
        return gameRecord;
    }

    @Override
    public List<Game> getTeamGames(Long teamId) {
        return gameRepository.findAllByTeamIdOrderByDateAscTimeAsc(teamId);
    }

    @Override
    public Long getNumberOfGamesByUser(Long userId) {
        return gameRepository.countByTeamOwnerId(userId);
    }

    @Override
    public List<Game> get4MostRecentGames(Long userId) {
        return gameRepository.findTop4ByTeamOwnerIdAndDateAfterOrderByDateAsc(userId, LocalDate.now());
    }

    @Override
    @PreAuthorize("@gameServiceImpl.isGameOwner(#id)")
    @Transactional
    public void deleteGame(Long id) {
        rosterService.deleteAllPlayersFromRoster(id);
        gameRepository.deleteById(id);
    }

    @Override
    @PreAuthorize("@gameServiceImpl.isGameOwner(#id)")
    public Game getGameById(Long id) {
        return gameRepository.findById(id).orElse(null);
    }

    @Override
    @PreAuthorize("@teamServiceImpl.isTeamOwner(#teamId)")
    public void deleteTeamGames(Long teamId) {
        rosterService.deleteAllTeamRosters(teamId);
        gameRepository.deleteAllByTeamId(teamId);
    }

    @Override
    @PreAuthorize("@gameServiceImpl.isGameOwner(#id)")
    public Game updateGame(Game game, Long id) {
        Game foundGame = gameRepository.findById(id).orElse(null);

        if(foundGame != null){
            if(game.getOpponent()!=null){
                foundGame.setOpponent(game.getOpponent());
            }
            if(game.getDate()!=null){
                foundGame.setDate(game.getDate());
            }
            if(game.getTime()!=null){
                foundGame.setTime(game.getTime());
            }
            if(game.getVenue()!=null){
                foundGame.setVenue(game.getVenue());
            }
            if(game.getTeam()!=null){
                foundGame.setTeam(game.getTeam());
            }


            return gameRepository.save(foundGame);
        } else {
            return null;
        }
    }

    @Override
    @PreAuthorize("@gameServiceImpl.isGameOwner(#id)")
    public void addPeriod(Long id) {
        Game game = gameRepository.findById(id).orElse(null);

        assert game != null;
        if(game.getCurrentPeriod()<=4){
        game.setCurrentPeriod(game.getCurrentPeriod()+1);

        gameRepository.save(game);}
    }

    @Override
    @PreAuthorize("@gameServiceImpl.isGameOwner(#id)")
    public void removePeriod(Long id) {
        Game game = gameRepository.findById(id).orElse(null);
        assert game != null;
        if(game.getCurrentPeriod()>1){
            game.setCurrentPeriod(game.getCurrentPeriod() - 1);
            gameRepository.save(game);
        }
    }

    @Override
    public List<GoalPeriodSum> getPeriodGoals(Long id) {
        Game game = gameRepository.findById(id).orElse(null);
        List<GoalPeriodSum> goals = new ArrayList<>();


        for(int i = 1; i<=game.getPeriods()+1; i++){
            Integer periodStart = (i-1) * game.getPeriodLength()*60;
            Integer periodEnd = (i) * game.getPeriodLength()*60;
            goals.add(gameRepository.getPeriodGoalSum(id, periodStart, periodEnd,i));
        }

        return goals;
    }

    @Override
    @PreAuthorize("@teamServiceImpl.isTeamOwner(#teamId)")
    public List<GameScoresDTO> getGamesWithScores(Long teamId) {
        return gameRepository.getGamesWithScores(teamId);
    }

    @Override
    public boolean isGameOwner(Long gameId) {
        Team team = Objects.requireNonNull(gameRepository.findById(gameId).orElse(null)).getTeam();

        if (team == null) {
            return false;
        }
        String currentUsername = securityUtil.getCurrentUsername();
        return team.getOwner().getUsername().equals(currentUsername);
    }
}
