package cz.landspa.statsapp.model.DTO.gameStats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SavePeriodSum {
    Integer period;
    Long team;
    Long opponent;

    public SavePeriodSum(Integer period,  Long saves, Long shots, Long goalsScored) {
        this.period = period;
        this.team = saves;
        this.opponent = shots - goalsScored;
    }
}
