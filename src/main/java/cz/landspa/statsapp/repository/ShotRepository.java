package cz.landspa.statsapp.repository;

import cz.landspa.statsapp.model.Shot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShotRepository extends JpaRepository<Shot, Long> {

    Shot getShotByGameIdAndPeriod(Long gameId, Integer period);

    List<Shot> findAllByGameId(Long gameId);
}
