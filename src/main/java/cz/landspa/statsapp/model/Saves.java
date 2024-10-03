package cz.landspa.statsapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(uniqueConstraints = {@UniqueConstraint(name = "Saves", columnNames = {"game_id", "goalkeeper_id", "period"})})
public class Saves {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    Game game;

    @ManyToOne
    Roster goalkeeper;

    Integer period;

    Long saves = 0L;
}
