package cz.landspa.statsapp.service;

import cz.landspa.statsapp.model.DTO.GoalkeeperChange;
import cz.landspa.statsapp.model.Roster;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RosterService {
    Roster addPlayerToRoster(Roster roster);

    List<Roster> addPlayersToRoster(List<Roster> rosters);

    List<Roster> getRosterForGame(Long gameId);

    void deletePlayerFromRoster(Long id);

    void deleteAllPlayersFromRoster(Long gameId);

    void deleteAllTeamRosters(Long teamId);

    void deleteAllPlayerRosters(Long playerId);

    Roster updateRoster(Roster roster, Long id);

    GoalkeeperChange changeActiveGoalkeeper(Long gameId);

    Roster getRosterById(Long id);

    boolean isRosterOwner(Long rosterId);

    boolean isGamePlayerOwner(Long gameId, Long playerId);
}
