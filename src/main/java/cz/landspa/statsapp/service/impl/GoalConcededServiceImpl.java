package cz.landspa.statsapp.service.impl;

import cz.landspa.statsapp.model.GoalConceded;
import cz.landspa.statsapp.model.Team;
import cz.landspa.statsapp.repository.GoalConcededRepository;
import cz.landspa.statsapp.service.GoalConcededService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class GoalConcededServiceImpl implements GoalConcededService {
    private final GoalConcededRepository goalConcededRepository;

    public GoalConcededServiceImpl(GoalConcededRepository goalConcededRepository) {
        this.goalConcededRepository = goalConcededRepository;
    }

    @Override
    @PreAuthorize("@gameServiceImpl.isGameOwner(#goalConceded.game.id)")
    public GoalConceded addGoal(GoalConceded goalConceded) {
        return goalConcededRepository.save(goalConceded);
    }

    @Override
    @PreAuthorize("@eventServiceImpl.isEventOwner(#id) && @gameServiceImpl.isGameOwner(#goalConceded.game.id)")
    public GoalConceded updateGoal(GoalConceded goalConceded, Long id) {
        GoalConceded oldGoal = goalConcededRepository.findById(id).orElse(null);

        if(oldGoal!= null){
            if(goalConceded.getTime()!=null){
                oldGoal.setTime(goalConceded.getTime());
            }

            if(goalConceded.getOnIce()!=null){
                oldGoal.setOnIce(goalConceded.getOnIce());
            }
            if(goalConceded.getSituation()!=null){
                oldGoal.setSituation(goalConceded.getSituation());
            }

            return goalConcededRepository.save(oldGoal);
        } else
        {return null;}
    }




}
