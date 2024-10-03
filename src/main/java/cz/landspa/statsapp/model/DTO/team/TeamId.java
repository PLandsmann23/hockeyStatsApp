package cz.landspa.statsapp.model.DTO.team;

import cz.landspa.statsapp.model.Team;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TeamId {
    Long id;


    public TeamId(Long id) {
        this.id = id;
    }

    public static TeamId fromTeam(Team team) {
        return new TeamId(team.getId());
    }
}
