package cz.landspa.statsapp.controller.api;

import cz.landspa.statsapp.model.DTO.game.GameEventDTO;
import cz.landspa.statsapp.model.Game;
import cz.landspa.statsapp.model.Shot;
import cz.landspa.statsapp.model.Team;
import cz.landspa.statsapp.service.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/games")
public class GameController {


    private final GameService gameService;
    private final TeamService teamService;
    private final EventService eventService;
    private final SavesService savesService;
    private final ShotService shotService;


    public GameController(GameService gameService, TeamService teamService, EventService eventService, SavesService savesService, ShotService shotService) {
        this.gameService = gameService;
        this.teamService = teamService;
        this.eventService = eventService;
        this.savesService = savesService;
        this.shotService = shotService;
    }

    @PostMapping
    public ResponseEntity<?> newGame(@Valid @RequestBody Game game){
        try {
    return new ResponseEntity<>(gameService.createGame(game), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<?> getTeamGames(@RequestBody Team team){
        try{
            if(teamService.getTeamById(team.getId()) != null) {
                return new ResponseEntity<>(gameService.getTeamGames(team.getId()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Tým nebyl nalezen", HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGame(@PathVariable Long id, @Valid @RequestBody Game game){
        try{
            return new ResponseEntity<>(gameService.updateGame(game, id), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGame(@PathVariable Long id){
        try {
            gameService.deleteGame(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGame(@PathVariable Long id){
        try {
            Game game = gameService.getGameById(id);
            if(shotService.getGamePeriodShots(game.getId(), game.getCurrentPeriod()) ==null){
                Shot shot = new Shot();
                shot.setGame(game);
                shot.setPeriod(game.getCurrentPeriod());
                shotService.newShots(shot);
            }
            GameEventDTO gameEventDTO = new GameEventDTO(game,eventService.getGameEvents(id),shotService.getGameShots(id), savesService.getGameSaves(id),gameService.getPeriodGoals(id), eventService.getScoredGoals(id), eventService.getConcededGoals(id));
            return new ResponseEntity<>(/*gameService.getGameRecordInfo(id)*/gameEventDTO,HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/addPeriod")
    public ResponseEntity<?> addPeriod(@PathVariable Long id){
        try {
            gameService.addPeriod(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/removePeriod")
    public ResponseEntity<?> removePeriod(@PathVariable Long id){
        try {
            gameService.removePeriod(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
