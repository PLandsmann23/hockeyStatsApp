package cz.landspa.statsapp.model.DTO.event;

import cz.landspa.statsapp.model.DTO.roster.RosterDTO;
import cz.landspa.statsapp.model.DTO.game.GameId;
import cz.landspa.statsapp.model.Penalty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PenaltyDTO extends EventDTO {

    RosterDTO player;
    Integer minutes;
    String reason;

    public PenaltyDTO(String type, Long id, GameId game, Integer time, RosterDTO player, Integer minutes, String reason) {
        super(type, id, game, time);
        this.player = player;
        this.minutes = minutes;
        this.reason = reason;
    }

    public static PenaltyDTO fromPenalty(Penalty penalty){
        return new PenaltyDTO(
                penalty.getClass().getSimpleName(),
                penalty.getId(),
                GameId.fromGame(penalty.getGame()),
                penalty.getTime(),
                RosterDTO.fromRoster(penalty.getPlayer()),
                penalty.getMinutes(),
                penalty.getReason()
        );
    }
}
