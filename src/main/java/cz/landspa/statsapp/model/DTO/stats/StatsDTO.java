package cz.landspa.statsapp.model.DTO.stats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StatsDTO {
    TeamStats team;
    List<PlayerStats> players;

    List <GoalieStats> goalkeepers;
}
