package cz.landspa.statsapp.controller.api;

import cz.landspa.statsapp.model.User;
import cz.landspa.statsapp.service.GameService;
import cz.landspa.statsapp.service.PlayerService;
import cz.landspa.statsapp.service.TeamService;
import cz.landspa.statsapp.service.UserService;
import cz.landspa.statsapp.util.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/info")
public class InfoController {

    private final UserService userService;
    private final TeamService teamService;
    private final GameService gameService;
    private final PlayerService playerService;

    private final SecurityUtil securityUtil;

    public InfoController(UserService userService, TeamService teamService, GameService gameService, PlayerService playerService, SecurityUtil securityUtil) {
        this.userService = userService;
        this.teamService = teamService;
        this.gameService = gameService;
        this.playerService = playerService;
        this.securityUtil = securityUtil;
    }

    @GetMapping
    public ResponseEntity<?> getInfo(){


        User user = userService.getUserByUsername(securityUtil.getCurrentUsername());

        Map<String,Object> map = new HashMap<>();

        map.put("userTeams", teamService.getTeamInfo(user.getId()));
        map.put("recentGames", gameService.get4MostRecentGames(user.getId()));
        map.put("noTeams",teamService.getNumberOfTeamsByUser(user.getId()));
        map.put("noGames", gameService.getNumberOfGamesByUser(user.getId()));
        map.put("noPlayers",playerService.getNumberOfPlayersByUser(user.getId()));

        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
