package cz.landspa.statsapp.service.impl;

import cz.landspa.statsapp.model.Player;
import cz.landspa.statsapp.model.Team;
import cz.landspa.statsapp.repository.PlayerRepository;
import cz.landspa.statsapp.service.PlayerService;
import cz.landspa.statsapp.service.RosterService;
import cz.landspa.statsapp.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final RosterService rosterService;
    private final PlayerRepository playerRepository;
    private final SecurityUtil securityUtil;

    public PlayerServiceImpl(PlayerRepository playerRepository, RosterService rosterService, SecurityUtil securityUtil) {
        this.playerRepository = playerRepository;
        this.rosterService = rosterService;
        this.securityUtil = securityUtil;
    }

    @Override
    public Long getNumberOfPlayersByUser(Long userId) {
        return playerRepository.countByTeamOwnerId(userId);
    }

    @Override
    @PreAuthorize("@teamServiceImpl.isTeamOwner(#player.team.id)")
    public Player createPlayer(Player player) {
        return playerRepository.save(player);
    }

    @Override
    @PreAuthorize("@teamServiceImpl.isTeamOwner(#teamId)")
    public List<Player> getTeamPlayers(Long teamId) {
        return playerRepository.findAllByTeamIdOrderByPosition(teamId);
    }

    @Override
    @PreAuthorize("@playerServiceImpl.isPlayerOwner(#id)")
    public Player getPlayerById(Long id) {
        return playerRepository.findById(id).orElse(null);
    }

    @Override
    @PreAuthorize("@playerServiceImpl.isPlayerOwner(#id)")
    public void deletePlayer(Long id) {
        rosterService.deleteAllPlayerRosters(id);
        playerRepository.deleteById(id);
    }

    @Override
    @PreAuthorize("@teamServiceImpl.isTeamOwner(#teamId)")
    public void deleteTeamPlayers(Long teamId) {
        rosterService.deleteAllTeamRosters(teamId);
        playerRepository.deleteAllByTeamId(teamId);
    }

    @Override
    @PreAuthorize("@teamServiceImpl.isTeamOwner(#teamId)")
    public List<Player> findTeamPlayersNotInRoster(Long teamId, Long gameId) {
        return playerRepository.findPlayersNotInRosterForGame(teamId, gameId);
    }

    @Override
    @PreAuthorize("@playerServiceImpl.isPlayerOwner(#id)")
    public Player updatePlayer(Player player, Long id) {
        Player foundPlayer = playerRepository.findById(id).orElse(null);

        if(foundPlayer != null){
            if(player.getName()!=null){
                foundPlayer.setName(player.getName());
            }
            if(player.getSurname()!=null){
                foundPlayer.setSurname(player.getSurname());
            }

                foundPlayer.setNumber(player.getNumber());

                foundPlayer.setPosition(player.getPosition());

            if(player.getTeam()!=null){
                foundPlayer.setTeam(player.getTeam());
            }



            return playerRepository.save(foundPlayer);
        } else {
            return null;
        }
    }

    @Override
    public boolean isPlayerOwner(Long playerId) {
        Team team = Objects.requireNonNull(playerRepository.findById(playerId).orElse(null)).getTeam();
        if (team == null) {
            return false;
        }
        String currentUsername = securityUtil.getCurrentUsername();
        return team.getOwner().getUsername().equals(currentUsername);
    }
}
