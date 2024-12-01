package cz.landspa.statsapp.repository;

import cz.landspa.statsapp.model.DTO.stats.GoalieStats;
import cz.landspa.statsapp.model.DTO.stats.PlayerStats;
import cz.landspa.statsapp.model.DTO.stats.TeamStats;
import cz.landspa.statsapp.model.Event;
import cz.landspa.statsapp.model.Game;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface StatsRepository extends JpaRepository<Event, Long> {


    @Query("""
    SELECT new cz.landspa.statsapp.model.DTO.stats.TeamStats(
        (SELECT COUNT(DISTINCT gs3.id) FROM GoalScored gs3 WHERE gs3.game.id IN :gameIds AND gs3.situation IN ('5/4','5/3','4/3')),
        null,
        (SELECT COUNT(DISTINCT op.id) FROM OpponentPenalty op WHERE op.game.id IN :gameIds AND op.minutes IN (2,5,25,12) AND op.coincidental=false ) + 
        ((SELECT COUNT(op2.id) FROM OpponentPenalty op2 WHERE op2.game.id IN :gameIds AND op2.minutes IN (4) AND op2.coincidental=false) * 2),
        (SELECT COUNT(DISTINCT gc3.id) FROM GoalConceded gc3 WHERE gc3.game.id IN :gameIds AND gc3.situation IN ('4/5','3/5','3/4')),
        null,
        (SELECT COUNT(DISTINCT p.id) FROM Penalty p WHERE p.game.id IN :gameIds AND p.minutes IN (2,5,25,12) AND p.coincidental=false) + 
        ((SELECT COUNT(p2.id) FROM Penalty p2 WHERE p2.game.id IN :gameIds AND p2.minutes IN (4) AND p2.coincidental=false) * 2),
        (SELECT SUM(s.shots) FROM Shot s WHERE s.game.id IN :gameIds),
        (SELECT SUM(sa.saves) FROM Saves sa WHERE sa.game.id IN :gameIds),
        (SELECT COUNT(DISTINCT g.id) FROM Game g WHERE g.id IN :gameIds),
        (SELECT COUNT(DISTINCT gs.id) FROM GoalScored gs WHERE gs.game.id IN :gameIds),
        (SELECT COUNT(DISTINCT gc.id) FROM GoalConceded gc WHERE gc.game.id IN :gameIds),
        (SELECT COUNT(DISTINCT gc.id) FROM GoalConceded gc WHERE gc.game.id IN :gameIds AND gc.inGoal IS NOT null)
    )
""")
    TeamStats getTeamStatsForGames(@Param("gameIds") List<Long> gameIds);



    @Query("""
    SELECT new cz.landspa.statsapp.model.DTO.stats.PlayerStats(
        p,
        (SELECT COUNT(DISTINCT r.id) FROM Roster r WHERE r.game.id IN :gameIds AND r.player = p),
        (SELECT COUNT(DISTINCT gs.id) FROM GoalScored gs WHERE gs.game.id IN :gameIds AND gs.scorer.player = p),
        (SELECT COUNT(DISTINCT a.id) FROM GoalScored gs2 JOIN gs2.assists a WHERE a.player.id = p.id AND gs2.game.id IN :gameIds),
        (SELECT COUNT( goi.id) FROM GoalScored gs3 JOIN gs3.onIce goi WHERE goi.player.id = p.id AND gs3.game.id IN :gameIds),
        (SELECT COUNT( gcoi.id) FROM GoalConceded gc2 JOIN gc2.onIce gcoi WHERE gcoi.player.id = p.id AND gc2.game.id IN :gameIds),
         (SELECT SUM(pen.minutes) FROM Penalty pen  WHERE pen.player.player.id = p.id AND pen.game.id IN :gameIds)
        
    )
    FROM Player p
    WHERE p.team.id = :teamId
    AND p.position <> 'GK'
""")
    List<PlayerStats> getPlayerStatsForGames(@Param("teamId") Long teamId, @Param("gameIds")List<Long> gameIds);


    @Query("""
    SELECT new cz.landspa.statsapp.model.DTO.stats.GoalieStats(
        p,
        (SELECT COUNT(DISTINCT s.game.id) FROM Saves s WHERE s.game.id IN :gameIds AND s.goalkeeper.player = p),
        (SELECT COUNT(DISTINCT gc.id) FROM GoalConceded gc WHERE gc.game.id IN :gameIds AND gc.inGoal.player = p),
        (SELECT SUM(s2.saves) FROM Saves s2 WHERE s2.game.id IN :gameIds AND  s2.goalkeeper.player = p) 
    )
    FROM Player p
    WHERE p.team.id = :teamId
    AND p.position = 'GK'
""")
    List<GoalieStats> getGoalieStatsForGames(@Param("teamId") Long teamId, @Param("gameIds")List<Long> gameIds);


    @Query("""
    SELECT g.id FROM Game g 
    WHERE g.team.id = :teamId 
    AND g.date BETWEEN :seasonStart AND CURRENT_DATE
""")
    List<Long> getSeasonGamesForStats(@Param("teamId") Long teamId, @Param("seasonStart") LocalDate seasonStart);



    @Query("""
    SELECT g.id FROM Game g 
    WHERE g.team.id = :teamId AND g.date <= CURRENT_DATE 
    ORDER BY g.date DESC
""")
    List<Long> findLastFiveGames(@Param("teamId") Long teamId, Pageable pageable);

}
