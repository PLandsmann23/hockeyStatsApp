package cz.landspa.statsapp.model.DTO.event;

import cz.landspa.statsapp.model.DTO.roster.RosterDTO;
import cz.landspa.statsapp.model.DTO.game.GameId;
import cz.landspa.statsapp.model.GoalScored;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
public class GoalScoredDTO extends EventDTO {
    RosterDTO scorer;
  List<RosterDTO> assists;
    List<RosterDTO> onIce;
    String situation;

    public GoalScoredDTO(String type, Long id, GameId game, Integer time, RosterDTO scorer, List<RosterDTO> assists, List<RosterDTO> onIce, String situation) {
        super(type, id, game, time);
        this.scorer = scorer;
        this.assists = assists;
         this.onIce = onIce;
        this.situation = situation;
    }

    public static GoalScoredDTO fromGoalScored(GoalScored goalScored){
        return new GoalScoredDTO(
                goalScored.getClass().getSimpleName(),
                goalScored.getId(),
                GameId.fromGame(goalScored.getGame()),
                goalScored.getTime(),
                RosterDTO.fromRoster(goalScored.getScorer()),
                goalScored.getAssists().stream().map(RosterDTO::fromRoster).collect(Collectors.toList()),
                goalScored.getOnIce().stream().map(RosterDTO::fromRoster).collect(Collectors.toList()),
                goalScored.getSituation()
        );
    }
}
