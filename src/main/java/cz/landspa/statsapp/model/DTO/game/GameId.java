package cz.landspa.statsapp.model.DTO.game;

import cz.landspa.statsapp.model.Game;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class GameId {
    Long id;


    public GameId(Long id) {
        this.id = id;
    }

    public static GameId fromGame(Game game) {
        return new GameId(game.getId());
    }
}
