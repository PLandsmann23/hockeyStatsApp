package cz.landspa.statsapp.service;

import cz.landspa.statsapp.model.DTO.team.TeamCountInfo;
import cz.landspa.statsapp.model.DTO.team.TeamPlayersGames;
import cz.landspa.statsapp.model.Team;
import cz.landspa.statsapp.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TeamService {
    Team createTeam(Team team);

    List<Team> getUserTeams(User user);

    Team getTeamById(Long id);

    void deleteTeam (Long id);

    Team updateTeam(Team team, Long id);

    Long getNumberOfTeamsByUser(Long userId);

    List<TeamCountInfo> getTeamInfo(Long userId);


    TeamPlayersGames getOneTeamInfo(Long teamId);

    boolean isTeamOwner(Long teamId);
}
