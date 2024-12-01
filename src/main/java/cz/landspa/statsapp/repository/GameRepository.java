package cz.landspa.statsapp.repository;

import cz.landspa.statsapp.model.DTO.game.GameScoresDTO;
import cz.landspa.statsapp.model.DTO.gameStats.GoalPeriodSum;
import cz.landspa.statsapp.model.DTO.gameStats.PenaltyPeriodSum;
import cz.landspa.statsapp.model.DTO.gameStats.SavePeriodSum;
import cz.landspa.statsapp.model.DTO.gameStats.ShotPeriodSum;
import cz.landspa.statsapp.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> findAllByTeamIdOrderByDateAscTimeAsc(Long teamId);

    @Query("SELECT new cz.landspa.statsapp.model.DTO.game.GameScoresDTO(g," +
            "(SELECT count (distinct gs.id) FROM GoalScored gs WHERE gs.game.team.id = :teamId AND gs.game.id = g.id)," +
            "(SELECT count (distinct gc.id) FROM GoalConceded gc WHERE gc.game.team.id = :teamId AND gc.game.id = g.id)" +
            " ) FROM Game g WHERE g.team.id = :teamId GROUP BY g.id ORDER BY g.date ASC, g.time ASC ")
    List<GameScoresDTO> getGamesWithScores(Long teamId);

    void deleteAllByTeamId(Long teamId);


    List<Game> findTop4ByTeamOwnerIdAndDateAfterOrderByDateAsc(Long userId, LocalDate date);

    Long countByTeamOwnerId(Long userId);

    @Query("""
    SELECT new cz.landspa.statsapp.model.DTO.gameStats.ShotPeriodSum(
        :period,
        (SELECT sh.shots FROM Shot sh WHERE sh.game.id = :gameId AND sh.period = :period),
        (SELECT SUM(sa.saves) FROM Saves sa WHERE sa.game.id = :gameId AND sa.period = :period),
        (SELECT COUNT(gc.id) FROM GoalConceded gc WHERE gc.game.id = :gameId AND gc.time >= :periodStart AND gc.time <= :periodEnd)
    )
""")
    ShotPeriodSum getPeriodShotSum(@Param("gameId") Long gameId, @Param("periodStart") Integer periodStart, @Param("periodEnd") Integer periodEnd, @Param("period") Integer period);

    @Query("""
    SELECT new cz.landspa.statsapp.model.DTO.gameStats.SavePeriodSum(
        :period,
        (SELECT SUM(sa.saves) FROM Saves sa WHERE sa.game.id = :gameId AND sa.period = :period),
        (SELECT sh.shots FROM Shot sh WHERE sh.game.id = :gameId AND sh.period = :period),
        (SELECT COUNT(DISTINCT gs.id) FROM GoalScored gs WHERE gs.game.id = :gameId AND gs.time >= :periodStart AND gs.time <= :periodEnd)
    )
""")
    SavePeriodSum getPeriodSaveSum(@Param("gameId") Long gameId, @Param("periodStart") Integer periodStart, @Param("periodEnd") Integer periodEnd, @Param("period") Integer period);


    @Query("""
    SELECT new cz.landspa.statsapp.model.DTO.gameStats.GoalPeriodSum(
        :period,
        (SELECT COUNT(DISTINCT gs.id) FROM GoalScored gs WHERE gs.game.id = :gameId AND gs.time >= :periodStart AND gs.time <= :periodEnd),
        (SELECT COUNT(DISTINCT gc.id) FROM GoalConceded gc WHERE gc.game.id = :gameId AND gc.time >= :periodStart AND gc.time <= :periodEnd)
    )
    FROM Game g
    WHERE g.id = :gameId
""")
    GoalPeriodSum getPeriodGoalSum(@Param("gameId") Long gameId, @Param("periodStart") Integer periodStart, @Param("periodEnd") Integer periodEnd, @Param("period") Integer period);

    @Query("""
    SELECT new cz.landspa.statsapp.model.DTO.gameStats.PenaltyPeriodSum(
        :period,
        (SELECT COALESCE(SUM(p.minutes),0) FROM Penalty p WHERE p.game.id = :gameId AND p.time >= :periodStart AND p.time <= :periodEnd),
        (SELECT COALESCE(SUM(op.minutes),0) FROM OpponentPenalty op WHERE op.game.id = :gameId AND op.time >= :periodStart AND op.time <= :periodEnd)
    )
    FROM Game g
    WHERE g.id = :gameId
""")
    PenaltyPeriodSum getPeriodPenaltySum(@Param("gameId") Long gameId, @Param("periodStart") Integer periodStart, @Param("periodEnd") Integer periodEnd, @Param("period") Integer period);

}
