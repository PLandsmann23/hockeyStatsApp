package cz.landspa.statsapp.model.DTO.saves;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PeriodSaves {
    Integer period;

    Long saves;
}
