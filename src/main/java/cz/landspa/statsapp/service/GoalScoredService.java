package cz.landspa.statsapp.service;

import cz.landspa.statsapp.model.GoalScored;
import org.springframework.stereotype.Service;

@Service
public interface GoalScoredService {
    GoalScored addGoal(GoalScored goalScored);

    GoalScored updateGoal(GoalScored goalScored, Long id);
}
