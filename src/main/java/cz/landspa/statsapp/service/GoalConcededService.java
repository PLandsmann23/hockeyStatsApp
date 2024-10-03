package cz.landspa.statsapp.service;

import cz.landspa.statsapp.model.GoalConceded;
import org.springframework.stereotype.Service;

@Service
public interface GoalConcededService {
    GoalConceded addGoal(GoalConceded goalConceded);

    GoalConceded updateGoal(GoalConceded goalConceded, Long id);
}
