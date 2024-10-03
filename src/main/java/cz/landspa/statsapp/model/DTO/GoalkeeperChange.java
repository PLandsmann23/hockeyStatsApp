package cz.landspa.statsapp.model.DTO;

import cz.landspa.statsapp.model.Roster;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GoalkeeperChange {
    Roster leaving;
    Roster coming;
}
