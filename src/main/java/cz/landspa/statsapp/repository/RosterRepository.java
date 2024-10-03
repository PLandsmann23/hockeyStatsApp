package cz.landspa.statsapp.repository;

import cz.landspa.statsapp.model.Position;
import cz.landspa.statsapp.model.Roster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import static cz.landspa.statsapp.model.Position.GK;

public interface RosterRepository extends JpaRepository<Roster, Long> {

    List<Roster> findAllByGameIdOrderByGameNumber(Long gameId);

    void deleteAllByGameId(Long gameId);

    void deleteAllByPlayerTeamId(Long teamId);

    void deleteAllByPlayerId(Long playerId);

    Long countAllByGameIdAndPlayerPosition(Long gameId, Position position);

    Roster getRosterByPlayerPositionAndGameIdAndActiveGkTrue(Position position, Long gameId);

    Roster getRosterByPlayerPositionAndGameIdAndActiveGkFalse(Position position, Long gameId);
}
