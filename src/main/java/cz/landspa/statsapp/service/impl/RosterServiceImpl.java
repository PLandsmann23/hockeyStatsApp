package cz.landspa.statsapp.service.impl;

import cz.landspa.statsapp.model.*;
import cz.landspa.statsapp.model.DTO.GoalkeeperChange;
import cz.landspa.statsapp.repository.GameRepository;
import cz.landspa.statsapp.repository.PlayerRepository;
import cz.landspa.statsapp.repository.RosterRepository;
import cz.landspa.statsapp.repository.TeamRepository;
import cz.landspa.statsapp.service.PlayerService;
import cz.landspa.statsapp.service.RosterService;
import cz.landspa.statsapp.service.TeamService;
import cz.landspa.statsapp.util.SecurityUtil;
import jakarta.transaction.Transactional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class RosterServiceImpl implements RosterService {




    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final GameRepository gameRepository;
    private final RosterRepository rosterRepository;
    private final SecurityUtil securityUtil;

    public RosterServiceImpl(RosterRepository rosterRepository, PlayerRepository playerRepository, TeamRepository teamRepository, GameRepository gameRepository, SecurityUtil securityUtil) {
        this.rosterRepository = rosterRepository;

        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.gameRepository = gameRepository;
        this.securityUtil = securityUtil;
    }



    @Override
    @PreAuthorize("@rosterServiceImpl.isGamePlayerOwner(#roster.game.id, #roster.player.id )")
    public Roster addPlayerToRoster(Roster roster) {
        Game game = gameRepository.findById(roster.getGame().getId()).orElse(null);
        Player player = playerRepository.findById(roster.getPlayer().getId()).orElse(null);
        Team team = teamRepository.findById(game.getTeam().getId()).orElse(null);
        if(roster.getGameNumber()==null){
            roster.setGameNumber(player.getNumber());
        }


        if(!player.getTeam().equals(team)){
            throw new IllegalArgumentException("Tento hráč není členem týmu");
        }

        Position gk = Position.GK;
        if(rosterRepository.countAllByGameIdAndPlayerPosition(game.getId(),gk)==2L && roster.getPlayer().getPosition() == gk){
            throw new IllegalArgumentException("V soupisce se již nachází dva brankáři");
        }

        return rosterRepository.save(roster);
    }

    @Override
    @Transactional
    public List<Roster> addPlayersToRoster(List<Roster> rosters) {
        List<Roster> savedRoster = new ArrayList<>();

        return rosterRepository.saveAll(savedRoster);
    }

    @Override
    @PreAuthorize("@gameServiceImpl.isGameOwner(#gameId)")
    public List<Roster> getRosterForGame(Long gameId) {
        return rosterRepository.findAllByGameIdOrderByGameNumber(gameId);
    }

    @Override
    @PreAuthorize("@rosterServiceImpl.isRosterOwner(#id)")
    public void deletePlayerFromRoster(Long id) {
        rosterRepository.deleteById(id);
    }

    @Override
    @PreAuthorize("@gameServiceImpl.isGameOwner(#gameId)")
    public void deleteAllPlayersFromRoster(Long gameId) {
        rosterRepository.deleteAllByGameId(gameId);
    }

    @Override
    @PreAuthorize("@teamServiceImpl.isTeamOwner(#teamId)")
    public void deleteAllTeamRosters(Long teamId) {
        rosterRepository.deleteAllByPlayerTeamId(teamId);
    }

    @Override
    @PreAuthorize("@playerServiceImpl.isPlayerOwner(#playerId)")
    public void deleteAllPlayerRosters(Long playerId) {
        rosterRepository.deleteAllByPlayerId(playerId);
    }

    @Override
    @PreAuthorize("@rosterServiceImpl.isRosterOwner(#id)")
    public Roster updateRoster(Roster roster, Long id) {
        Roster foundRosterPlayer = rosterRepository.findById(id).orElse(null);

        if(foundRosterPlayer != null){
            if(roster.getPlayer()!=null){
                foundRosterPlayer.setPlayer(roster.getPlayer());
            }
            if(roster.getGame()!=null){
                foundRosterPlayer.setGame(roster.getGame());
            }
            if(roster.getLine()!=null){
                foundRosterPlayer.setLine(roster.getLine());
            }
            if(roster.getGameNumber()!=null){
                foundRosterPlayer.setGameNumber(roster.getGameNumber());
            }
            foundRosterPlayer.setActiveGk(roster.isActiveGk());




            return rosterRepository.save(foundRosterPlayer);
        } else {
            return null;
        }
    }

    @Override
    @PreAuthorize("@gameServiceImpl.isGameOwner(#gameId)")
    public GoalkeeperChange changeActiveGoalkeeper(Long gameId) {
        Roster leaving = rosterRepository.getRosterByPlayerPositionAndGameIdAndActiveGkTrue(Position.GK, gameId);
        Roster coming = rosterRepository.getRosterByPlayerPositionAndGameIdAndActiveGkFalse(Position.GK, gameId);
        leaving.setActiveGk(false);
        coming.setActiveGk(true);

        GoalkeeperChange goalkeeperChange = new GoalkeeperChange();
        goalkeeperChange.setLeaving(rosterRepository.save(leaving));
        goalkeeperChange.setComing(rosterRepository.save(coming));
        return goalkeeperChange;
    }

    @Override
    @PreAuthorize("@rosterServiceImpl.isRosterOwner(#id)")
    public Roster getRosterById(Long id) {
        return rosterRepository.findById(id).orElse(null);
    }

    public boolean isRosterOwner(Long rosterId) {
        Team team = Objects.requireNonNull(rosterRepository.findById(rosterId).orElse(null)).getGame().getTeam();
        if (team == null) {
            return false;
        }
        String currentUsername = securityUtil.getCurrentUsername();
        return team.getOwner().getUsername().equals(currentUsername);
    }

    @Override
    public boolean isGamePlayerOwner(Long gameId, Long playerId) {
        Team team = Objects.requireNonNull(gameRepository.findById(gameId).orElse(null)).getTeam();
        Team team2 = Objects.requireNonNull(playerRepository.findById(playerId).orElse(null)).getTeam();
        if (team == null || team2 == null || team != team2) {
            return false;
        }
        String currentUsername = securityUtil.getCurrentUsername();
        return team.getOwner().getUsername().equals(currentUsername);
    }
}
