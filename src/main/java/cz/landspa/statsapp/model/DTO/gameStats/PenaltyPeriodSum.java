package cz.landspa.statsapp.model.DTO.gameStats;

import lombok.*;
import org.springframework.stereotype.Service;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PenaltyPeriodSum {
    Integer period;
    Long teamPenalty;
    Long opponentPenalty;
}
