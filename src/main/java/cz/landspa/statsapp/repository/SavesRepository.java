package cz.landspa.statsapp.repository;


import cz.landspa.statsapp.model.DTO.saves.PeriodSaves;
import cz.landspa.statsapp.model.Saves;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SavesRepository extends JpaRepository<Saves, Long> {

    Saves getSavesByGameIdAndGoalkeeperIdAndPeriod(Long gameId, Long goalkeeperId, Integer period);


    List<Saves> getSavesByGameIdAndGoalkeeperId(Long gameId, Long goalkeeperId);

    List<Saves> findAllByGameId(Long gameId);


    @Query("""
    SELECT new cz.landspa.statsapp.model.DTO.saves.PeriodSaves(
    s.period,
    s.saves
    ) FROM Saves s
    WHERE s.game.id = :gameId
    AND s.goalkeeper.id = :goalkeeperId
    ORDER BY s.period
""")
    List<PeriodSaves> getPeriodSaves(Long gameId, Long goalkeeperId);

}
