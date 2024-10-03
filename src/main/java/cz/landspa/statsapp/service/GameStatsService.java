package cz.landspa.statsapp.service;

import cz.landspa.statsapp.model.DTO.game.GameStats;
import cz.landspa.statsapp.model.DTO.gameStats.GoalieStat;
import cz.landspa.statsapp.model.DTO.gameStats.PlayerStat;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GameStatsService {
    List<PlayerStat> getGamePlayerStats(Long gameId);

    List<GoalieStat> getGameGoalieStats(Long gameId);

    GameStats getGameStats(Long gameId);
}
