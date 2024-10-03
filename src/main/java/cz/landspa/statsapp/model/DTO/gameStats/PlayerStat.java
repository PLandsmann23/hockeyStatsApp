package cz.landspa.statsapp.model.DTO.gameStats;

import cz.landspa.statsapp.model.Player;
import cz.landspa.statsapp.model.Roster;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlayerStat {
    private Roster player;
    private Long goals;
    private Long assists;
    private Long plus;
    private Long minus;
    private Long penaltyMinutes;


}
