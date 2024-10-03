package cz.landspa.statsapp.controller.api;

import cz.landspa.statsapp.model.Team;
import cz.landspa.statsapp.model.User;
import cz.landspa.statsapp.service.GameService;
import cz.landspa.statsapp.service.PlayerService;
import cz.landspa.statsapp.service.TeamService;
import cz.landspa.statsapp.service.UserService;
import cz.landspa.statsapp.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/teams")
public class TeamController {

    private final TeamService teamService;
    private final UserService userService;
    private final PlayerService playerService;
    private final GameService gameService;
    private final SecurityUtil securityUtil;

    public TeamController(TeamService teamService, UserService userService, PlayerService playerService, GameService gameService, SecurityUtil securityUtil) {
        this.teamService = teamService;
        this.userService = userService;
        this.playerService = playerService;
        this.gameService = gameService;
        this.securityUtil = securityUtil;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTeamInfo(@PathVariable Long id){

        try{

            return new ResponseEntity<>(teamService.getOneTeamInfo(id), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping
    public ResponseEntity<?> getUserTeams(/*@RequestBody User user*/){
       // User foundUser = userService.findById(user.getId());
        User foundUser = userService.getUserByUsername(securityUtil.getCurrentUsername());


        if(foundUser != null){

            return new ResponseEntity<>(teamService.getTeamInfo(foundUser.getId()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found",HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> newTeam(@Valid @RequestBody Team team){
        try{
            User foundUser = userService.getUserByUsername(securityUtil.getCurrentUsername());
            team.setOwner(foundUser);
            return new ResponseEntity<>(teamService.createTeam(team), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<?> deleteTeam (@PathVariable Long id){
        try {
            teamService.deleteTeam(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editTeam (@PathVariable Long id,@Valid  @RequestBody Team team){
        try {
            return new ResponseEntity<>(teamService.updateTeam(team,id),HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
