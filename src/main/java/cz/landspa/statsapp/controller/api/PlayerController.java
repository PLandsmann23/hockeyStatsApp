package cz.landspa.statsapp.controller.api;

import cz.landspa.statsapp.model.Player;
import cz.landspa.statsapp.service.PlayerService;
import cz.landspa.statsapp.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class PlayerController {


    private final PlayerService playerService;
    private final TeamService teamService;

    public PlayerController(PlayerService playerService,TeamService teamService) {
        this.playerService = playerService;
        this.teamService = teamService;
    }

    @PostMapping("/teams/{teamId}/players")
    public ResponseEntity<?> createTeamPlayer(@PathVariable Long teamId,@Valid @RequestBody Player player){
        try{
            player.setTeam(teamService.getTeamById(teamId));
            return new ResponseEntity<>(playerService.createPlayer(player), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/teams/{teamId}/players")
    public ResponseEntity<?> getTeamPlayers(@PathVariable Long teamId){
        try{
            return new ResponseEntity<>(playerService.getTeamPlayers(teamId), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/players/{id}")
    public ResponseEntity<?> updatePlayer(@PathVariable Long id,@Valid @RequestBody Player player){
            try{
                return new ResponseEntity<>(playerService.updatePlayer(player, id), HttpStatus.OK);
            } catch (Exception e){
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
    }

    @DeleteMapping("/players/{id}")
    public ResponseEntity<?> deletePlayer(@PathVariable Long id){
        try{
            playerService.deletePlayer(id);
            return new ResponseEntity<>( HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}
