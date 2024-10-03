package cz.landspa.statsapp.controller.api;

import cz.landspa.statsapp.model.Game;
import cz.landspa.statsapp.model.OpponentPenalty;
import cz.landspa.statsapp.model.Penalty;
import cz.landspa.statsapp.service.EventService;
import cz.landspa.statsapp.service.GameService;
import cz.landspa.statsapp.service.OpponentPenaltyService;
import cz.landspa.statsapp.service.PenaltyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/games/")
public class OpponentPenaltyController {

    private final GameService gameService;
    private final OpponentPenaltyService opponentPenaltyService;
    private final EventService eventService;

    public OpponentPenaltyController(GameService gameService, OpponentPenaltyService opponentPenaltyService, EventService eventService) {
        this.gameService = gameService;
        this.opponentPenaltyService = opponentPenaltyService;
        this.eventService = eventService;
    }

    @PostMapping("{gameId}/opponentPenalties")
    public ResponseEntity<?> addPenalty(@PathVariable Long gameId, @RequestBody OpponentPenalty penalty){
        try{
            Game game = gameService.getGameById(gameId);
            penalty.setGame(game);
            return new ResponseEntity<>(opponentPenaltyService.addPenalty(penalty), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("{gameId}/opponentPenalties/{eventId}")
    public ResponseEntity<?> addPenalty(@PathVariable Long gameId,@PathVariable Long eventId, @RequestBody OpponentPenalty penalty){
        try{

            return new ResponseEntity<>(opponentPenaltyService.updatePenalty(penalty, eventId), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("{gameId}/opponentPenalties/{eventId}")
    public ResponseEntity<?> deleteOpponentPenalty(@PathVariable Long eventId){
        try{

            eventService.deleteEvent(eventId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
