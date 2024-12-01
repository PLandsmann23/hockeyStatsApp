package cz.landspa.statsapp.model;


import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@DiscriminatorValue("Penalty")
public class Penalty extends Event{

    @ManyToOne
    Roster player;

    Integer minutes;

    String reason;

    Boolean coincidental = false;
}
