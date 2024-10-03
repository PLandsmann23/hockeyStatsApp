package cz.landspa.statsapp.service;

import cz.landspa.statsapp.model.Penalty;
import org.springframework.stereotype.Service;

@Service
public interface PenaltyService {
    Penalty addPenalty(Penalty penalty);

    Penalty updatePenalty(Penalty penalty, Long id);


}
