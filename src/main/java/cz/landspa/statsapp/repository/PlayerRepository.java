package cz.landspa.statsapp.repository;

import com.sun.jdi.LongValue;
import cz.landspa.statsapp.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    List<Player> findAllByTeamIdOrderByPosition(Long teamId);

    @Query("SELECT p FROM Player p WHERE p.team.id = :teamId AND p.id NOT IN (SELECT r.player.id FROM Roster r WHERE r.game.id = :gameId)")
    List<Player> findPlayersNotInRosterForGame(@Param("teamId") Long teamId, @Param("gameId") Long gameId);


    void deleteAllByTeamId(Long teamId);

    Long countByTeamOwnerId(Long userId);
}
