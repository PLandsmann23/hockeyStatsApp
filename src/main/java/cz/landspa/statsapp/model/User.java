package cz.landspa.statsapp.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(name = "UniqueUsername", columnNames = {"username"}),@UniqueConstraint(name = "UniqueEmail", columnNames = {"email"})})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

   // @Column(unique = true)
    @NotBlank(message = "Uživatelské jméno musí být vyplněno")
    String username;

   // @Column(unique = true)
    @NotBlank(message = "Email musí být vyplněn")
    String email;



   // @JsonIgnore
    @NotBlank(message = "Heslo musí být vyplněno")
    String password;


    @Column(columnDefinition = "ENUM('USER','ADMIN')")
    @Enumerated(EnumType.STRING)
    Role role = Role.USER;

    @Column(nullable = false)
    boolean enabled = false;



}
