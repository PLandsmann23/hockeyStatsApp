package cz.landspa.statsapp.service;

import cz.landspa.statsapp.model.DTO.stats.StatsDTO;
import cz.landspa.statsapp.model.DTO.stats.TeamStats;
import org.springframework.stereotype.Service;

@Service
public interface StatsService {

    StatsDTO getTeamStatsSum (Long teamId);

    StatsDTO getTeamStatsSumLast5 (Long teamId);
}
