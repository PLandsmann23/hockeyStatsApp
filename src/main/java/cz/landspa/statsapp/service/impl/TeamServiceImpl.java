package cz.landspa.statsapp.service.impl;

import cz.landspa.statsapp.model.DTO.team.TeamCountInfo;
import cz.landspa.statsapp.model.DTO.team.TeamPlayersGames;
import cz.landspa.statsapp.model.Team;
import cz.landspa.statsapp.model.User;
import cz.landspa.statsapp.repository.GameRepository;
import cz.landspa.statsapp.repository.PlayerRepository;
import cz.landspa.statsapp.repository.TeamRepository;
import cz.landspa.statsapp.service.GameService;
import cz.landspa.statsapp.service.PlayerService;
import cz.landspa.statsapp.service.TeamService;
import cz.landspa.statsapp.util.SecurityUtil;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {

    private final PlayerRepository playerRepository;
    private final GameRepository gameRepository;
    private final SecurityUtil securityUtil;
    GameService gameService;
    PlayerService playerService;
    TeamRepository teamRepository;



    public TeamServiceImpl(TeamRepository teamRepository, GameService gameService, PlayerService playerService, PlayerRepository playerRepository, GameRepository gameRepository, SecurityUtil securityUtil) {
        this.teamRepository = teamRepository;
        this.gameService = gameService;
        this.playerService = playerService;
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
        this.securityUtil = securityUtil;
    }

    @Override
    public List<TeamCountInfo> getTeamInfo(Long userId) {


        return teamRepository.findTeamsWithPlayerCountAndGameCountByUserId(userId);
    }

    @Override
    @PreAuthorize("@teamServiceImpl.isTeamOwner(#teamId)")
    public TeamPlayersGames getOneTeamInfo(Long teamId) {
        TeamPlayersGames teamInfo = new TeamPlayersGames();
        teamInfo.setTeam(teamRepository.findById(teamId).orElse(null));
        teamInfo.setPlayers(playerRepository.findAllByTeamIdOrderByPosition(teamId));
        teamInfo.setGames(gameRepository.getGamesWithScores(teamId));
        return teamInfo;
    }

    @Override
    public Long getNumberOfTeamsByUser(Long userId) {
        return teamRepository.countByOwnerId(userId);
    }

    @Override
    public Team createTeam(Team team) {
        return teamRepository.save(team);
    }

    @Override
    public List<Team> getUserTeams(User user) {
        return teamRepository.findAllByOwner(user);
    }

    @Override
    public Team getTeamById(Long id) {
        return teamRepository.findById(id).orElse(null);
    }

    @Override
    @PreAuthorize("@teamServiceImpl.isTeamOwner(#id)")
    @Transactional
    public void deleteTeam(Long id) {
        playerService.deleteTeamPlayers(id);
        gameService.deleteTeamGames(id);
        teamRepository.deleteById(id);
    }

    @Override
    @PreAuthorize("@teamServiceImpl.isTeamOwner(#id)")
    public Team updateTeam(Team team, Long id) {
        Team foundTeam = teamRepository.findById(id).orElse(null);

        if(foundTeam != null){
            if(team.getName()!=null){
                foundTeam.setName(team.getName());
            }
            if(team.getOwner()!=null){
                foundTeam.setOwner(team.getOwner());
            }

            return teamRepository.save(team);
        } else {
            return null;
        }

    }

    @Override
    public boolean isTeamOwner(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElse(null);
        if (team == null) {
            return false;
        }
        String currentUsername = securityUtil.getCurrentUsername();
        return team.getOwner().getUsername().equals(currentUsername);
    }
}
