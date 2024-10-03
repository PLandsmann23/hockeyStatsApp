package cz.landspa.statsapp.repository;

import cz.landspa.statsapp.model.GoalConceded;
import cz.landspa.statsapp.model.GoalScored;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GoalConcededRepository extends JpaRepository<GoalConceded, Long> {

    List<GoalConceded> findAllByGameId(Long gameId);

    @Modifying
    @Transactional
    @Query("DELETE FROM GoalConceded g WHERE EXISTS (SELECT 1 FROM g.onIce o WHERE o.player.id = :playerId)")
    void removeParticipationByPlayerId(Long playerId);
}
