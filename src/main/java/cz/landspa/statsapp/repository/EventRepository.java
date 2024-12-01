package cz.landspa.statsapp.repository;

import cz.landspa.statsapp.model.DTO.game.GameStats;
import cz.landspa.statsapp.model.DTO.gameStats.GoalieStat;
import cz.landspa.statsapp.model.DTO.gameStats.PlayerStat;
import cz.landspa.statsapp.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByGameIdOrderByTimeAsc(Long id);

    @Query("select count (g) from GoalScored g where g.game.id = :gameId")
    Long scoredGoalsInGame (@Param("gameId")Long gameId);

    @Query("select count (g) from GoalConceded g where g.game.id = :gameId")
    Long concededGoalsInGame (@Param("gameId")Long gameId);


    @SuppressWarnings("JpaQlInspection")
    @Query("""
    SELECT new cz.landspa.statsapp.model.DTO.gameStats.PlayerStat(
        r,
        COUNT( gs.id),
        (SELECT COUNT( a.id) FROM GoalScored gs2 JOIN gs2.assists a WHERE a.id = r.id AND gs2.game.id = :gameId),
        (SELECT COUNT( goi.id) FROM GoalScored gs3 JOIN gs3.onIce goi WHERE goi.id = r.id AND gs3.game.id = :gameId),
        (SELECT COUNT( gcoi.id) FROM GoalConceded gc2 JOIN gc2.onIce gcoi WHERE gcoi.id = r.id AND gc2.game.id = :gameId),
         (SELECT SUM(p.minutes) FROM Penalty p  WHERE p.player.id = r.id AND p.game.id = :gameId)

    )
    FROM Roster r
    LEFT JOIN GoalScored gs ON gs.scorer.id = r.id
    WHERE r.game.id = :gameId
        AND r.player.position <> "GK"
    GROUP BY r
""")
    List<PlayerStat> findPlayerStatsByGameId(@Param("gameId") Long gameId);

    @Query("""
    SELECT new cz.landspa.statsapp.model.DTO.gameStats.GoalieStat(
        r,
        (SELECT SUM(s.saves) FROM Saves s WHERE s.goalkeeper.id = r.id),
        (SELECT s.saves FROM Saves s WHERE s.period = 1 AND s.goalkeeper.id = r.id),
        (SELECT s.saves FROM Saves s WHERE s.period = 2 AND s.goalkeeper.id = r.id),
        (SELECT s.saves FROM Saves s WHERE s.period = 3 AND s.goalkeeper.id = r.id),
        (SELECT s.saves FROM Saves s WHERE s.period = 4 AND s.goalkeeper.id = r.id),
        COUNT(DISTINCT gc.id)
    )
    FROM Roster r
    LEFT JOIN Saves s ON s.goalkeeper.id = r.id
    LEFT JOIN GoalConceded gc ON gc.inGoal.id = r.id
    WHERE r.game.id = :gameId
        AND r.player.position = "GK"
    GROUP BY r
    ORDER BY r.id
""")
    List<GoalieStat> findGoalieStatsByGameId(@Param("gameId") Long gameId);

    @SuppressWarnings("JpaQlInspection")
    @Query("""
    SELECT new cz.landspa.statsapp.model.DTO.game.GameStats(
        g,
        COUNT(DISTINCT gs.id),
        COUNT(DISTINCT gc.id),
        (SELECT SUM(s.shots) FROM Shot s WHERE s.game.id = g.id),
        (SELECT SUM(sa.saves) FROM Saves sa WHERE sa.game.id = g.id),
        (SELECT SUM(p.minutes) FROM Penalty p WHERE p.game.id = g.id),
        (SELECT SUM(op.minutes) FROM OpponentPenalty op WHERE op.game.id = g.id),
        (SELECT COUNT (p.id) FROM Penalty p WHERE p.game.id = g.id AND p.minutes IN (2,5,25,12) AND p.coincidental=false) + 
        ((SELECT COUNT (p2.id) FROM Penalty p2 WHERE p2.game.id = g.id AND p2.minutes IN (4) AND p2.coincidental=false) * 2),   
        (SELECT COUNT (op.id) FROM OpponentPenalty op WHERE op.game.id = g.id AND op.minutes IN (2,5,25,12) AND op.coincidental=false) + 
        (SELECT COUNT (op.id) FROM OpponentPenalty op WHERE op.game.id = g.id AND op.minutes IN (4) AND op.coincidental=false) * 2,   
        (SELECT COUNT(gs3.id) FROM GoalScored gs3 WHERE gs3.game.id = :gameId AND gs3.situation IN ("5/4","5/3","4/3")),
        (SELECT COUNT(gc3.id) FROM GoalConceded gc3 WHERE gc3.game.id = :gameId AND gc3.situation IN ("4/5","3/5","3/4")),
        (SELECT COUNT(gs3.id) FROM GoalScored gs3 WHERE EXISTS (
            SELECT 1 FROM OpponentPenalty p2 WHERE p2.game.id = g.id AND 
            p2.time <= gs3.time AND (p2.minutes = 5 OR p2.minutes = 25) AND 
            p2.time + 300 >= gs3.time
        ) AND gs3.situation IN ("5/4","5/3","4/3")),
        (SELECT COUNT(gc3.id) FROM GoalConceded gc3 WHERE EXISTS (
            SELECT 1 FROM Penalty p2 WHERE p2.game.id = g.id AND 
            p2.time <= gc3.time AND (p2.minutes = 5 OR p2.minutes = 25) AND 
            p2.time + 300 >= gc3.time
        ) AND gc3.situation IN ("4/5","3/5","3/4"))
    )
    FROM Game g
    LEFT JOIN GoalScored gs ON gs.game.id = g.id
    LEFT JOIN GoalConceded gc ON gc.game.id = g.id
    LEFT JOIN Shot s ON s.game.id = g.id
    LEFT JOIN Saves sa ON sa.game.id = g.id
    LEFT JOIN Penalty p ON p.game.id = g.id
    LEFT JOIN OpponentPenalty op ON op.game.id = g.id
    WHERE g.id = :gameId
""")
    GameStats getGameStats(@Param("gameId") Long gameId);



}
