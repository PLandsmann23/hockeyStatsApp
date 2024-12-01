package cz.landspa.statsapp.model.DTO.gameStats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ShotPeriodSum {
    Integer period;
    Long team;
    Long opponent;

    public ShotPeriodSum(Integer period, Long shots, Long saves, Long goalsConceded) {
        this.period = period;
        this.team = shots;
        if(saves !=null){
            this.opponent = saves + goalsConceded;
        } else {
            this.opponent = goalsConceded;
        }
    }
}
