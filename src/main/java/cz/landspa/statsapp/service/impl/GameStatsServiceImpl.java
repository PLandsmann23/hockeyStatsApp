package cz.landspa.statsapp.service.impl;

import cz.landspa.statsapp.model.DTO.game.GameStats;
import cz.landspa.statsapp.model.DTO.gameStats.*;
import cz.landspa.statsapp.model.DTO.saves.PeriodSaves;
import cz.landspa.statsapp.model.DTO.stats.GoalieStats;
import cz.landspa.statsapp.model.Game;
import cz.landspa.statsapp.model.Saves;
import cz.landspa.statsapp.repository.EventRepository;
import cz.landspa.statsapp.repository.GameRepository;
import cz.landspa.statsapp.repository.SavesRepository;
import cz.landspa.statsapp.service.GameStatsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameStatsServiceImpl implements GameStatsService {
    private final EventRepository eventRepository;
    private final GameRepository gameRepository;
    private final SavesRepository savesRepository;

    public GameStatsServiceImpl(EventRepository eventRepository, GameRepository gameRepository, SavesRepository savesRepository) {
        this.eventRepository = eventRepository;
        this.gameRepository = gameRepository;
        this.savesRepository = savesRepository;
    }

    @Override
    @PreAuthorize("@gameServiceImpl.isGameOwner(#gameId)")
    public List<PlayerStat> getGamePlayerStats(Long gameId) {
        return eventRepository.findPlayerStatsByGameId(gameId);


    }

    @Override
    @PreAuthorize("@gameServiceImpl.isGameOwner(#gameId)")
    public List<GoalieStat> getGameGoalieStats(Long gameId) {

        Game game = gameRepository.findById(gameId).orElse(null);

        List<GoalieStat> goalieStats =  eventRepository.findGoalieStatsByGameId(gameId);

        for(GoalieStat stat : goalieStats){
            List<PeriodSaves> dbsaves = savesRepository.getPeriodSaves(gameId, stat.getPlayer().getId());
            List<PeriodSaves> saves = new ArrayList<>();


            if (game != null) {
                for(int i = 0; i<game.getPeriods()+1; i++){
                    PeriodSaves found = null;
                    for(PeriodSaves save : dbsaves){
                        if(save.getPeriod() == i+1){
                            found = save;
                            break;
                        }
                    }

                    saves.add(found!=null?found:new PeriodSaves(i+1, null));

                }
            }


            stat.setPeriodSaves(saves);
        }

        return goalieStats;
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
