package cz.landspa.statsapp.controller.api;

import cz.landspa.statsapp.service.ShotService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/games/{gameId}/shots")
public class ShotController {

    private final ShotService shotService;

    public ShotController(ShotService shotService) {
        this.shotService = shotService;
    }

    @PutMapping("/period/{period}/add")
    public ResponseEntity<?> addShots(@PathVariable Long gameId, @PathVariable Integer period){
        try{
            return new ResponseEntity<>(shotService.addOneShot(gameId, period), HttpStatus.OK);

        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/period/{period}/remove")
    public ResponseEntity<?> removeShots(@PathVariable Long gameId, @PathVariable Integer period){
        try{
            return new ResponseEntity<>(shotService.removeOneShot(gameId, period), HttpStatus.OK);

        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
