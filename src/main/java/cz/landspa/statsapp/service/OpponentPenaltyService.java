package cz.landspa.statsapp.service;

import cz.landspa.statsapp.model.OpponentPenalty;
import cz.landspa.statsapp.model.Penalty;
import org.springframework.stereotype.Service;

@Service
public interface OpponentPenaltyService {
    OpponentPenalty addPenalty(OpponentPenalty penalty);

    OpponentPenalty updatePenalty(OpponentPenalty penalty, Long id);

}
