package cz.landspa.statsapp.controller.api;

import cz.landspa.statsapp.service.GameStatsService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/games/{gameId}/stats")
public class GameStatsController {

    private final GameStatsService gameStatsService;

    public GameStatsController(GameStatsService gameStatsService) {
        this.gameStatsService = gameStatsService;
    }

    @GetMapping
    public ResponseEntity<?>getGameStats(@PathVariable Long gameId){
        Map<String, Object> stats = new HashMap<>();
        stats.put("goalkeepers", gameStatsService.getGameGoalieStats(gameId));
        stats.put("players", gameStatsService.getGamePlayerStats(gameId));
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }

    @GetMapping("summary")
    public ResponseEntity<?>getGameStatsSummary(@PathVariable Long gameId){
        Map<String, Object> stats = new HashMap<>();
        stats.put("goalkeepers", gameStatsService.getGameGoalieStats(gameId));
        stats.put("players", gameStatsService.getGamePlayerStats(gameId));
        stats.put("gameStats", gameStatsService.getGameStats(gameId));
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
}
