package cz.landspa.statsapp.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@DiscriminatorValue("GoalScored")
public class GoalScored extends Event{

        @ManyToOne
        Roster scorer;

        @ManyToMany
        @JoinTable(
                name = "goal_assists",
                joinColumns = @JoinColumn(name = "goal_id"),
                inverseJoinColumns = @JoinColumn(name = "roster_id")
        )
        List<Roster> assists;


        @ManyToMany
        @JoinTable(
                name = "goal_on_ice",
                joinColumns = @JoinColumn(name = "goal_id"),
                inverseJoinColumns = @JoinColumn(name = "roster_id")
        )
        List<Roster> onIce;


        String situation;

}
