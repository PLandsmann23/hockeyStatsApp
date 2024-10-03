package cz.landspa.statsapp.model.DTO.stats;

import cz.landspa.statsapp.model.DTO.player.PlayerIdNameNumber;
import cz.landspa.statsapp.model.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlayerStats {
    PlayerIdNameNumber player;

    Long gamesPlayed;

    Long goals;

    Long assists;

    Long plusMinus;

    Long penaltyMinutes;


    public PlayerStats(Player player, Long gamesPlayed, Long goals, Long assists, Long plus, Long minus, Long penaltyMinutes) {
        this.player = PlayerIdNameNumber.fromPlayer(player);
        this.gamesPlayed = gamesPlayed;
        this.goals = goals;
        this.assists = assists;
        this.plusMinus = plus - minus;
        this.penaltyMinutes = penaltyMinutes;
    }
}
