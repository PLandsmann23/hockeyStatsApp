package cz.landspa.statsapp.model.DTO.event;

import cz.landspa.statsapp.model.DTO.game.GameId;
import cz.landspa.statsapp.model.Event;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class EventDTO {
    String type;
    Long id;
    GameId game;
    Integer time;

    public EventDTO(String type,Long id, GameId game, Integer time) {
        this.type = type;
        this.id = id;
        this.game = game;
        this.time = time;
    }

    public static EventDTO fromEvent(Event event){
        return new EventDTO(event.getClass().getSimpleName(),event.getId(),GameId.fromGame(event.getGame()), event.getTime());
    }
}
