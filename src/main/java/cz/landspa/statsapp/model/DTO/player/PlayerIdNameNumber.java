package cz.landspa.statsapp.model.DTO.player;

import cz.landspa.statsapp.model.Player;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class PlayerIdNameNumber {
    Long id;

    String name;

    String surname;

    Integer number;


    public PlayerIdNameNumber(Long id, String name, String surname, Integer number) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.number = number;
    }

    public static PlayerIdNameNumber fromPlayer(Player player){
        return new PlayerIdNameNumber(player.getId(), player.getName(), player.getSurname(), player.getNumber());
    }
}
