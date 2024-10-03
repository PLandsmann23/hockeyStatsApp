package cz.landspa.statsapp.model.DTO.roster;

import cz.landspa.statsapp.model.DTO.game.GameId;
import cz.landspa.statsapp.model.DTO.player.PlayerIdName;
import cz.landspa.statsapp.model.Roster;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RosterDTO {
    Long id;
    Integer gameNumber;
    PlayerIdName player;
    GameId game;

    public RosterDTO(Long id, Integer gameNumber, PlayerIdName player, GameId game) {
        this.id = id;
        this.gameNumber = gameNumber;
        this.player = player;
        this.game = game;
    }

    public static RosterDTO fromRoster(Roster roster){
        if(roster != null) {
            return new RosterDTO(
                    roster.getId(),
                    roster.getGameNumber(),
                    PlayerIdName.fromPlayer(roster.getPlayer()),
                    GameId.fromGame(roster.getGame())
            );
        } else {
            return null;
        }
    }
}
