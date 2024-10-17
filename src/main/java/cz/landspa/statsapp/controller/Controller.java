package cz.landspa.statsapp.controller;


import cz.landspa.statsapp.model.Game;
import cz.landspa.statsapp.model.Team;
import cz.landspa.statsapp.service.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;


@org.springframework.stereotype.Controller
@RequestMapping("/")
public class Controller {

    private final GameService gameService;
    private final TeamService teamService;
    private final GameStatsService gameStatsService;
    private final EventService eventService;
    private final UserService userService;

    public Controller(GameService gameService, TeamService teamService, GameStatsService gameStatsService, EventService eventService, UserService userService) {
        this.gameService = gameService;
        this.teamService = teamService;
        this.gameStatsService = gameStatsService;
        this.eventService = eventService;
        this.userService = userService;
    }

    @GetMapping("")
    public String index(Model model){
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            model.addAttribute("username", auth.getPrincipal());
            model.addAttribute("settings", null);

            return "index";
        } catch (Exception e){
            throw new RuntimeException();

        }
    }

    @GetMapping("teams")
    public String myTeams(Model model){
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            model.addAttribute("username", auth.getPrincipal());
            return "myteams";
        }catch (Exception e){
            throw new RuntimeException(e);

        }
    }

    @GetMapping("teams/{id}")
    public String teamDetail(Model model, @PathVariable Long id){
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            model.addAttribute("username", auth.getPrincipal());
            Team team = teamService.getTeamById(id);
            if (team != null) {
                return "teamdetail";
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tým nebyl nalezen");
            }
        }catch (Exception e){
            throw new RuntimeException(e);

        }
    }

    @GetMapping("games/{id}")
    public String gameRoster(Model model, @PathVariable Long id){
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            model.addAttribute("username", auth.getPrincipal());
            Game game= gameService.getGameById(id);
            if(game!= null) {
            model.addAttribute("teamId", gameService.getGameById(id).getTeam().getId());
            model.addAttribute("id", id);
            return "pregame";}
            else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Zápas nebyl nalezen");

            }
        } catch (Exception e){
           throw new RuntimeException(e);

        }
    }

    @GetMapping("games/{id}/record")
    public String statsRecord(Model model, @PathVariable Long id){
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            model.addAttribute("username", auth.getPrincipal());
            Game game= gameService.getGameById(id);
            if(game!= null) {
                model.addAttribute("id", id);
                return "record";
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Zápas nebyl nalezen");
            }
        }
    catch (Exception e){
            throw new RuntimeException(e);

        }
    }

    @GetMapping("games/{id}/summary")
    public String summary(Model model, @PathVariable Long id){
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            model.addAttribute("username", auth.getPrincipal());
            if(gameService.getGameById(id)!=null){
                model.addAttribute("id", id);
                model.addAttribute("playerStats", gameStatsService.getGamePlayerStats(id));
                model.addAttribute("goalieStats", gameStatsService.getGameGoalieStats(id));
                model.addAttribute("events", eventService.getEventsNoDTO(id));
                model.addAttribute("gameStats", gameStatsService.getGameStats(id));
                return "summary";
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e){
            throw
                    new RuntimeException(e);

        }
    }

    @GetMapping("stats")
    public String stats(Model model){
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            model.addAttribute("username", auth.getPrincipal());
            return "stats";
        } catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    @GetMapping("settings")
    public String settingsPage(Model model)
    {try{
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getPrincipal());
        return "settings";
    }catch (Exception e){
        throw new RuntimeException(e);

    }}

    @GetMapping("help")
    public String supportPage(Model model)
    {try{
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getPrincipal());
        return "support";
    }catch (Exception e){
        throw new RuntimeException(e);

    }}

    @GetMapping("activate")
    public String activateAccount(@RequestParam("token") String token){
        try {


            if(!userService.activateUser(token)){
               throw new RuntimeException();
            }

            return "validToken";
        } catch (Exception e){
            return "invalidToken";
        }
    }

    @GetMapping("login")
    public String loginPage()
    {return "login";}

    @GetMapping("register")
    public String registerPage()
    {return "register";}


    @GetMapping("test")
    public String testPage(){return "test";}
}
