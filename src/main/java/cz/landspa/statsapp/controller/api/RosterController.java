package cz.landspa.statsapp.controller.api;

import cz.landspa.statsapp.model.DTO.GoalkeeperChange;
import cz.landspa.statsapp.model.Game;
import cz.landspa.statsapp.model.Roster;
import cz.landspa.statsapp.service.GameService;
import cz.landspa.statsapp.service.PlayerService;
import cz.landspa.statsapp.service.RosterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("api/games")
public class RosterController {


    private final GameService gameService;
    private final RosterService rosterService;
    private final PlayerService playerService;

    public RosterController(RosterService rosterService, GameService gameService, PlayerService playerService) {
        this.rosterService = rosterService;
        this.gameService = gameService;
        this.playerService = playerService;
    }

    @PostMapping("/{gameId}/roster")
    public ResponseEntity<?> addPlayerToRoster(@PathVariable Long gameId, @Valid @RequestBody Roster roster){
        try{
            if(roster.getGame() == null){
                roster.setGame(gameService.getGameById(gameId));
            }
            return new ResponseEntity<>(rosterService.addPlayerToRoster(roster), HttpStatus.OK);
        } catch (DataIntegrityViolationException e){
            return new ResponseEntity<>("Tento záznam již existuje", HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            if(Objects.equals(e.getMessage(), "V soupisce se již nachází dva brankáři")){
               Map<String, String> message = new HashMap<>();
               message.put("message", e.getMessage());
                return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/{gameId}/roster")
    public ResponseEntity<?> getGameRoster(@PathVariable Long gameId){
        try{
            Game game = gameService.getGameById(gameId);

            Map<String, Object> response = new HashMap<>();
            response.put("game", game);
            response.put("roster", rosterService.getRosterForGame(gameId));
            response.put("notInRoster", playerService.findTeamPlayersNotInRoster(game.getTeam().getId(), gameId));

            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{gameId}/roster")
    public ResponseEntity<?> deleteGameRoster(@PathVariable Long gameId){
        try{
            rosterService.deleteAllPlayersFromRoster(gameId);
            return new ResponseEntity<>( HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/{gameId}/roster/{id}")
    public ResponseEntity<?> deleteRosterItem(@PathVariable Long gameId,@PathVariable Long id){
        try{
            rosterService.deletePlayerFromRoster(id);
            return new ResponseEntity<>( HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{gameId}/roster/{id}")
    public ResponseEntity<?> updateRosterItem(@PathVariable Long gameId,@PathVariable Long id, @RequestBody Roster roster){

        try{
            return new ResponseEntity<>(rosterService.updateRoster(roster, id) ,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{gameId}/goalkeeperChange")
    public ResponseEntity<?> updateRosterItem(@PathVariable Long gameId){

        try{
            return new ResponseEntity<>(rosterService.changeActiveGoalkeeper(gameId) ,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
