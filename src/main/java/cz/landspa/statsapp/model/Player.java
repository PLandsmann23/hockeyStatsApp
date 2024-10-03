package cz.landspa.statsapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Jméno musí být vyplněno")
    String name;

    @NotBlank(message = "Příjmení musí být vyplněno")
    String surname;


    @Min(value = 1, message = "Číslo musí být větší než 0")
    @Max(value = 99, message = "Číslo musí být menší než 100")
    Integer number;

    @ManyToOne(fetch = FetchType.EAGER)
    Team team;

    @Column(columnDefinition = "ENUM('GK','D','F')")
    @Enumerated(EnumType.STRING)
    Position position;

}
