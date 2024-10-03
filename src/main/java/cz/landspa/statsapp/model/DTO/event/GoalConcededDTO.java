package cz.landspa.statsapp.model.DTO.event;

import cz.landspa.statsapp.model.DTO.roster.RosterDTO;
import cz.landspa.statsapp.model.DTO.game.GameId;
import cz.landspa.statsapp.model.GoalConceded;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
public class GoalConcededDTO extends EventDTO {

    List<RosterDTO> onIce;
    String situation;

    public GoalConcededDTO(String type, Long id, GameId game, Integer time, List<RosterDTO> onIce, String situation) {
        super(type, id, game, time);
         this.onIce = onIce;
        this.situation = situation;
    }

    public static GoalConcededDTO fromGoalConceded(GoalConceded goalConceded){
        return new GoalConcededDTO(
                goalConceded.getClass().getSimpleName(),
                goalConceded.getId(),
                GameId.fromGame(goalConceded.getGame()),
                goalConceded.getTime(),
                goalConceded.getOnIce().stream().map(RosterDTO::fromRoster).collect(Collectors.toList()),
                goalConceded.getSituation()
        );
    }
}
