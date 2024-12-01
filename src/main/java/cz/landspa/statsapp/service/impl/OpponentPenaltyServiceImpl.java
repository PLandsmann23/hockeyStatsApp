package cz.landspa.statsapp.service.impl;

import cz.landspa.statsapp.model.OpponentPenalty;
import cz.landspa.statsapp.model.Penalty;
import cz.landspa.statsapp.repository.OpponentPenaltyRepository;
import cz.landspa.statsapp.service.OpponentPenaltyService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class OpponentPenaltyServiceImpl implements OpponentPenaltyService {
    private final OpponentPenaltyRepository opponentPenaltyRepository;

    public OpponentPenaltyServiceImpl(OpponentPenaltyRepository opponentPenaltyRepository) {
        this.opponentPenaltyRepository = opponentPenaltyRepository;
    }

    @Override
    @PreAuthorize("@gameServiceImpl.isGameOwner(#penalty.game.id)")
    public OpponentPenalty addPenalty(OpponentPenalty penalty) {
        return opponentPenaltyRepository.save(penalty);
    }

    @Override
    @PreAuthorize("@eventServiceImpl.isEventOwner(#id) && @gameServiceImpl.isGameOwner(#penalty.game.id)")
    public OpponentPenalty updatePenalty(OpponentPenalty penalty, Long id) {
        OpponentPenalty oldPenalty = opponentPenaltyRepository.findById(id).orElse(null);

        if(oldPenalty != null){
            if(penalty.getTime()!=null){
                oldPenalty.setTime(penalty.getTime());
            }
            if(penalty.getMinutes()!=null){
                oldPenalty.setMinutes(penalty.getMinutes());
            }
            if(penalty.getCoincidental()!=null){
                oldPenalty.setCoincidental(penalty.getCoincidental());
            }


            return opponentPenaltyRepository.save(oldPenalty);
        } else {
            return null;
        }
    }
}
