package cz.landspa.statsapp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    Team team;

    @NotBlank(message = "Soupeř musí být vyplněn")
    String opponent;

    LocalDate date;

    LocalTime time;

    String venue;

    Integer periods = 3;

    Integer periodLength = 20;

    Integer currentPeriod =1;

}
