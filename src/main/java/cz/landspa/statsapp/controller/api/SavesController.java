package cz.landspa.statsapp.controller.api;

import cz.landspa.statsapp.model.Saves;
import cz.landspa.statsapp.service.GameService;
import cz.landspa.statsapp.service.RosterService;
import cz.landspa.statsapp.service.SavesService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/games/{gameId}/saves")
public class SavesController {

    private final SavesService savesService;
    private final GameService gameService;
    private final RosterService rosterService;

    public SavesController(SavesService savesService, GameService gameService, RosterService rosterService) {
        this.savesService = savesService;
        this.gameService = gameService;
        this.rosterService = rosterService;
    }

    @GetMapping
    public ResponseEntity<?> getGameSaves(@PathVariable Long gameId){
        try{
            return new ResponseEntity<>(savesService.getGameSaves(gameId), HttpStatus.OK);

        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{savesId}")
    public ResponseEntity<?> getGoalieGameSaves(@PathVariable Long gameId, @PathVariable Long savesId){
        try{
            return new ResponseEntity<>(savesService.getGameGoalieSaves(gameId,savesId), HttpStatus.OK);

        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{gkId}/period/{period}")
    public ResponseEntity<?> getGoalieGameSaves(@PathVariable Long gameId, @PathVariable Long gkId, @PathVariable Integer period){
        try{
            if(savesService.getByGameGoalkeeperPeriod(gameId, gkId, period)!= null) {
                return new ResponseEntity<>(savesService.getByGameGoalkeeperPeriod(gameId, gkId, period), HttpStatus.OK);
            } else {
                Saves saves = new Saves();
                saves.setGame(gameService.getGameById(gameId));
                saves.setGoalkeeper(rosterService.getRosterById(gkId));
                saves.setPeriod(period);
                return new ResponseEntity<>(savesService.newSaves(saves), HttpStatus.OK);
            }
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/{gkId}/period/{period}/add")
    public ResponseEntity<?> addGoalieSave(@PathVariable Long gameId, @PathVariable Long gkId, @PathVariable Integer period){
        try{
            return new ResponseEntity<>(savesService.addOneSave(gameId,gkId, period), HttpStatus.OK);

        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{gkId}/period/{period}/remove")
    public ResponseEntity<?> removeGoalieSave(@PathVariable Long gameId, @PathVariable Long gkId, @PathVariable Integer period){
        try{
            return new ResponseEntity<>(savesService.removeOneSave(gameId,gkId, period), HttpStatus.OK);

        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
