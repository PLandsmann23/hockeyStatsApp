package cz.landspa.statsapp.controller.api;

import cz.landspa.statsapp.service.StatsService;
import cz.landspa.statsapp.service.TeamService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/stats")
@RestController
public class StatsController {


    private final StatsService statsService;
    private final TeamService teamService;

    public StatsController(StatsService statsService, TeamService teamService) {
        this.statsService = statsService;
        this.teamService = teamService;
    }


    @GetMapping("/{teamId}")
    public ResponseEntity<?> getTeamStatsSummary(@PathVariable Long teamId, @Param("range") String range) {
     try {
            if (teamService != null) {
                if(range ==null) {
                    return new ResponseEntity<>(statsService.getTeamStatsSum(teamId), HttpStatus.OK);
                } else if(range.equals("last5")){
                    return new ResponseEntity<>(statsService.getTeamStatsSumLast5(teamId), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(statsService.getTeamStatsSum(teamId), HttpStatus.OK);

                }
            } else {
                throw new RuntimeException("Tým nebyl nalezen");
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage()+e.getCause().toString(), HttpStatus.BAD_REQUEST);
        }
    }


}
