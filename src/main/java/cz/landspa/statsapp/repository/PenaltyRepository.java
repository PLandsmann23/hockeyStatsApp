package cz.landspa.statsapp.repository;

import cz.landspa.statsapp.model.Penalty;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PenaltyRepository extends JpaRepository<Penalty, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Penalty p SET p.player = null WHERE p.player.player.id = :playerId")
    void updatePenaltyPlayerToNull(Long playerId);
}
