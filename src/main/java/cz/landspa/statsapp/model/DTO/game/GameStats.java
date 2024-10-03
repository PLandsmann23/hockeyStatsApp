package cz.landspa.statsapp.model.DTO.game;

import cz.landspa.statsapp.model.DTO.gameStats.GoalPeriodSum;
import cz.landspa.statsapp.model.DTO.gameStats.PenaltyPeriodSum;
import cz.landspa.statsapp.model.DTO.gameStats.SavePeriodSum;
import cz.landspa.statsapp.model.DTO.gameStats.ShotPeriodSum;
import cz.landspa.statsapp.model.Game;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GameStats {
    Game game;

    Long goalsScored;

    Long goalsConceded;

    Long totalShots;

    Long totalSaves;

    Long totalPenaltyMinutes;

    Long totalOpponentPenaltyMinutes;

    Long numberOfPenalties;

    Long numberOfOpponentPenalties;

    Long ppGoalsScored;

    Long shGoalsConceded;

    Long goalsScoredInMajorPP;

    Long goalsConcededInMajorSh;

    Float myShotsEfficiency;

    Float opponentShotsEfficiency;

    Float ppEfficiency;

    Float shEfficiency;

    List<GoalPeriodSum> goals;

    List<ShotPeriodSum> shots;

    List<SavePeriodSum> saves;

    List<PenaltyPeriodSum> penalties;

    public GameStats(Game game, Long goalsScored, Long goalsConceded, Long totalShots, Long totalSaves, Long totalPenalty, Long totalOpponentPenalty,Long numberOfPenalties, Long numberOfOpponentPenalties, Long ppGoalsScored, Long shGoalsConceded,Long goalsScoredInMajorPP, Long goalsConcededInMajorSh) {
        this.game = game;
        this.goalsScored = goalsScored;
        this.goalsConceded = goalsConceded;
        this.totalShots = totalShots;
        this.totalSaves = totalSaves;
        this.totalPenaltyMinutes = totalPenalty;
        this.totalOpponentPenaltyMinutes = totalOpponentPenalty;
        this.numberOfPenalties = numberOfPenalties;
        this.numberOfOpponentPenalties = numberOfOpponentPenalties;
        this.ppGoalsScored = ppGoalsScored;
        this.shGoalsConceded = shGoalsConceded;
        this.goalsScoredInMajorPP = goalsScoredInMajorPP;
        this.goalsConcededInMajorSh = goalsConcededInMajorSh;
        if(this.totalShots != null ) {
            if (this.totalShots != 0) {
                this.myShotsEfficiency = ((float) this.goalsScored / this.totalShots) * 100;
            } else {
                this.myShotsEfficiency = null;
            }
        } else {
            this.myShotsEfficiency = null;
        }
        if(this.totalSaves != null ) {
            if (this.totalSaves + this.goalsConceded != 0) {
                this.opponentShotsEfficiency = (((float) this.goalsConceded / (this.totalSaves + this.goalsConceded)) * 100);
            } else {
                this.opponentShotsEfficiency = null;
            }
        } else {
            this.opponentShotsEfficiency = null;
        }
        if(this.numberOfOpponentPenalties != null ) {
            if (this.numberOfOpponentPenalties + this.goalsScoredInMajorPP != 0) {
                this.ppEfficiency = (((float) this.ppGoalsScored / (this.numberOfOpponentPenalties + this.goalsScoredInMajorPP)) * 100);
            } else {
                this.ppEfficiency = null;
            }
        } else {
            this.ppEfficiency = null;
        }
        if(this.numberOfPenalties != null ) {
            if (this.numberOfPenalties + this.goalsConcededInMajorSh != 0) {
                this.shEfficiency = (((float) (this.numberOfPenalties - this.shGoalsConceded + this.goalsConcededInMajorSh) / (this.numberOfPenalties + this.goalsConcededInMajorSh)) * 100);
            } else {
                this.shEfficiency = null;
            }
        } else {
            this.shEfficiency = null;
        }



    }


}
