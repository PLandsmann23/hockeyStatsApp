package cz.landspa.statsapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(uniqueConstraints = {@UniqueConstraint(name = "Roster", columnNames = {"game_id", "player_id"})})
public class Roster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    Game game;

    @ManyToOne
    Player player;

    Integer gameNumber;

    @Min(value = 0)
    @Max(value = 5)
    Integer line = 0;

    boolean activeGk;
}
