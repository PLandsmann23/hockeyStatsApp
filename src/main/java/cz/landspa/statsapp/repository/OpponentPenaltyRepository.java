package cz.landspa.statsapp.repository;

import cz.landspa.statsapp.model.OpponentPenalty;
import cz.landspa.statsapp.model.Penalty;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface OpponentPenaltyRepository extends JpaRepository<OpponentPenalty, Long> {

}
