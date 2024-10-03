package cz.landspa.statsapp.controller.api;

import cz.landspa.statsapp.model.Game;
import cz.landspa.statsapp.model.GoalConceded;
import cz.landspa.statsapp.service.EventService;
import cz.landspa.statsapp.service.GameService;
import cz.landspa.statsapp.service.GoalConcededService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/games/")
public class GoalConcededController {

    private final GameService gameService;
    private final GoalConcededService goalConcededService;

    private final EventService eventService;

    public GoalConcededController(GameService gameService, GoalConcededService goalConcededService, EventService eventService) {
        this.gameService = gameService;
        this.goalConcededService = goalConcededService;
        this.eventService = eventService;
    }

    @PostMapping("{gameId}/goalsConceded")
    public ResponseEntity<?> addConcededGoal(@PathVariable Long gameId, @RequestBody GoalConceded goalConceded){
        try{
            Game game = gameService.getGameById(gameId);
            goalConceded.setGame(game);
            return new ResponseEntity<>(goalConcededService.addGoal(goalConceded), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("{gameId}/goalsConceded/{eventId}")
    public ResponseEntity<?> updateConcededGoal(@PathVariable Long eventId, @RequestBody GoalConceded goalConceded){
        try{

            return new ResponseEntity<>(goalConcededService.updateGoal(goalConceded, eventId), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("{gameId}/goalsConceded/{eventId}")
    public ResponseEntity<?> deleteConcededGoal(@PathVariable Long eventId){
        try{

            eventService.deleteEvent(eventId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
