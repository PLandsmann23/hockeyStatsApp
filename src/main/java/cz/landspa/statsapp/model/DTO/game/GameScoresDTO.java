package cz.landspa.statsapp.model.DTO.game;

import cz.landspa.statsapp.model.Game;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GameScoresDTO {
    Game game;
    Long goalsScored;
    Long goalsConceded;
}
