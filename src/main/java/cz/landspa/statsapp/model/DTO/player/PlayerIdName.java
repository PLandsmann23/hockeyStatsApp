package cz.landspa.statsapp.model.DTO.player;

import cz.landspa.statsapp.model.Player;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PlayerIdName {
    Long id;

    String name;

    String surname;


    public PlayerIdName(Long id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public static PlayerIdName fromPlayer(Player player){
        return new PlayerIdName(player.getId(), player.getName(), player.getSurname());
    }
}
