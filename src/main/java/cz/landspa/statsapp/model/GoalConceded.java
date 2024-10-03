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
@DiscriminatorValue("GoalConceded")
public class GoalConceded extends Event{

    @ManyToMany
    @JoinTable(
            name = "goal_conceded_on_ice",
            joinColumns = @JoinColumn(name = "goal_conceded_id"),
            inverseJoinColumns = @JoinColumn(name = "roster_id")
    )
    List<Roster> onIce;

    @ManyToOne
    Roster inGoal;


    String situation;
}
