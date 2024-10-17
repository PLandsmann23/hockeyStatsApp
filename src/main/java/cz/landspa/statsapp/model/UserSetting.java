package cz.landspa.statsapp.model;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
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
public class UserSetting {


    @Id
    @Column(name = "user_id")
    Long userId;

    @MapsId
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIdentityReference(alwaysAsId = true)
    User user;


    Integer defaultPeriods = 3;

    Integer defaultPeriodLength = 20;
}
