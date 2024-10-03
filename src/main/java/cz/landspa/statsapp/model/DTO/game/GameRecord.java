package cz.landspa.statsapp.model.DTO.game;

import cz.landspa.statsapp.model.Event;
import cz.landspa.statsapp.model.Game;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GameRecord {
    Game game;
    List<Event> events;
}
