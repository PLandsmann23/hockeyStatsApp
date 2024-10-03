package cz.landspa.statsapp.service;

import cz.landspa.statsapp.model.Player;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PlayerService {

    Player createPlayer(Player player);

    List<Player> getTeamPlayers(Long teamId);

    Player getPlayerById(Long id);

    void deletePlayer(Long id);

    void deleteTeamPlayers(Long teamId);

    Player updatePlayer(Player player, Long id);


    Long getNumberOfPlayersByUser(Long userId);

    List<Player> findTeamPlayersNotInRoster(Long teamId, Long gameId);

    boolean isPlayerOwner(Long playerId);
}
