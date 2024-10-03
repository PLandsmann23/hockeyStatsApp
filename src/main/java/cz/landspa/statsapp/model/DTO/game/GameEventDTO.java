package cz.landspa.statsapp.model.DTO.game;

import cz.landspa.statsapp.model.DTO.event.EventDTO;
import cz.landspa.statsapp.model.DTO.gameStats.GoalPeriodSum;
import cz.landspa.statsapp.model.Game;
import cz.landspa.statsapp.model.Saves;
import cz.landspa.statsapp.model.Shot;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GameEventDTO {
    Game game;

    List<EventDTO> events;
    List<Shot> shots;
    List<Saves> saves;
    List<GoalPeriodSum> goals;
    Long goalsScored;
    Long goalsConceded;
}

