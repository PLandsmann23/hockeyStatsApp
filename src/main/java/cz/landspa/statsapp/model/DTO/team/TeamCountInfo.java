package cz.landspa.statsapp.model.DTO.team;


import cz.landspa.statsapp.model.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeamCountInfo {
    private Team team;
    private Long players;
    private Long games;
}
