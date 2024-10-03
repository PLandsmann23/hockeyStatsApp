package cz.landspa.statsapp.service.impl;

import cz.landspa.statsapp.model.GoalScored;
import cz.landspa.statsapp.repository.GoalScoredRepository;
import cz.landspa.statsapp.service.GoalScoredService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class GoalScoredServiceImpl implements GoalScoredService {
    private final GoalScoredRepository goalScoredRepository;

    public GoalScoredServiceImpl(GoalScoredRepository goalScoredRepository) {
        this.goalScoredRepository = goalScoredRepository;
    }

    @Override
    @PreAuthorize("@gameServiceImpl.isGameOwner(#goalScored.game.id)")
    public GoalScored addGoal(GoalScored goalScored) {
        return goalScoredRepository.save(goalScored);
    }

    @Override
    @PreAuthorize("@eventServiceImpl.isEventOwner(#id) && @gameServiceImpl.isGameOwner(#goalScored.game.id)")
    public GoalScored updateGoal(GoalScored goalScored, Long id) {
        GoalScored oldGoal = goalScoredRepository.findById(id).orElse(null);

        if(oldGoal!= null){
            if(goalScored.getTime()!=null){
                oldGoal.setTime(goalScored.getTime());
            }
            if(goalScored.getScorer()!=null){
                oldGoal.setScorer(goalScored.getScorer());
            }
            if(goalScored.getAssists()!=null){
                oldGoal.setAssists(goalScored.getAssists());
            }
            if(goalScored.getOnIce()!=null){
                oldGoal.setOnIce(goalScored.getOnIce());
            }
            if(goalScored.getSituation()!=null){
                oldGoal.setSituation(goalScored.getSituation());
            }

            return goalScoredRepository.save(oldGoal);
        } else
        {return null;}
    }


}
