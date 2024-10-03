package cz.landspa.statsapp.controller.api;

import cz.landspa.statsapp.model.Game;
import cz.landspa.statsapp.model.GoalScored;
import cz.landspa.statsapp.service.EventService;
import cz.landspa.statsapp.service.GameService;
import cz.landspa.statsapp.service.GoalScoredService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/games/")
public class GoalScoredController {

    private final GameService gameService;
    private final GoalScoredService goalScoredService;
    private final EventService eventService;

    public GoalScoredController(GameService gameService, GoalScoredService goalScoredService, EventService eventService) {
        this.gameService = gameService;
        this.goalScoredService = goalScoredService;
        this.eventService = eventService;
    }

    @PostMapping("{gameId}/goalsScored")
    public ResponseEntity<?> addScoredGoal(@PathVariable Long gameId, @RequestBody GoalScored goalScored){
        try{
            Game game = gameService.getGameById(gameId);
            goalScored.setGame(game);
            return new ResponseEntity<>(goalScoredService.addGoal(goalScored), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("{gameId}/goalsScored/{eventId}")
    public ResponseEntity<?> updateScoredGoal(@PathVariable Long eventId, @RequestBody GoalScored goalScored){
        try{

            return new ResponseEntity<>(goalScoredService.updateGoal(goalScored, eventId), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("{gameId}/goalsScored/{eventId}")
    public ResponseEntity<?> deleteScoredGoal(@PathVariable Long eventId){
        try{

            eventService.deleteEvent(eventId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
