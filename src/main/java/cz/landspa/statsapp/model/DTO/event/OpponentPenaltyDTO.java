package cz.landspa.statsapp.model.DTO.event;

import cz.landspa.statsapp.model.DTO.game.GameId;
import cz.landspa.statsapp.model.OpponentPenalty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class OpponentPenaltyDTO extends EventDTO {
    Integer minutes;
    Boolean coincidental;

    public OpponentPenaltyDTO(String type, Long id, GameId game, Integer time, Integer minutes, Boolean coincidental) {
        super(type, id, game, time);
        this.minutes = minutes;
        this.coincidental = coincidental;
    }

    public static OpponentPenaltyDTO fromOpponentPenalty(OpponentPenalty opponentPenalty){
        return new OpponentPenaltyDTO(
                opponentPenalty.getClass().getSimpleName(),
                opponentPenalty.getId(),
                GameId.fromGame(opponentPenalty.getGame()),
                opponentPenalty.getTime(),
                opponentPenalty.getMinutes(),
                opponentPenalty.getCoincidental()
        );
    }
}
