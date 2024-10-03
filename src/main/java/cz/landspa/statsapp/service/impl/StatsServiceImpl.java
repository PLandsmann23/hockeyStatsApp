package cz.landspa.statsapp.service.impl;

import cz.landspa.statsapp.model.DTO.stats.StatsDTO;
import cz.landspa.statsapp.model.DTO.stats.TeamStats;
import cz.landspa.statsapp.model.Game;
import cz.landspa.statsapp.repository.StatsRepository;
import cz.landspa.statsapp.service.StatsService;
import cz.landspa.statsapp.util.Util;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    public StatsServiceImpl(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    @Override
    public StatsDTO getTeamStatsSum(Long teamId) {

        List<Long> games = statsRepository.getSeasonGamesForStats(teamId, Util.getSeasonStart());
        return new StatsDTO(statsRepository.getTeamStatsForGames(games), statsRepository.getPlayerStatsForGames(teamId,games),statsRepository.getGoalieStatsForGames(teamId, games));
    }

    @Override
    public StatsDTO getTeamStatsSumLast5(Long teamId) {
        Pageable pageable = PageRequest.of(0,5);
        List<Long> games = statsRepository.findLastFiveGames(teamId, pageable);
        return new StatsDTO(statsRepository.getTeamStatsForGames(games), statsRepository.getPlayerStatsForGames(teamId,games),statsRepository.getGoalieStatsForGames(teamId, games));
    }
}
