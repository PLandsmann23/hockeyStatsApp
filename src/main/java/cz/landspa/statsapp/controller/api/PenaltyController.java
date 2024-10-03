package cz.landspa.statsapp.controller.api;

import cz.landspa.statsapp.model.Game;
import cz.landspa.statsapp.model.Penalty;
import cz.landspa.statsapp.service.EventService;
import cz.landspa.statsapp.service.GameService;
import cz.landspa.statsapp.service.PenaltyService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/games/")
public class PenaltyController {

    private final GameService gameService;
    private final EventService eventService;
    private final PenaltyService penaltyService;

    public PenaltyController(GameService gameService, EventService eventService, PenaltyService penaltyService) {
        this.gameService = gameService;
        this.eventService = eventService;
        this.penaltyService = penaltyService;
    }

    @PostMapping("{gameId}/penalties")
    public ResponseEntity<?> addPenalty(@PathVariable Long gameId, @RequestBody Penalty penalty){
        try{
            Game game = gameService.getGameById(gameId);
            penalty.setGame(game);
            return new ResponseEntity<>(penaltyService.addPenalty(penalty), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("{gameId}/penalties/{eventId}")
    public ResponseEntity<?> addPenalty(@PathVariable Long gameId,@PathVariable Long eventId, @RequestBody Penalty penalty){
        try{

            return new ResponseEntity<>(penaltyService.updatePenalty(penalty, eventId), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("{gameId}/penalties/{eventId}")
    public ResponseEntity<?> deletePenalty(@PathVariable Long eventId){
        try{

            eventService.deleteEvent(eventId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
