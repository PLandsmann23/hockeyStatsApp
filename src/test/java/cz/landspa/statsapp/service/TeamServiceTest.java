package cz.landspa.statsapp.service;

import cz.landspa.statsapp.model.Team;
import cz.landspa.statsapp.repository.TeamRepository;
import cz.landspa.statsapp.service.impl.TeamServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TeamServiceTest {

    @InjectMocks
    TeamServiceImpl teamService;

    @Mock
    TeamRepository teamRepository;

    public TeamServiceTest (){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void saveTeam(){
        Team team = new Team();
        team.setName("Tým 1");

        when(teamRepository.save(any(Team.class))).thenReturn(team);

        Team savedTeam = teamService.createTeam(team);

        assertThat(savedTeam).isNotNull();
        assertThat(savedTeam.getName()).isEqualTo("Tým 1");
        verify(teamRepository, times(1)).save(team);
    }

    @Test
    public void getTeamById(){
        Team team = new Team();
        team.setName("Tým 1");
        team.setId(1L);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        Team savedTeam = teamService.getTeamById(1L);

        assertThat(savedTeam).isNotNull();
        assertThat(savedTeam.getName()).isEqualTo("Tým 1");
        verify(teamRepository, times(1)).findById(1L);
    }

    @Test
    public void updateTeam(){
        Team team = new Team();
        team.setName("Tým 1");
        team.setId(1L);

        Team teamUpdt = new Team();
        teamUpdt.setName("Tým 1 updt");

        Team teamUpdtDB = new Team();
        teamUpdtDB.setName("Tým 1 updt");
        teamUpdtDB.setId(1L);

        when(teamRepository.save(any(Team.class))).thenReturn(teamUpdtDB);



        Team updt = teamService.updateTeam(teamUpdt,1L);

        when(teamRepository.save(any(Team.class))).thenReturn(updt);

        assertThat(teamUpdt.getName()).isEqualTo("Tým 1 updt");
    }
}
