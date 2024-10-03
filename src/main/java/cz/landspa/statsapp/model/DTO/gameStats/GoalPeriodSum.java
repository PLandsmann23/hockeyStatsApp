package cz.landspa.statsapp.model.DTO.gameStats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GoalPeriodSum {
    Integer period;
    Long team;
    Long opponent;
}
