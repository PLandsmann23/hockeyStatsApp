package cz.landspa.statsapp.model.DTO.stats;

import cz.landspa.statsapp.model.DTO.player.PlayerIdNameNumber;
import cz.landspa.statsapp.model.Player;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GoalieStats {
    PlayerIdNameNumber player;

    Long gamesPlayed;

    Long goalsConceded;

    Long saves;

    Float avgGoals;

    Float savePercentage;

    public GoalieStats(Player player, Long gamesPlayed, Long goalsConceded, Long saves) {
        this.player = PlayerIdNameNumber.fromPlayer(player);
        this.gamesPlayed = gamesPlayed;
        this.goalsConceded = goalsConceded;
        this.saves = saves;

        if (gamesPlayed != null && gamesPlayed > 0) {
            this.avgGoals = (float) goalsConceded / gamesPlayed;
        } else {
            this.avgGoals = null;
        }

        if (saves != null && saves > 0 && goalsConceded != null) {
            this.savePercentage = (float) saves / (saves + goalsConceded);
        } else {
            this.savePercentage = null;
        }
    }

}
