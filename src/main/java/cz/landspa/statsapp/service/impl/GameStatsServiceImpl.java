package cz.landspa.statsapp.service.impl;

import cz.landspa.statsapp.model.DTO.game.GameStats;
import cz.landspa.statsapp.model.DTO.gameStats.*;
import cz.landspa.statsapp.model.Game;
import cz.landspa.statsapp.repository.EventRepository;
import cz.landspa.statsapp.repository.GameRepository;
import cz.landspa.statsapp.service.GameStatsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameStatsServiceImpl implements GameStatsService {
    private final EventRepository eventRepository;
    private final GameRepository gameRepository;

    public GameStatsServiceImpl(EventRepository eventRepository, GameRepository gameRepository) {
        this.eventRepository = eventRepository;
        this.gameRepository = gameRepository;
    }

    @Override
    @PreAuthorize("@gameServiceImpl.isGameOwner(#gameId)")
    public List<PlayerStat> getGamePlayerStats(Long gameId) {
        return eventRepository.findPlayerStatsByGameId(gameId);


    }

    @Override
    @PreAuthorize("@gameServiceImpl.isGameOwner(#gameId)")
    public List<GoalieStat> getGameGoalieStats(Long gameId) {
        return eventRepository.findGoalieStatsByGameId(gameId);
    }

    @Override
    @PreAuthorize("@gameServiceImpl.isGameOwner(#gameId)")
    public GameStats getGameStats(Long gameId) {
        Game game = gameRepository.findById(gameId).orElse(null);
        GameStats gameStats = eventRepository.getGameStats(gameId);
        List<ShotPeriodSum> shots = new ArrayList<>();
        List<SavePeriodSum> saves = new ArrayList<>();
        List<GoalPeriodSum> goals = new ArrayList<>();
        List<PenaltyPeriodSum> penalties = new ArrayList<>();

        for(int i = 1; i<=game.getPeriods()+1; i++){
            Integer periodStart = (i-1) * game.getPeriodLength()*60;
            Integer periodEnd = (i) * game.getPeriodLength()*60;
            shots.add(gameRepository.getPeriodShotSum(gameId,periodStart, periodEnd, i));
            saves.add(gameRepository.getPeriodSaveSum(gameId,periodStart, periodEnd, i));
            goals.add(gameRepository.getPeriodGoalSum(gameId, periodStart, periodEnd,i));
            penalties.add(gameRepository.getPeriodPenaltySum(gameId, periodStart, periodEnd,i));
        }
        gameStats.setShots(shots);
        gameStats.setSaves(saves);
        gameStats.setGoals(goals);
        gameStats.setPenalties(penalties);

        return gameStats;
    }
}
