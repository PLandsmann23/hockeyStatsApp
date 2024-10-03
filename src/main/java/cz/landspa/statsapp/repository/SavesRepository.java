package cz.landspa.statsapp.repository;

import cz.landspa.statsapp.model.Penalty;
import cz.landspa.statsapp.model.Saves;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SavesRepository extends JpaRepository<Saves, Long> {

    Saves getSavesByGameIdAndGoalkeeperIdAndPeriod(Long gameId, Long goalkeeperId, Integer period);

    List<Saves> getSavesByGameIdAndGoalkeeperId(Long gameId, Long goalkeeperId);

    List<Saves> findAllByGameId(Long gameId);
}
