package cz.landspa.statsapp.repository;

import cz.landspa.statsapp.model.GoalScored;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GoalScoredRepository extends JpaRepository<GoalScored, Long> {

    List<GoalScored> findAllByGameId(Long gameId);


    @Modifying
    @Transactional
    @Query("UPDATE GoalScored g SET g.scorer = null WHERE g.scorer.player.id = :playerId")
    void updateScorerToNull(Long playerId);

    @Modifying
    @Query("DELETE FROM GoalScored g WHERE EXISTS (SELECT 1 FROM g.assists o WHERE o.player.id = :playerId)")
    void removeAssistsByPlayerId(Long playerId);

    @Modifying
    @Transactional
    @Query("DELETE FROM GoalScored g WHERE EXISTS (SELECT 1 FROM g.onIce o WHERE o.player.id = :playerId)")
    void removeParticipationByPlayerId(Long playerId);
}
