package cz.landspa.statsapp.model.DTO.team;

import cz.landspa.statsapp.model.DTO.game.GameScoresDTO;
import cz.landspa.statsapp.model.Player;
import cz.landspa.statsapp.model.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeamPlayersGames {
    private Team team;

    private List<Player> players;
    private List<GameScoresDTO> games;
}
