package cz.landspa.statsapp.controller.api;

import cz.landspa.statsapp.service.EventService;
import cz.landspa.statsapp.service.GameService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/games/")
public class EventController {


    private final EventService eventService;
    private final GameService gameService;

    public EventController(EventService eventService,GameService gameService) {
        this.eventService = eventService;
        this.gameService = gameService;
    }

    @GetMapping("{gameId}/events/{eventId}")
    public ResponseEntity<?> getOneEvent(@PathVariable Long gameId, @PathVariable Long eventId){
        try {
            if(eventService.getById(eventId)==null){
                throw new IllegalArgumentException("Neexistující událost");
            }
            return new ResponseEntity<>(eventService.getById(eventId), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("{gameId}/events")
    public ResponseEntity<?> getGameEvents(@PathVariable Long gameId){
        try {
            if(gameService.getGameById(gameId)==null){
                 throw new IllegalArgumentException("Neexistující zápas");
            }
            return new ResponseEntity<>(eventService.getGameEvents(gameId), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("{gameId}/events/{eventId}")
    public ResponseEntity<?>deleteGameEvent(@PathVariable Long eventId, @PathVariable Long gameId){
        try {
            eventService.deleteEvent(eventId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
}
