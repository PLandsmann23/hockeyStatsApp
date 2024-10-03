package cz.landspa.statsapp.repository;

import cz.landspa.statsapp.model.DTO.team.TeamCountInfo;
import cz.landspa.statsapp.model.Team;
import cz.landspa.statsapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    List<Team> findAllByOwner (User user);

    Long countByOwnerId(Long userId);


    @Query("SELECT new cz.landspa.statsapp.model.DTO.team.TeamCountInfo(t, COUNT (DISTINCT p), COUNT (DISTINCT g)) FROM Team t LEFT JOIN Player p ON p.team.id = t.id  LEFT JOIN Game g ON g.team.id = t.id WHERE t.owner.id = :userId GROUP BY t.id")
    List<TeamCountInfo> findTeamsWithPlayerCountAndGameCountByUserId(@Param("userId") Long userId);

}
